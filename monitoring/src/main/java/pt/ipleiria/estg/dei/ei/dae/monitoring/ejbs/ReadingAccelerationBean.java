package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Client;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Manager;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.ReadingAcceleration;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.VolumeStatus;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.time.Instant;
import java.util.EnumSet;
import java.util.logging.Logger;

@Stateless
public class ReadingAccelerationBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SensorBean sensorBean;

    @EJB
    private EmailBean emailBean;
    @EJB
    private ManagerBean managerBean;

    private static final Logger logger = Logger.getLogger("ejbs.ReadingAccelerationBean");

    public boolean exists(Long id) {
        Query query = em.createQuery(
                "SELECT COUNT(ra.id) FROM ReadingAcceleration ra WHERE ra.id = :id",
                Long.class
        );
        query.setParameter("id", id);
        return (Long)query.getSingleResult() > 0L;
    }

    public ReadingAcceleration create(String sensorCode, Double acceleration)
            throws CustomConstraintViolationException, CustomEntityNotFoundException {
        logger.info("Creating new acceleration reading, sensor '" + sensorCode+"'");
        Sensor sensor = sensorBean.find(sensorCode);

        try {
            VolumeStatus status = sensor.getVolume().getStatus();
            if (EnumSet.of(VolumeStatus.CANCELLED, VolumeStatus.RETURNED, VolumeStatus.DELIVERED).contains(status)) {
                throw new CustomConstraintViolationException("Volume associated with the sensor '"+ sensorCode +"' has status "+status);
            }
            ReadingAcceleration readingAcceleration = new ReadingAcceleration(sensor, Instant.now(),acceleration);
            em.persist(readingAcceleration);


            if( acceleration >= 5 ){
                sendEmail(acceleration, sensorCode, sensor.getVolume().getCode(), status);
            }

            return readingAcceleration;
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    private void sendEmail(Double acceleration, String sensorCode, String volumeCode, VolumeStatus status) throws CustomEntityNotFoundException {
        Manager manager = managerBean.find("Head - manager");
        String subject = "Sensor detected high impact!";
        String body = "Hello, Head manager " + manager.getName() + ",\n\n"
                + "Here to inform that volume '" + volumeCode + "' while in status '" + status + "' suffered an impact with "
                + acceleration + "G. It was detected by the sensor with code '" + sensorCode + "' consider get a check on the volume to might return it.\n\n"
                + "Email generated automatically by the system";

        emailBean.send(manager.getEmail(), subject, body);
    }

    public ReadingAcceleration find(Long id)
            throws CustomEntityNotFoundException {
        ReadingAcceleration readingAcceleration = em.find(ReadingAcceleration.class, id);
        if (readingAcceleration == null) {
            throw new CustomEntityNotFoundException("ReadingAcceleration", Long.toString(id));
        }
        return readingAcceleration;
    }
}
