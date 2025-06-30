package dal;

import Entity.User;
import Entity.Role;
import Entity.User_Role;
import Entity.User_RolePK;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDBContext extends DBContext {

    /**
     * Lấy User theo id.
     */
    public User getById(int uid) {
        EntityManager em = createEntityManager();
        try {
            return em.find(User.class, uid);
        } finally {
            em.close();
        }
    }

    public void insert(User user) {
        EntityManager em = createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            // Handle or log exception
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
} 