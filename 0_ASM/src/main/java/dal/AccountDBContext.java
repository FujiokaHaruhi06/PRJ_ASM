package dal;

import Entity.Account;
import Entity.Role;
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
    public Set<Account> getSubordinates(Account manager) {
        EntityManager em = createEntityManager();
        try {
            // Tìm group mà manager là trưởng nhóm
            TypedQuery<Entity.Group> groupQuery = em.createQuery(
                "SELECT g FROM Group g WHERE g.manager.aid = :managerId", Entity.Group.class);
            groupQuery.setParameter("managerId", manager.getAid());
            List<Entity.Group> groups = groupQuery.getResultList();
            Set<Account> subordinates = new HashSet<>();
            for (Entity.Group group : groups) {
                TypedQuery<Account> accQuery = em.createQuery(
                    "SELECT a FROM Account a WHERE a.group.gid = :gid AND a.aid <> :managerId", Account.class);
                accQuery.setParameter("gid", group.getGid());
                accQuery.setParameter("managerId", manager.getAid());
                subordinates.addAll(accQuery.getResultList());
            }
            return subordinates;
        } finally {
            em.close();
        }
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
            // Tạo mới Account_Role
            EntityManager em2 = em;
            em2.createNativeQuery("INSERT INTO Account_Role (aid, rid) VALUES (?, ?)")
                .setParameter(1, account.getAid())
                .setParameter(2, role.getRid())
                .executeUpdate();
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