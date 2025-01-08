package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.SensorType;

import java.util.List;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager entityManager;


    public boolean exists(long code) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(s.code) FROM Sensor s WHERE s.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }


    public void create(long code, long volumeCode, SensorType type){

//        Volume volume = volumeBean.find(volumeCode);
        Volume volume = new Volume();
        if (exists(code)){
            return;
            //throw new MyEntityExistsException("Course '" +String.valueOf(code)+ "'");
        }
        Sensor sensor = new Sensor(code, new Volume(), type);
        entityManager.persist(sensor);
    }

    public Sensor find(long code) {
        Sensor sensor = entityManager.find(Sensor.class, code);
//        if (sensor == null) {
//            throw new MyEntityNotFoundException("Course '" + code + "'");
//        }
        return sensor;
    }

    public List<Sensor> findAll() {
        // remember, maps to: “SELECT c FROM Course c ORDER BY c.name”
        return entityManager.createNamedQuery("getAllSensors", Sensor.class).getResultList();
    }
}