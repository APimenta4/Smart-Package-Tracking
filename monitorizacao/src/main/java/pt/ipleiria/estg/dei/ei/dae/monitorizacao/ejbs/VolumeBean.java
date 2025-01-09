package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;

import jakarta.ejb.EJB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Client;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.PackingType;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.VolumeStatus;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityNotFoundException;

import java.util.List;
import java.util.logging.Logger;

public class VolumeBean {

    @EJB
    private OrderBean orderBean;
    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = Logger.getLogger("ejbs.VolumeBean");

    // CREATE - Criar e persistir um volume
    public void create(String code, Order order, VolumeStatus status, PackingType packingType) throws CustomEntityExistsException, CustomEntityNotFoundException, CustomConstraintViolationException {
        logger.info("Creating new Volume '" + code + "'");

        // Verifica se o volume já existe
        if (entityManager.find(Volume.class, code) != null) {
            throw new CustomEntityExistsException("Volume",code);
        }

        // Verificar se a order existe
        Order orderValid = orderBean.find(order.getCode());


        try{

            Volume volume = new Volume(code, orderValid, status,packingType);
            orderValid.addVolume(volume);

            entityManager.persist(volume);

            entityManager.flush(); // when using Hibernate, to force it to throw a ConstraintViolationException, as in the JPA specification

        } catch (
                ConstraintViolationException e) {
            throw new CustomConstraintViolationException(e);
        }
    }

    // READ - Buscar todas os Volumes
    public List<Volume> findAll() {
        return entityManager.createNamedQuery("getAllVolumes", Volume.class).getResultList();
    }

    // READ - Buscar um volume pelo código
    public Volume find(String code) throws CustomEntityNotFoundException {
        var volume = entityManager.find(Volume.class, code);
        if (volume == null) {
            throw new CustomEntityNotFoundException("Volume",code);
        }
        return volume;
    }

    // DELETE - Remover um volume
    public void delete(String code) throws CustomEntityNotFoundException {
        Volume volume = find(code); // Lança CustomEntityNotFoundException se não encontrado
        // Locks object that is being deleted
        entityManager.lock(volume, LockModeType.PESSIMISTIC_WRITE);
        entityManager.remove(volume);
    }


    // UPDATE - Atualizar um volume
    public Volume update(String code, VolumeStatus status) throws CustomEntityNotFoundException {
        Volume volume = find(code); // Lança CustomEntityNotFoundException se não encontrado
        entityManager.lock(volume, LockModeType.OPTIMISTIC);
        volume.setStatus(status);
        return entityManager.merge(volume); // Realiza o merge e retorna o volume atualizado
    }
}
