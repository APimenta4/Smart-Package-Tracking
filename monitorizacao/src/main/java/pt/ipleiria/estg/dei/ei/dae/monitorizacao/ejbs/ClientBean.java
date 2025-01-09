package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Client;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.security.Hasher;

import java.util.List;
import java.util.logging.Logger;

@Stateless
public class ClientBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private UserBean userBean;

    @Inject
    private Hasher hasher;

    private static final Logger logger = Logger.getLogger("ejbs.ClientBean");

    public boolean exists(String code) {
        Query query = em.createQuery(
                "SELECT COUNT(c.code) FROM Client c WHERE c.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }
    public void create(String code, String name, String email, String password)
            throws CustomEntityExistsException, CustomConstraintViolationException {
        logger.info("Creating new client '" + code + "'");

        if (userBean.exists(code)) {
            throw new CustomEntityExistsException("User", code);
        }
        try {
            Client client = new Client(code, name, email, hasher.hash(password));
            em.persist(client);
            em.flush();
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public Client find(String code)
            throws CustomEntityNotFoundException {
        Client client = em.find(Client.class, code);
        if (client == null) {
            throw new CustomEntityNotFoundException("Client", code);
        }
        return client;
    }

    public Client findWithOrders(String code)
            throws CustomEntityNotFoundException {
        Client client = find(code);
        Hibernate.initialize(client.getOrders());
        return client;
    }

    // TODO: not used
    public void update(String code, String name, String email)
            throws CustomEntityNotFoundException, CustomConstraintViolationException {
        Client client = find(code);
        em.lock(client, LockModeType.OPTIMISTIC);
        logger.info("Updating client '" + code + "'");
        try{
            client.setEmail(email);
            client.setName(name);
            em.flush();
        }catch (ConstraintViolationException e){
            throw new CustomConstraintViolationException(e);
        }
    }


    public void delete(String code) throws CustomEntityNotFoundException {
        Client client = find(code);
        // Locks object that is being deleted
        em.lock(client, LockModeType.PESSIMISTIC_WRITE);
        logger.info("Deleting client '" + code + "'");
        em.remove(client);
    }
}
