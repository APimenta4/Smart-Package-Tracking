package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Client;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.security.Hasher;

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

    public void create(long code, String name, String email, String password)
            throws CustomEntityExistsException, CustomConstraintViolationException {
        logger.info("Creating new client '" + code + "'");

        // TODO: this assertExists isn't doing nothing it throws even if is commented
        userBean.assertExists(code);
        try {
            Client client = new Client(code, name, email, hasher.hash(password));
            em.persist(client);

        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public Client find(long code)
            throws CustomEntityNotFoundException {
        Client client = em.find(Client.class, code);
        if (client == null) {
            throw new CustomEntityNotFoundException("Client '" +code+ "'");
        }
        return client;
    }

    public Client findWithOrders(long code)
            throws CustomEntityNotFoundException {
        Client client = find(code);
        Hibernate.initialize(client.getOrders());
        return client;
    }

    public void update(long code, String email, String name)
            throws CustomEntityNotFoundException, IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name '"+name+"' cannot be null or blank.");
        }
        Client client = find(code);
        em.lock(client, LockModeType.OPTIMISTIC);
        logger.info("Updating client '" + code + "'");
        client.setEmail(email);
        client.setName(name);
    }


    public void delete(long code) throws CustomEntityNotFoundException {
        Client client = find(code);
        // Locks object that is being deleted
        em.lock(client, LockModeType.PESSIMISTIC_WRITE);
        logger.info("Deleting client '" + code + "'");
        em.remove(client);
    }
}
