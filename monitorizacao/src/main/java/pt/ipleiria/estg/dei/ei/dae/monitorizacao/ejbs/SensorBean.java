package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.SensorType;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;

import java.util.logging.Logger;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private VolumeBean volumeBean;

    private static final Logger logger = Logger.getLogger("ejbs.SensorBean");

    public boolean exists(String code) {
        Query query = em.createQuery(
                "SELECT COUNT(s.code) FROM Sensor s WHERE s.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }


    public void create(String code, String volumeCode, SensorType type)
            throws CustomEntityExistsException, CustomEntityNotFoundException, CustomConstraintViolationException {
        logger.info("Creating new sensor '" + code + "'");
        if (exists(code)) {
            throw new CustomEntityExistsException("Sensor", code);
        }

        Volume volume = volumeBean.find(volumeCode);

        try {
            Sensor sensor = new Sensor(code, volume, type);
            em.persist(sensor);

        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    public Sensor find(String code)
            throws CustomEntityNotFoundException {
        Sensor sensor = em.find(Sensor.class, code);
        if (sensor == null) {
            throw new CustomEntityNotFoundException("Sensor", code);
        }
        return sensor;
    }
}