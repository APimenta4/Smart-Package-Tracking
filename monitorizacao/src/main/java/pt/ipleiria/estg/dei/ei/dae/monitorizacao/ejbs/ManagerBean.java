package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Manager;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.security.Hasher;

import java.util.logging.Logger;

@Stateless
public class ManagerBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    private static final Logger logger = Logger.getLogger("ejbs.ManagerBean");

    public boolean exists(long code) {
        Query query = em.createQuery(
                "SELECT COUNT(s.code) FROM Client s WHERE s.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(long code, String name, String email, String password)
            throws CustomEntityExistsException, CustomConstraintViolationException {
        if (exists(code)){
            throw new CustomEntityExistsException("Manager '" +code+ "'");
        }
        try {
            logger.info("Creating new manager '" + code + "'");
            Manager manager = new Manager(code, name, email, hasher.hash(password));
            em.persist(manager);

        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public Manager find(long code)
            throws CustomEntityNotFoundException {
        Manager manager = em.find(Manager.class, code);
        if (manager == null) {
            throw new CustomEntityNotFoundException("Client '" +code+ "'");
        }
        return manager;
    }

    public void update(long code, String email, String name, String password)
            throws CustomEntityNotFoundException, IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name '"+name+"' cannot be null or blank.");
        }
        Manager manager = find(code);
        em.lock(manager, LockModeType.OPTIMISTIC);
        logger.info("Updating manager '" + code + "'");
        // Update user
        manager.setEmail(email);
        manager.setName(name);
    }

    public void delete(long code) throws CustomEntityNotFoundException {
        Manager manager = find(code);
        // Locks object that is being deleted
        em.lock(manager, LockModeType.PESSIMISTIC_WRITE);
        logger.info("Deleting client '" + code + "'");
        em.remove(manager);
    }
}
