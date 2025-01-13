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
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.VolumeStatus;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

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
        String volumeCode = volumeDTO.getCode();
        create(volumeCode, orderCode, volumeDTO.getPackageType());

        long countAcc = 0;
        long countLoc = 0;
        long countTemp = 0;

        for (ProductDTO productDTO : volumeDTO.getProducts()) {
            Product product = productBean.find(productDTO.getCode());

            lineOfSaleBean.create(volumeCode, productDTO.getCode(), productDTO.getQuantity());


            //updateCounter(product.getPackageType(),productDTO.getQuantity());


            switch (product.getPackageType()) {
                case FRAGILE:
                    countAcc = productDTO.getQuantity();
                    break;
                case ISOTERMIC:
                    countLoc = productDTO.getQuantity();
                    break;
                case GEOLOCATION:
                    countTemp = productDTO.getQuantity();
                    break;
                default:
                    throw new CustomConstraintViolationException("Unknown status " + product.getPackageType());
            }


        }
        for (SensorDTO sensorDTO : volumeDTO.getSensors()) {

            Sensor sensor = sensorBean.find(sensorDTO.getCode());

            sensorBean.create(sensorDTO.getCode(), volumeCode,sensorDTO.getType());

            switch (sensor.getType()) {
                case ACCELERATION:
                    countAcc -= 1;
                    break;
                case TEMPERATURE:
                    countLoc -= 1;
                    break;
                case LOCATION:
                    countTemp -= 1;
                    break;
                default:
                    throw new CustomConstraintViolationException("Unknown status " + sensor.getType());
            }
        }

        //verify
        if( countAcc != 0 || countTemp != 0 || countLoc != 0){
            throw new CustomConstraintViolationException("Sensores and Products Quantity and/or PackageTypes doesn't correspond");
        }

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

        validateStatusChange(volume, currentStatus, newStatus);

        em.lock(volume, LockModeType.OPTIMISTIC);
        logger.info("Updating volume '" + code + "' from status '" + currentStatus + "' to '" + newStatus + "'");
        try {
            updateVolumeStatus(volume, currentStatus, newStatus);
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

    public void delete(String code) throws CustomEntityNotFoundException {
        Volume volume = find(code);
        // Locks object that is being deleted
        em.lock(volume, LockModeType.PESSIMISTIC_WRITE);
        logger.info("Deleting volume '" + code + "'");
        em.remove(volume);
    }
}
