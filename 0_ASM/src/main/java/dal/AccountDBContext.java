package dal;

import Entity.Account;
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

public class AccountDBContext extends DBContext {

    /**
     * Finds an account by its username.
     * @param username The username to search for.
     * @return The Account object if found, otherwise null.
     */
    public Account getByUsername(String username) {
        EntityManager em = createEntityManager();
        try {
            String jpql = "SELECT a FROM Account a WHERE a.username = :username";
            TypedQuery<Account> query = em.createQuery(jpql, Account.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Lấy danh sách tất cả cấp dưới mà một account có quyền xem.
     * (Tùy vào logic RBAC, có thể cần join sang User nếu cần thông tin cá nhân)
     * @param account The manager Account object.
     * @return A Set of subordinate Account objects.
     */
    public Set<Account> getSubordinates(Account account) {
        // Placeholder: cần xác định lại logic phân cấp theo Account
        // Có thể cần join sang User nếu cần lấy thông tin cá nhân
        return new HashSet<>();
    }

    public void insert(Account account) {
        EntityManager em = createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(account);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void addRoleForAccount(Account account, Role role) {
        EntityManager em = createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            User_RolePK pk = new User_RolePK();
            pk.setAid(account.getAid());
            pk.setRid(role.getRid());
            User_Role userRole = new User_Role();
            userRole.setUserRolePK(pk);
            userRole.setAccount(em.merge(account));
            userRole.setRole(em.merge(role));
            em.persist(userRole);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Account getById(int aid) {
        EntityManager em = createEntityManager();
        try {
            return em.find(Account.class, aid);
        } finally {
            em.close();
        }
    }
} 