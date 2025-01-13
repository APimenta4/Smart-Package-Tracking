package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.User;
import pt.ipleiria.estg.dei.ei.dae.monitoring.security.Hasher;

import java.util.logging.Logger;

@Stateless
public class UserBean {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private Hasher hasher;

    private static final Logger logger = Logger.getLogger("ejbs.UserBean");

    /**
     * Checks whether a user with the given code exists in the database.
     * This method determines if a user exists by counting the number of users
     * with the specified code. It returns a boolean indicating the result.
     *
     * @param code The unique identifier of the user to check.
     * @return {@code true} if a user with the specified code exists, {@code false} otherwise.
     */
    public boolean exists(String code){
        Query query = em.createQuery(
                "SELECT COUNT(u.code) FROM User u WHERE u.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        return (Long)query.getSingleResult() > 0L;
    }

    public User findOrFail(String code) {
        User user = em.getReference(User.class, code);
        Hibernate.initialize(user);
        return user;
    }

    public boolean canLogin(String code, String password) {
        User user = em.find(User.class, code);
        return user != null && user.getPassword().equals(hasher.hash(password));
    }

    public void updatePassword(String code, String newPassword) {
        User user = em.find(User.class, code);
        em.lock(user, LockModeType.OPTIMISTIC);
        logger.info("Updating password for user '" + code + "'");
        user.setPassword(hasher.hash(newPassword));
        em.merge(user);
    }
}

