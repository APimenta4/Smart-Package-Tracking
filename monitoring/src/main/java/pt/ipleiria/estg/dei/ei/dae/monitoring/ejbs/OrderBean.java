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
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Volume;
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
            Order order = new Order(code, client);
            client.addOrder(order);
            em.persist(order);
            em.flush();
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public Order find(String code) throws CustomEntityNotFoundException {
        var order = em.find(Order.class, code);
        if (order == null) {
            throw new CustomEntityNotFoundException("Order",code);
        }
        return order;
    }

    public void initializeEntities(Order order) {
        List<Volume> volumes = order.getVolumes();
        Hibernate.initialize(volumes);
        volumes.forEach(volumeBean::initializeEntities);
    }

    public List<Order> findAllWithAllDetails() {
        List<Order> orders = em.createNamedQuery("getAllOrders", Order.class).getResultList();
        orders.forEach(this::initializeEntities);
        return orders;
    }

    public Order findWithReadings(String code) throws CustomEntityNotFoundException {
        Order order = find(code);
        List<Volume> volumes = order.getVolumes();
        Hibernate.initialize(volumes);
        for (Volume volume : volumes) {
            List<Sensor> sensors = volume.getSensors();
            Hibernate.initialize(sensors);
            for(Sensor sensor : sensors) {
                Hibernate.initialize(sensor.getReadings());
            }
        }
        return order;
    }
    public Order findWithAllDetails(String code)
            throws CustomEntityNotFoundException {
        Order order = find(code);
        initializeEntities(order);
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
