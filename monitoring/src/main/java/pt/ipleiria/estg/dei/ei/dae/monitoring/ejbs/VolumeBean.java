package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.VolumeStatus;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.List;
import java.util.logging.Logger;

@Stateless
public class VolumeBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private OrderBean orderBean;

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

    public void create(String code, String orderCode, PackageType packageType)
            throws CustomEntityExistsException, CustomEntityNotFoundException, CustomConstraintViolationException {
        logger.info("Creating new Volume '" + code + "'");
        if (exists(code)) {
            throw new CustomEntityExistsException("Volume",code);
        }
        Order order = orderBean.find(orderCode);
        try{
            // TODO: verify association in case of invalid volume
            Volume volume = new Volume(code, order, VolumeStatus.READY_FOR_PICKUP, packageType);
            order.addVolume(volume);
            em.persist(volume);
            em.flush();
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

    public void initializeEntities(Volume volume) {
        Hibernate.initialize(volume.getLineOfSales());
        Hibernate.initialize(volume.getSensors());
    }

    public List<Volume> findAllWithAllDetails() {
        List<Volume> volumes = em.createNamedQuery("getAllVolumes", Volume.class).getResultList();
        volumes.forEach(this::initializeEntities);
        return volumes;
    }

    public Volume findWithAllDetails(String code)
            throws CustomEntityNotFoundException {
        Volume volume = find(code);
        initializeEntities(volume);
        return volume;
    }

    public void updateStatus(String code, VolumeStatus status)
            throws CustomEntityNotFoundException, CustomConstraintViolationException {
        Volume volume = find(code);
        em.lock(volume, LockModeType.OPTIMISTIC);
        logger.info("Updating volume '" + code + "' to status '" + status.toString() + "'");
        try{
            volume.setStatus(status);
            em.flush();
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public void delete(String code) throws CustomEntityNotFoundException {
        Volume volume = find(code);
        // Locks object that is being deleted
        em.lock(volume, LockModeType.PESSIMISTIC_WRITE);
        logger.info("Deleting volume '" + code + "'");
        em.remove(volume);
    }
}
