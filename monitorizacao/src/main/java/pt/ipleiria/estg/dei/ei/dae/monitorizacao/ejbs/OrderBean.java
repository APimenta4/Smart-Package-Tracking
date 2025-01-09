package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Client;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;

import java.util.List;

@Stateless
public class OrderBean {

    @EJB
    private ClientBean clientBean;
    @PersistenceContext
    private EntityManager entityManager;

    // CREATE - Criar e persistir uma nova order
    public void create(String code, Client client) throws CustomEntityExistsException, CustomEntityNotFoundException, CustomConstraintViolationException {
        // Verifica se o order já existe
        if (entityManager.find(Order.class, code) != null) {
            throw new CustomEntityExistsException("Order",code);
        }

        // Verificar se o cliente existe
        Client clientValid = clientBean.find(client.getCode()); // ja protegido


        try{

        Order order = new Order(code, clientValid);
        clientValid.addOrder(order);

        entityManager.persist(order);

        entityManager.flush(); // when using Hibernate, to force it to throw a ConstraintViolationException, as in the JPA specification

        } catch (
        ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    // READ - Buscar todas as Orders
    public List<Order> findAll() {
        return entityManager.createNamedQuery("getAllOrders", Order.class).getResultList();
    }

    // READ - Buscar uma order pelo código
    public Order find(String code) throws CustomEntityNotFoundException {
        var order = entityManager.find(Order.class, code);
        if (order == null) {
            throw new CustomEntityNotFoundException("Order",code);
        }
        return order;
    }

    // DELETE - Remover uma Order
    public void delete(String code) throws CustomEntityNotFoundException {
        Order order = find(code); //codigo ja protegido
        entityManager.remove(order);
    }
}
