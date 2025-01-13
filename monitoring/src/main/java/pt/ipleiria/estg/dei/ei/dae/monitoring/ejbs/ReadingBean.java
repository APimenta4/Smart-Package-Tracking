package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.ReadingDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Reading;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.ReadingAcceleration;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.User;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.List;
import java.util.logging.Logger;

@Stateless
public class ReadingBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SensorBean sensorBean;

    @EJB
    private ReadingAccelerationBean readingAccelerationBean;

    @EJB
    private ReadingLocationBean readingLocationBean;

    @EJB
    private ReadingTemperatureBean readingTemperatureBean;

    private static final Logger logger = Logger.getLogger("ws.ReadingBean");


    public List<Reading> findAll() {
        return em.createNamedQuery("getAllReading", Reading.class).getResultList();
    }

    public Reading create(ReadingDTO readingDTO) throws CustomEntityNotFoundException, CustomConstraintViolationException {
        String sensorCode = readingDTO.getSensorCode();
        Sensor sensor = sensorBean.find(sensorCode);
        logger.info("Creating new Reading '" + sensor.getType() + "'");
        Reading reading = null;
        switch (sensor.getType()) {
            case ACCELERATION:
                reading = readingAccelerationBean.create(sensorCode, readingDTO.getAcceleration());
                break;
            case LOCATION:
                reading = readingLocationBean.create(sensorCode, readingDTO.getLatitude(), readingDTO.getLongitude());
                break;
            case TEMPERATURE:
                reading = readingTemperatureBean.create(sensorCode, readingDTO.getTemperature());
                break;
            default:
                throw new CustomConstraintViolationException("Invalid sensor type");
        }
        return reading;
    }
}
