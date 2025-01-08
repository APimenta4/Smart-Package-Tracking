package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.ReadingLocation;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;

import java.time.Instant;
import java.util.logging.Logger;

@Stateless
public class ReadingLocationBean {
    @PersistenceContext
    private EntityManager em;

    private static final Logger logger = Logger.getLogger("ejbs.ReadingLocationBean");

    public boolean exists(long id) {
        Query query = em.createQuery(
                "SELECT COUNT(rl.id) FROM ReadingLocation rl WHERE rl.id = :id",
                Long.class
        );
        query.setParameter("id", id);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(Sensor sensor, double latitude, double longitude)
            throws CustomConstraintViolationException {
        logger.info("Creating new location reading");
        try {
            ReadingLocation readingLocation = new ReadingLocation(sensor, Instant.now(),latitude, longitude);
            em.persist(readingLocation);

        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public ReadingLocation find(long id)
            throws CustomEntityNotFoundException {
        ReadingLocation readingLocation = em.find(ReadingLocation.class, id);
        if (readingLocation == null) {
            throw new CustomEntityNotFoundException("ReadingLocation", Long.toString(id));
        }
        return readingLocation;
    }
}
