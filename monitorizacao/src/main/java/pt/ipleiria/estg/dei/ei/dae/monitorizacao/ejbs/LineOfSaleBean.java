package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;

import jakarta.ejb.EJB;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.LineOfSale;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.CategoryType;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;

import java.util.logging.Logger;

public class LineOfSaleBean {

    //no create juntar product com volume
    @EJB
    private EntityManager entityManager;
    private static final Logger logger = Logger.getLogger("ejbs.LineOfSaleBean");

    public void create(Volume volume, Product product, long quantity) throws CustomEntityExistsException, CustomEntityNotFoundException, CustomConstraintViolationException {
        logger.info("Creating new LineOfSale , volume: " + volume.getCode() + " product: " + product.getCode());


        // Verifica se o volume NAO já existe
        if (entityManager.find(Volume.class, volume.getCode()) == null) {
            throw new CustomEntityNotFoundException("Volume", volume.getCode());
        }

        // Verifica se o produto NAO já existe
        if (entityManager.find(Product.class, product.getCode()) == null) {
            throw new CustomEntityNotFoundException("Product",product.getCode());
        }

        //volume e produto existem!
        try{

            LineOfSale lineOfSale = new LineOfSale(volume, product, quantity);
            
            entityManager.persist(lineOfSale);

            entityManager.flush(); // when using Hibernate, to force it to throw a ConstraintViolationException, as in the JPA specification

        } catch (
                ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }
}
