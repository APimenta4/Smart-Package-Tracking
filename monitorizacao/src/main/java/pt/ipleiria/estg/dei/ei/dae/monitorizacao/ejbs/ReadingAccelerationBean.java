package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.ReadingAcceleration;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;

import java.time.Instant;
import java.util.logging.Logger;

@Stateless
public class ReadingAccelerationBean {
    @PersistenceContext
    private EntityManager em;

    private static final Logger logger = Logger.getLogger("ejbs.ReadingAccelerationBean");

    public boolean exists(long id) {
        Query query = em.createQuery(
                "SELECT COUNT(ra.id) FROM ReadingAcceleration ra WHERE ra.id = :id",
                Long.class
        );
        query.setParameter("id", id);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(Sensor sensor, double acceleration)
            throws CustomConstraintViolationException {
        logger.info("Creating new acceleration reading");
        try {
            ReadingAcceleration readingAcceleration = new ReadingAcceleration(sensor, Instant.now(),acceleration);
            em.persist(readingAcceleration);

        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public ReadingAcceleration find(long id)
            throws CustomEntityNotFoundException {
        ReadingAcceleration readingAcceleration = em.find(ReadingAcceleration.class, id);
        if (readingAcceleration == null) {
            throw new CustomEntityNotFoundException("ReadingAcceleration", Long.toString(id));
        }
        return readingAcceleration;
    }
}
