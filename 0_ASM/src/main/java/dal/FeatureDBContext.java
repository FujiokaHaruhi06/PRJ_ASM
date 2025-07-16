package dal;

import Entity.Feature;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

public class FeatureDBContext extends DBContext {

    /**
     * Gets a list of all features a user has access to based on their roles.
     *
     * @param userId The ID of the user.
     * @return A list of Feature objects. Returns an empty list if none are
     * found.
     */
    public List<Feature> getFeaturesByAccountId(int aid) {
        EntityManager em = createEntityManager();
        try {
            String jpql = "SELECT DISTINCT rf.feature FROM Account_Role ar "
                    + "JOIN ar.role r "
                    + "JOIN r.roleFeatureList rf "
                    + "WHERE ar.account.aid = :aid "
                    + "AND rf.feature.isActive = true "
                    + "AND rf.feature.link <> 'register'";
            TypedQuery<Feature> query = em.createQuery(jpql, Feature.class);
            query.setParameter("aid", aid);
            return query.getResultList();
        } catch (Exception e) {
            // Log the exception in a real application
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list on error
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Feature> getAll() {
        EntityManager em = createEntityManager();
        try {
            String jpql = "SELECT f FROM Feature f";
            TypedQuery<Feature> query = em.createQuery(jpql, Feature.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
