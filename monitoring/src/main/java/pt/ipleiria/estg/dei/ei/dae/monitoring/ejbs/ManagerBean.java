package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Manager;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.security.Hasher;

import java.util.logging.Logger;

@Stateless
public class ManagerBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private UserBean userBean;

    @Inject
    private Hasher hasher;

    private static final Logger logger = Logger.getLogger("ejbs.ManagerBean");

    public void create(String code, String name, String email, String password)
            throws CustomEntityExistsException, CustomConstraintViolationException {
        logger.info("Creating new manager '" + code + "'");
        if (userBean.exists(code)) {
            throw new CustomEntityExistsException("User", code);
        }
        try {
            Manager manager = new Manager(code, name, email, hasher.hash(password));
            em.persist(manager);

        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public Manager find(String code)
            throws CustomEntityNotFoundException {
        Manager manager = em.find(Manager.class, code);
        if (manager == null) {
            throw new CustomEntityNotFoundException("Manager", code);
        }
        return manager;
    }

    // TODO: not used
    public void update(String code, String name, String email)
            throws CustomEntityNotFoundException, CustomConstraintViolationException {
        Manager manager = find(code);
        em.lock(manager, LockModeType.OPTIMISTIC);
        logger.info("Updating manager '" + code + "'");
        try{
            manager.setEmail(email);
            manager.setName(name);
            em.flush();
        }catch (ConstraintViolationException e){
            throw new CustomConstraintViolationException(e);
        }
    }

    public void delete(String code) throws CustomEntityNotFoundException {
        Manager manager = find(code);
        // Locks object that is being deleted
        em.lock(manager, LockModeType.PESSIMISTIC_WRITE);
        logger.info("Deleting client '" + code + "'");
        em.remove(manager);
    }
}
