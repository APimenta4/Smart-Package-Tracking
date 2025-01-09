package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;

import jakarta.ejb.EJB;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.CategoryType;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.PackingType;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.VolumeStatus;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;

import java.util.List;
import java.util.logging.Logger;

public class ProductBean {

    @EJB
    private  EntityManager entityManager;

    private static final Logger logger = Logger.getLogger("ejbs.ProductBean");

    public void create(String code, CategoryType category, String description) throws CustomEntityExistsException, CustomEntityNotFoundException, CustomConstraintViolationException {
        logger.info("Creating new Product '" + code + "'");

        // Verifica se o produto já existe
        if (entityManager.find(Product.class, code) != null) {
            throw new CustomEntityExistsException("Product",code);
        }


        try{

            Product product = new Product(code, category,description);

            entityManager.persist(product);

            entityManager.flush(); // when using Hibernate, to force it to throw a ConstraintViolationException, as in the JPA specification

        } catch (
                ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    // READ - Buscar todos os Produtos
    public List<Product> findAll() {
        return entityManager.createNamedQuery("getAllProducts", Product.class).getResultList();
    }
}
