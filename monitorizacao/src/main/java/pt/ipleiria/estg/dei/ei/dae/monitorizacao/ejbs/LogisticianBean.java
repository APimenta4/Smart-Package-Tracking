package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Logistician;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.security.Hasher;

import java.util.logging.Logger;

@Stateless
public class LogisticianBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    private static final Logger logger = Logger.getLogger("ejbs.LogisticianBean");

    public boolean exists(long code) {
        Query query = em.createQuery(
                "SELECT COUNT(l.code) FROM Logistician l WHERE l.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(long code, String name, String email, String password)
            throws CustomEntityExistsException, CustomConstraintViolationException {
        if (exists(code)){
            throw new CustomEntityExistsException("Logistician '" +code+ "'");
        }
        try {
            logger.info("Creating new logistician '" + code + "'");
            Logistician logistician = new Logistician(code, name, email, hasher.hash(password));
            em.persist(logistician);

        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public Logistician find(long code)
            throws CustomEntityNotFoundException {
        Logistician logistician = em.find(Logistician.class, code);
        if (logistician == null) {
            throw new CustomEntityNotFoundException("Logistician '" +code+ "'");
        }
        return logistician;
    }

    public void update(long code, String email, String name, String password)
            throws CustomEntityNotFoundException, IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name '"+name+"' cannot be null or blank.");
        }
        Logistician logistician = find(code);
        em.lock(logistician, LockModeType.OPTIMISTIC);
        logger.info("Updating logistician '" + code + "'");
        // Update user
        logistician.setEmail(email);
        logistician.setName(name);
    }


    public void delete(long code) throws CustomEntityNotFoundException {
        Logistician logistician = find(code);
        // Locks object that is being deleted
        em.lock(logistician, LockModeType.PESSIMISTIC_WRITE);
        logger.info("Deleting logistician '" + code + "'");
        em.remove(logistician);
    }
}
