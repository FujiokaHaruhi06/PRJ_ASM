package dal;

import Entity.Group;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class GroupDBContext extends DBContext {
    public List<Group> getAll() {
        EntityManager em = createEntityManager();
        try {
            String sql = "SELECT g FROM Group g";
            TypedQuery<Group> query = em.createQuery(sql, Group.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Group get(int id) {
        EntityManager em = createEntityManager();
        try {
            return em.find(Group.class, id);
        } finally {
            em.close();
        }
    }
} 