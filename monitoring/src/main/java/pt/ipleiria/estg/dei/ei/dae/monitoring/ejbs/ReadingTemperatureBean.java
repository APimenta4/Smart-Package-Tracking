package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.ReadingTemperature;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.VolumeStatus;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.time.Instant;
import java.util.EnumSet;
import java.util.logging.Logger;

@Stateless
public class ReadingTemperatureBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SensorBean sensorBean;

    private static final Logger logger = Logger.getLogger("ejbs.ReadingTemperatureBean");

    public boolean exists(Long id) {
        Query query = em.createQuery(
                "SELECT COUNT(rt.id) FROM ReadingTemperature rt WHERE rt.id = :id",
                Long.class
        );
        query.setParameter("id", id);
        return (Long)query.getSingleResult() > 0L;
    }

    public ReadingTemperature create(String sensorCode, Double temperature)
            throws CustomConstraintViolationException, CustomEntityNotFoundException {
        logger.info("Creating new temperature reading, sensor '" + sensorCode+"'");
        Sensor sensor = sensorBean.find(sensorCode);

        try {
            VolumeStatus status = sensor.getVolume().getStatus();
            if (EnumSet.of(VolumeStatus.CANCELLED, VolumeStatus.RETURNED, VolumeStatus.DELIVERED).contains(status)) {
                throw new CustomConstraintViolationException("Volume associated with the sensor '"+ sensorCode +"' has status "+status);
            }
            ReadingTemperature readingTemperature = new ReadingTemperature(sensor, Instant.now(),temperature);
            em.persist(readingTemperature);
            return readingTemperature;
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public ReadingTemperature find(Long id)
            throws CustomEntityNotFoundException {
        ReadingTemperature readingTemperature = em.find(ReadingTemperature.class, id);
        if (readingTemperature == null) {
            throw new CustomEntityNotFoundException("ReadingTemperature", Long.toString(id));
        }
        return readingTemperature;
    }
}
