package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Logistician;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.security.Hasher;

import java.util.logging.Logger;

@Stateless
public class LogisticianBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private UserBean userBean;

    @Inject
    private Hasher hasher;

    private static final Logger logger = Logger.getLogger("ejbs.LogisticianBean");

    public void create(String code, String name, String email, String password)
            throws CustomEntityExistsException, CustomConstraintViolationException {
        logger.info("Creating new logistician '" + code + "'");
        if (userBean.exists(code)) {
            throw new CustomEntityExistsException("User", code);
        }
        try {
            Logistician logistician = new Logistician(code, name, email, hasher.hash(password));
            em.persist(logistician);
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public Logistician find(String code)
            throws CustomEntityNotFoundException {
        Logistician logistician = em.find(Logistician.class, code);
        if (logistician == null) {
            throw new CustomEntityNotFoundException("Logistician", code);
        }
        return logistician;
    }

    // TODO: not used
    public void update(String code, String name, String email)
            throws CustomEntityNotFoundException, CustomConstraintViolationException {
        Logistician logistician = find(code);
        em.lock(logistician, LockModeType.OPTIMISTIC);
        logger.info("Updating logistician '" + code + "'");
        try{
            logistician.setEmail(email);
            logistician.setName(name);
            em.flush();
        }catch (ConstraintViolationException e){
            throw new CustomConstraintViolationException(e);
        }
    }


    public void delete(String code) throws CustomEntityNotFoundException {
        Logistician logistician = find(code);
        // Locks object that is being deleted
        em.lock(logistician, LockModeType.PESSIMISTIC_WRITE);
        logger.info("Deleting logistician '" + code + "'");
        em.remove(logistician);
    }
}
