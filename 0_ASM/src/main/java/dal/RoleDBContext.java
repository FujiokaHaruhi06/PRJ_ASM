package dal;

import Entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class RoleDBContext extends DBContext {

    /**
     * Gets a list of all roles assigned to a specific user.
     * @param userId The ID of the user.
     * @return A list of Role objects.
     */
    public List<Role> getRolesByAccountId(int aid) {
        EntityManager em = createEntityManager();
        try {
            String jpql = "SELECT ar.role FROM Account_Role ar WHERE ar.account.aid = :aid";
            TypedQuery<Role> query = em.createQuery(jpql, Role.class);
            query.setParameter("aid", aid);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Role> getAll() {
        EntityManager em = createEntityManager();
        try {
            String sql = "SELECT r FROM Role r";
            TypedQuery<Role> query = em.createQuery(sql, Role.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Role get(int id) {
        EntityManager em = createEntityManager();
        try {
            return em.find(Role.class, id);
        } finally {
            em.close();
        }
    }
} 