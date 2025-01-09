package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Client;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.List;
import java.util.logging.Logger;

@Stateless
public class OrderBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private ClientBean clientBean;

    @EJB
    private VolumeBean volumeBean;

    private static final Logger logger = Logger.getLogger("ejbs.OrderBean");

    public boolean exists(String code) {
        Query query = em.createQuery(
                "SELECT COUNT(o.code) FROM Order o WHERE o.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(String code, String clientCode)
            throws CustomEntityExistsException, CustomEntityNotFoundException, CustomConstraintViolationException {
        logger.info("Creating new order '" + code + "'");
        if (exists(code)) {
            throw new CustomEntityExistsException("Order",code);
        }
        Client client = clientBean.find(clientCode);
        try{
            // TODO: verify association in case of invalid volume
            Order order = new Order(code, client);
            client.addOrder(order);
            em.persist(order);
            em.flush();
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public List<Order> findAll() {
        return em.createNamedQuery("getAllOrders", Order.class).getResultList();
    }

    public Order find(String code) throws CustomEntityNotFoundException {
        var order = em.find(Order.class, code);
        if (order == null) {
            throw new CustomEntityNotFoundException("Order",code);
        }
        return order;
    }

    public Order findWithVolumes(String code)
            throws CustomEntityNotFoundException {
        Order order = find(code);
        Hibernate.initialize(order.getVolumes());
        return order;
    }

    public void delete(String code) throws CustomEntityNotFoundException {
        Order order = find(code);
        // Locks object that is being deleted
        em.lock(order, LockModeType.PESSIMISTIC_WRITE);
        logger.info("Deleting order '" + code + "'");
        em.remove(order);
    }
}
