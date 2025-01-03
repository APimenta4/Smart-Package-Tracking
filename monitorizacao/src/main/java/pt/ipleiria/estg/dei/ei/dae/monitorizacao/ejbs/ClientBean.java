package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Client;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.security.Hasher;

import java.util.List;

@Stateless
public class ClientBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public void create(long code, String name, String email, String password) {
        Client client = new Client(code, name, email, hasher.hash(password));
        em.persist(client);
    }

    public Client find(String username) {
        Client client = em.find(Client.class, username);
        if (client == null) {
            throw new RuntimeException("client " + username + " not found");
        }
        return client;
    }

    public List<Client> findAll() {
        return em.createNamedQuery("getAllClients", Client.class).getResultList();
    }

    public Client findWithSubjects(String username){
        Client client = this.find(username);
        Hibernate.initialize(client.getOrders());
        return client;
    }

    public void update(String username, String email, String name, String password) {
        Client client = find(username);
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }

        // Update user
        client.setEmail(email);
        client.setName(name);
        // TODO: Remove password from here
        client.setPassword(hasher.hash(password));

        em.merge(client);
    }

    public void delete(String username) {
        Client client = find(username);
        em.remove(client);
    }

}
