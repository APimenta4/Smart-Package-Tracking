package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.VolumeDTO;
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

    @EJB
    private LineOfSaleBean lineOfSaleBean;

    @EJB
    private SensorBean sensorBean;

    private static final Logger logger = Logger.getLogger("ejbs.OrderBean");

    public boolean exists(String code) {
        Query query = em.createQuery(
                "SELECT COUNT(o.code) FROM Order o WHERE o.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }

    @Transactional(rollbackOn = Exception.class)
    public void createWithDetails(OrderDTO orderDTO)
            throws CustomConstraintViolationException, CustomEntityNotFoundException, CustomEntityExistsException {
        String orderCode = orderDTO.getCode();
        create(orderCode ,orderDTO.getClientCode());

        for(VolumeDTO volumeDTO : orderDTO.getVolumes()) {
            String volumeCode = volumeDTO.getCode();
            volumeBean.create(volumeCode, orderCode, volumeDTO.getPackageType());

            for (ProductDTO productDTO : volumeDTO.getProducts()) {
                lineOfSaleBean.create(volumeCode, productDTO.getCode(), productDTO.getQuantity());
            }
            for (SensorDTO sensorDTO : volumeDTO.getSensors()) {
                sensorBean.create(sensorDTO.getCode(), volumeCode,sensorDTO.getType());
            }
        }

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

    private void initializeVolumes(Order order) {
        List<Volume> volumes = order.getVolumes();
        Hibernate.initialize(volumes);
        for(Volume volume : volumes) {
            Hibernate.initialize(volume.getLineOfSales());
            Hibernate.initialize(volume.getSensors());
        }
    }

    public Order findWithAllDetails(String code)
            throws CustomEntityNotFoundException {
        Order order = find(code);
        initializeVolumes(order);
        return order;
    }

    public List<Order> findAllWithAllDetails() {
        List<Order> orders = em.createNamedQuery("getAllOrders", Order.class).getResultList();
        for(Order order:orders){
            initializeVolumes(order);
        }
        return orders;
    }

    public List<Order> findAllWithAllDetails(String clientCode) throws CustomEntityNotFoundException {
        Client client = clientBean.find(clientCode);
        List<Order> orders = client.getOrders();
        for(Order order: orders) {
            initializeVolumes(order);
        }
        return orders;
    }

    public void delete(String code) throws CustomEntityNotFoundException {
        Order order = find(code);
        // Locks object that is being deleted
        em.lock(order, LockModeType.PESSIMISTIC_WRITE);
        logger.info("Deleting order '" + code + "'");
        em.remove(order);
    }
}
