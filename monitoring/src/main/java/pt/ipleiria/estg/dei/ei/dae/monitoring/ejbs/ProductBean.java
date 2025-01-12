package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.logging.Logger;

@Stateless
public class ProductBean{

    @PersistenceContext
    private  EntityManager em;

    private static final Logger logger = Logger.getLogger("ejbs.ProductBean");

    public boolean exists(String code) {
        Query query = em.createQuery(
                "SELECT COUNT(p.code) FROM Product p WHERE p.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(String code, PackageType category, String description)
            throws CustomEntityExistsException, CustomConstraintViolationException {
        logger.info("Creating new Product '" + code + "'");
        if (exists(code)) {
            throw new CustomEntityExistsException("Product",code);
        }
        try{
            Product product = new Product(code, category,description);
            em.persist(product);
        } catch (
                ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public Product find(String code)
            throws CustomEntityNotFoundException {
        Product product = em.find(Product.class, code);
        if (product == null) {
            throw new CustomEntityNotFoundException("Product", code);
        }
        return product;
    }
}
