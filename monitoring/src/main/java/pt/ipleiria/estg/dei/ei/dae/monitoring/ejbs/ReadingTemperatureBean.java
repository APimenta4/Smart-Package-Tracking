package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.ReadingTemperature;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.time.Instant;
import java.util.logging.Logger;

@Stateless
public class ReadingTemperatureBean {
    @PersistenceContext
    private EntityManager em;

    private static final Logger logger = Logger.getLogger("ejbs.ReadingTemperatureBean");

    public boolean exists(long id) {
        Query query = em.createQuery(
                "SELECT COUNT(rt.id) FROM ReadingTemperature rt WHERE rt.id = :id",
                Long.class
        );
        query.setParameter("id", id);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(Sensor sensor, double temperature)
            throws CustomConstraintViolationException {
        logger.info("Creating new temperature reading");
        try {
            ReadingTemperature readingTemperature = new ReadingTemperature(sensor, Instant.now(),temperature);
            em.persist(readingTemperature);

        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public ReadingTemperature find(long id)
            throws CustomEntityNotFoundException {
        ReadingTemperature readingTemperature = em.find(ReadingTemperature.class, id);
        if (readingTemperature == null) {
            throw new CustomEntityNotFoundException("ReadingTemperature", Long.toString(id));
        }
        return readingTemperature;
    }
}
