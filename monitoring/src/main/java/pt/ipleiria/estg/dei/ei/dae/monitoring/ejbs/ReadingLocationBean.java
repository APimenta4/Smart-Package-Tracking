package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.ReadingLocation;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.VolumeStatus;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.time.Instant;
import java.util.EnumSet;
import java.util.logging.Logger;

@Stateless
public class ReadingLocationBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SensorBean sensorBean;

    private static final Logger logger = Logger.getLogger("ejbs.ReadingLocationBean");

    public boolean exists(Long id) {
        Query query = em.createQuery(
                "SELECT COUNT(rl.id) FROM ReadingLocation rl WHERE rl.id = :id",
                Long.class
        );
        query.setParameter("id", id);
        return (Long)query.getSingleResult() > 0L;
    }

    public ReadingLocation create(String sensorCode, Double latitude, Double longitude)
            throws CustomConstraintViolationException, CustomEntityNotFoundException {
        logger.info("Creating new location reading, sensor '" + sensorCode+"'");
        if (latitude < -90.0 || latitude > 90.0) {
            throw new CustomConstraintViolationException("Latitude must be between -90.0 and 90.0. Provided: " + latitude);
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new CustomConstraintViolationException("Longitude must be between -180.0 and 180.0. Provided: " + longitude);
        }

        Sensor sensor = sensorBean.find(sensorCode);
        try {
            VolumeStatus status = sensor.getVolume().getStatus();
            if (EnumSet.of(VolumeStatus.CANCELLED, VolumeStatus.RETURNED, VolumeStatus.DELIVERED).contains(status)) {
                throw new CustomConstraintViolationException("Volume associated with the sensor '"+ sensorCode +"' has status "+status);
            }
            ReadingLocation readingLocation = new ReadingLocation(sensor, Instant.now(),latitude, longitude);
            em.persist(readingLocation);
            return readingLocation;
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public ReadingLocation find(Long id)
            throws CustomEntityNotFoundException {
        ReadingLocation readingLocation = em.find(ReadingLocation.class, id);
        if (readingLocation == null) {
            throw new CustomEntityNotFoundException("ReadingLocation", Long.toString(id));
        }
        return readingLocation;
    }
}
