package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.User;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.security.Hasher;

import java.util.logging.Logger;

@Stateless
public class UserBean {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private Hasher hasher;

    private static final Logger logger = Logger.getLogger("ejbs.UserBean");

    /**
     * Asserts that a user with the given code exists in the database.
     * This method checks if a user exists by counting the number of users
     * with the specified code. If no users are found, it throws a
     * {@link CustomEntityExistsException}.
     *
     * @param code The unique identifier of the user to check.
     * @throws CustomEntityExistsException if no user with the specified code exists.
     */
    public void assertExists(long code) throws CustomEntityExistsException {
        Query query = em.createQuery(
                "SELECT COUNT(u.code) FROM User u WHERE u.code = :code",
                Long.class
        );
        query.setParameter("code", code);
        if ((Long)query.getSingleResult() <= 0L){
            throw new CustomEntityExistsException("User '" +code+ "'");
        }
    }

    public User findOrFail(long code) {
        User user = em.getReference(User.class, code);
        Hibernate.initialize(user);
        return user;
    }
    public boolean canLogin(long code, String password) {
        User user = em.find(User.class, code);
        return user != null && user.getPassword().equals(hasher.hash(password));
    }

    public void updatePassword(long code, String newPassword) {
        User user = em.find(User.class, code);
        em.lock(user, LockModeType.OPTIMISTIC);
        logger.info("Updating password for user '" + code + "'");
        user.setPassword(hasher.hash(newPassword));
        em.merge(user);
    }
}

