package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Reading;

import java.util.List;

@Stateless
public class ReadingBean {
    @PersistenceContext
    private EntityManager em;

    public List<Reading> findAll() {
        return em.createNamedQuery("getAllReading", Reading.class).getResultList();
    }
}
