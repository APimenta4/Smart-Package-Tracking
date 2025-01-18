package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.SensorType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.VolumeStatus;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.utils.Counts;

import java.util.*;
import java.util.logging.Logger;

@Stateless
public class VolumeBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private ClientBean clientBean;

    @EJB
    private OrderBean orderBean;

    @EJB
    private LineOfSaleBean lineOfSaleBean;

    @EJB
    private SensorBean sensorBean;

    @EJB
    private ProductBean productBean;
    @EJB
    private EmailBean emailBean;

    private static final Logger logger = Logger.getLogger("ejbs.VolumeBean");

    public boolean exists(String code) {
        Query query = em.createQuery(
                "SELECT COUNT(v.code) FROM Volume v WHERE v.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }

    @Transactional(rollbackOn = Exception.class)
    public void createWithDetails(VolumeDTO volumeDTO, String orderCode)
            throws CustomConstraintViolationException, CustomEntityNotFoundException, CustomEntityExistsException {
        createWithDetailsNoTransaction(volumeDTO, orderCode);
    }

    public void createWithDetailsNoTransaction(VolumeDTO volumeDTO, String orderCode)
            throws CustomConstraintViolationException, CustomEntityNotFoundException, CustomEntityExistsException {
        Counts counts = new Counts();
        String volumeCode = volumeDTO.getCode();
        PackageType volumePackageType = volumeDTO.getPackageType();

        List<ProductDTO> productDTOs = volumeDTO.getProducts();
        if (productDTOs.isEmpty()){
            throw new CustomConstraintViolationException("Empty or missing product list");
        }

        create(volumeCode, orderCode, volumePackageType);
        counts.add(volumePackageType, 1L);

        // Process Products
        for (ProductDTO productDTO : productDTOs) {
            String productCode = productDTO.getCode();
            Product product = productBean.find(productCode);
            PackageType productPackageType = product.getPackageType();
            if (volumePackageType == PackageType.NONE && productPackageType == PackageType.NONE) {
                throw new CustomConstraintViolationException("Cannot have both volume and product with NONE package type");
            }

            Long productQuantity = productDTO.getQuantity();
            lineOfSaleBean.create(volumeCode, productCode, productQuantity);
            counts.add(productPackageType, productQuantity);
        }

        // Process Sensors
        for (SensorDTO sensorDTO : volumeDTO.getSensors()) {
            SensorType sensorType = sensorDTO.getType();

            sensorBean.create(sensorDTO.getCode(), volumeCode, sensorType);
            counts.remove(sensorType);
        }

        counts.validate();

        try{
            Volume volume = find(volumeCode);
            sendEmail(volume.getOrder().getClient(), volumeCode);
        } catch (Exception ignored){}
    }

    public void create(String code, String orderCode, PackageType packageType)
            throws CustomEntityExistsException, CustomEntityNotFoundException, CustomConstraintViolationException {
        logger.info("Creating new Volume '" + code + "'");
        if (exists(code)) {
            throw new CustomEntityExistsException("Volume",code);
        }
        Order order = orderBean.find(orderCode);
        try{
            Volume volume = new Volume(code, order, packageType);
            order.addVolume(volume);
            em.persist(volume);
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public Volume find(String code) throws CustomEntityNotFoundException {
        var volume = em.find(Volume.class, code);
        if (volume == null) {
            throw new CustomEntityNotFoundException("Volume",code);
        }
        return volume;
    }

    public Volume findWithReadings(String code) throws CustomEntityNotFoundException {
        Volume volume = find(code);
        List<Sensor> sensors = volume.getSensors();
        Hibernate.initialize(sensors);
        for (Sensor sensor :sensors){
            Hibernate.initialize(sensor.getReadings());
        }
        return volume;
    }

    public Volume findWithAllDetails(String code)
            throws CustomEntityNotFoundException {
        Volume volume = find(code);
        Hibernate.initialize(volume.getLineOfSales());
        Hibernate.initialize(volume.getSensors());
        return volume;
    }

    public List<Volume> findAllWithAllDetails() {
        List<Volume> volumes = em.createNamedQuery("getAllVolumes", Volume.class).getResultList();
        for(Volume volume : volumes) {
            Hibernate.initialize(volume.getLineOfSales());
            Hibernate.initialize(volume.getSensors());
        }
        return volumes;
    }

    public List<Volume> findAllWithAllDetails(String clientCode) throws CustomEntityNotFoundException {
        Client client = clientBean.find(clientCode);
        List<Volume> volumes = client.getVolumes();
        for(Volume volume : volumes) {
            Hibernate.initialize(volume.getLineOfSales());
            Hibernate.initialize(volume.getSensors());
        }
        return volumes;
    }

    public void updateStatus(String code, VolumeStatus newStatus)
            throws CustomEntityNotFoundException, CustomConstraintViolationException {

        Volume volume = find(code);
        VolumeStatus currentStatus = volume.getStatus();

        Client client = volume.getOrder().getClient();

        validateStatusChange(volume, currentStatus, newStatus);

        em.lock(volume, LockModeType.OPTIMISTIC);
        logger.info("Updating volume '" + code + "' from status '" + currentStatus + "' to '" + newStatus + "'");
        try {
            updateVolumeStatus(volume, currentStatus, newStatus);

            try{
                sendEmail(client, code, currentStatus, newStatus);
            } catch (Exception ignored){}

        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    private void validateStatusChange(Volume volume, VolumeStatus currentStatus, VolumeStatus newStatus)
            throws CustomConstraintViolationException {
        if (currentStatus == newStatus) {
            throw new CustomConstraintViolationException(
                    "Volume '" + volume.getCode() + "' already has status '" + currentStatus + "'");
        }

        if (newStatus == VolumeStatus.READY_FOR_PICKUP) {
            throw new CustomConstraintViolationException(
                    generateStatusErrorMessage(volume, currentStatus, newStatus));
        }

        if (EnumSet.of(VolumeStatus.CANCELLED, VolumeStatus.RETURNED, VolumeStatus.DELIVERED).contains(currentStatus)) {
            throw new CustomConstraintViolationException(
                    generateStatusErrorMessage(volume, currentStatus, newStatus));
        }
    }

    private void updateVolumeStatus(Volume volume, VolumeStatus currentStatus, VolumeStatus newStatus)
            throws CustomConstraintViolationException {

        switch (currentStatus) {
            case READY_FOR_PICKUP:
                handleReadyForPickupTransition(volume, newStatus);
                break;
            case IN_TRANSIT:
                handleInTransitTransition(volume, newStatus);
                break;
            default:
                throw new CustomConstraintViolationException("Unknown status " + currentStatus);
        }

        volume.setStatus(newStatus);
    }

    private void handleReadyForPickupTransition(Volume volume, VolumeStatus newStatus)
            throws CustomConstraintViolationException {

        switch (newStatus) {
            case IN_TRANSIT:
                volume.setShippedDate(new Date());
                break;
            case CANCELLED:
                volume.setCancelledDate(new Date());
                break;
            default:
                throw new CustomConstraintViolationException(
                    generateStatusErrorMessage(volume, volume.getStatus(), newStatus)
                );
        }
    }

    private void handleInTransitTransition(Volume volume, VolumeStatus newStatus)
            throws CustomConstraintViolationException {

        switch (newStatus) {
            case DELIVERED:
                volume.setDeliveredDate(new Date());
                break;
            case RETURNED:
                volume.setReturnedDate(new Date());
                break;
            case CANCELLED:
                volume.setCancelledDate(new Date());
                break;
            default:
                throw new CustomConstraintViolationException(
                    generateStatusErrorMessage(volume, volume.getStatus(), newStatus)
                );
        }
    }

    private String generateStatusErrorMessage(Volume volume, VolumeStatus currentStatus, VolumeStatus newStatus) {
        return "Cannot change status to '" + newStatus + "' when volume '" + volume.getCode() +
                "' status is '" + currentStatus + "'";
    }

    private void sendEmail(Client client, String volumeCode, VolumeStatus currentStatus, VolumeStatus newStatus){
        String subject = "Volume Status Updated";
        String body = "Dear " + client.getName() + ",\n\n"
                + "We would like to inform you that the status of your volume '" + volumeCode
                + "' has been updated to '" + newStatus + "'."
                + "\n\nIf you have any questions or need further clarification, please feel free to contact us."
                + "\n\nSincerely,\nManagement Team";

        emailBean.send(client.getEmail(), subject, body);
    }

    private void sendEmail(Client client, String volumeCode) {
        String subject = "Volume Status Update";
        String body = "Dear " + client.getName() + ",\n\n"
                + "We are pleased to inform you that your volume '" + volumeCode
                + "' has been successfully processed and is now awaiting distribution."
                + "\n\nIf you have any questions or need further assistance, "
                + "please do not hesitate to reach out to us."
                + "\n\nBest regards,\nManagement Team";
        emailBean.send(client.getEmail(), subject, body);
    }

    public void delete(String code) throws CustomEntityNotFoundException {
        Volume volume = find(code);
        // Locks object that is being deleted
        em.lock(volume, LockModeType.PESSIMISTIC_WRITE);
        logger.info("Deleting volume '" + code + "'");
        em.remove(volume);
    }
}
