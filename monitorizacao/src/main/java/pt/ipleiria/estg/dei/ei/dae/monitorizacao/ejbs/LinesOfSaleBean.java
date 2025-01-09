package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.LinesOfSale;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;

import java.util.logging.Logger;

@Stateless
public class LinesOfSaleBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private VolumeBean volumeBean;

    @EJB
    private ProductBean productBean;

    private static final Logger logger = Logger.getLogger("ejbs.LineOfSaleBean");

    public void create(String volumeCode, String productCode, long quantity)
            throws CustomEntityNotFoundException, CustomConstraintViolationException {
        logger.info("Creating new LineOfSale, volume '" + volumeCode + "', product '" + productCode + "'");

        Volume volume = volumeBean.find(volumeCode);
        Product product = productBean.find(productCode);
        try{
            LinesOfSale lineOfSale = new LinesOfSale(volume, product, quantity);
            em.persist(lineOfSale);
            em.flush();
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }
}
