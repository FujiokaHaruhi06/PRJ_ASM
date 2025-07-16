package dal;

import Entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;


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

    public User getByEmail(String email) {
        EntityManager em = createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Không tìm thấy user nào
        } finally {
            em.close();
        }
    }
} 