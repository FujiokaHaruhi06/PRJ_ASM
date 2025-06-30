package dal;

import Entity.Leave_Application;
import Entity.User;
import Entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

public class LeaveApplicationDBContext extends DBContext {
    
    /**
     * Checks if an account is on an approved leave for a specific date.
     * @param aid The ID of the account to check.
     * @param date The date to check against.
     * @return true if the account is on leave, false otherwise.
     */
    public boolean isAccountOnLeaveOnDate(int aid, Date date) {
        EntityManager em = createEntityManager();
        final int APPROVED_STATUS_ID = 2;
        try {
            String jpql = "SELECT COUNT(la) FROM Leave_Application la "
                        + "WHERE la.account.aid = :aid "
                        + "AND la.status.sid = :statusId "
                        + "AND :checkDate BETWEEN la.startDate AND la.endDate";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("aid", aid);
            query.setParameter("statusId", APPROVED_STATUS_ID);
            query.setParameter("checkDate", date, TemporalType.DATE);
            Long count = query.getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    /**
     * Lấy tất cả đơn của các thành viên trong một nhóm (dành cho Group Leader).
     */
    public List<Leave_Application> getApplicationsByGroupId(int groupId) {
        EntityManager em = createEntityManager();
        try {
            TypedQuery<Leave_Application> query = em.createQuery(
                "SELECT la FROM Leave_Application la WHERE la.account.user.group.gid = :groupId ORDER BY la.createTime DESC", 
                Leave_Application.class);
            query.setParameter("groupId", groupId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy tất cả đơn của các Group Leader thuộc một Division (dành cho Division Leader).
     */
    public List<Leave_Application> getApplicationsByDivisionId(int divisionId) {
        EntityManager em = createEntityManager();
        try {
            TypedQuery<Leave_Application> query = em.createQuery(
                "SELECT la FROM Leave_Application la " +
                "WHERE la.account.user.group.division.divid = :divisionId " +
                "AND la.account.user.uid IN (SELECT g.mgrid FROM Group g WHERE g.divid = :divisionId)",
                Leave_Application.class);
            query.setParameter("divisionId", divisionId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy các đơn đang chờ duyệt mà người dùng có quyền.
     * Logic này sẽ thay thế getPendingApplicationsForApprover cũ.
     */
    public List<Leave_Application> getReviewableApplications(Account approverAccount) {
        EntityManager em = createEntityManager();
        try {
            final int PENDING_STATUS_ID = 1;
            Set<Leave_Application> reviewables = new HashSet<>();
            // 1. Lấy đơn được gán trực tiếp cho mình
            TypedQuery<Leave_Application> directQuery = em.createQuery(
                "SELECT la FROM Leave_Application la WHERE la.approverAccount.aid = :approverAid AND la.status.sid = :statusId",
                Leave_Application.class);
            directQuery.setParameter("approverAid", approverAccount.getAid());
            directQuery.setParameter("statusId", PENDING_STATUS_ID);
            reviewables.addAll(directQuery.getResultList());
            // 2. Nếu là Group Leader, lấy đơn của member trong nhóm mình quản lý
            if (isGroupLeader(approverAccount)) {
                TypedQuery<Leave_Application> groupQuery = em.createQuery(
                    "SELECT la FROM Leave_Application la WHERE la.account.group.gid = :gid AND la.status.sid = :statusId AND la.account.aid != :approverAid",
                    Leave_Application.class);
                groupQuery.setParameter("gid", approverAccount.getGroup().getGid());
                groupQuery.setParameter("statusId", PENDING_STATUS_ID);
                groupQuery.setParameter("approverAid", approverAccount.getAid());
                reviewables.addAll(groupQuery.getResultList());
            }
            // 3. Nếu là Division Leader, lấy đơn của các Group Leader mình quản lý
            if (isDivisionLeader(approverAccount)) {
                 TypedQuery<Leave_Application> divisionQuery = em.createQuery(
                    "SELECT la FROM Leave_Application la " +
                    "WHERE la.account.group.division.divid = :divid AND la.status.sid = :statusId " +
                    "AND la.account.aid IN (SELECT a.aid FROM Account a WHERE a.group.division.divid = :divid AND a.group.mgrid = a.aid)",
                    Leave_Application.class);
                divisionQuery.setParameter("divid", approverAccount.getGroup().getDivision().getDivid());
                divisionQuery.setParameter("statusId", PENDING_STATUS_ID);
                reviewables.addAll(divisionQuery.getResultList());
            }
            return new ArrayList<>(reviewables);
        } finally {
            em.close();
        }
    }
    
    // Helper methods to check roles based on structure for data retrieval
    private boolean isGroupLeader(Account account) {
        return account.getGroup() != null && account.getGroup().getManager() != null && account.equals(account.getGroup().getManager());
    }

    private boolean isDivisionLeader(Account account) {
        return account.getGroup() != null && account.getGroup().getDivision() != null && account.getGroup().getDivision().getHead() != null && account.equals(account.getGroup().getDivision().getHead());
    }
    
    /**
     * Cập nhật trạng thái của một đơn.
     */
    public void updateApplicationStatus(int applicationId, int newStatusId, int approverAid) {
        EntityManager em = createEntityManager();
        try {
            em.getTransaction().begin();
            Leave_Application app = em.find(Leave_Application.class, applicationId);
            if (app != null) {
                app.getStatus().setSid(newStatusId);
                app.setApproverAccount(em.find(Entity.Account.class, approverAid));
                app.setApprovalTime(new Date());
                em.merge(app);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Logic leo thang mới, mạnh mẽ hơn để tìm người duyệt đơn.
     */
    private Account findNextApprover(Account applicant, EntityManager em) {
        Account currentManager = getImmediateManager(applicant, em);
        while (currentManager != null) {
            // Người duyệt không thể là chính người nộp đơn
            if (currentManager.equals(applicant)) {
                currentManager = getImmediateManager(currentManager, em);
                continue;
            }
            // Kiểm tra xem người quản lý có đang nghỉ không
            if (!isAccountOnLeaveOnDate(currentManager.getAid(), new Date())) {
                return currentManager; // Tìm thấy người duyệt hợp lệ
            }
            // Nếu người quản lý hiện tại nghỉ, tìm quản lý của họ
            currentManager = getImmediateManager(currentManager, em);
        }
        // Fallback: Nếu không tìm thấy ai trong chuỗi phân cấp
        return null; // Servlet sẽ phải xử lý trường hợp này
    }

    /**
     * Tạo một đơn mới.
     * Logic tìm người duyệt sẽ được xử lý ở đây.
     */
    public void createApplication(Leave_Application app) {
        EntityManager em = createEntityManager();
        try {
            em.getTransaction().begin();
            // ... LOGIC TỰ ĐỘNG DUYỆT ĐƠN ...
            String[] autoApproveKeywords = {"ốm", "bệnh", "lấy vợ", "kết hôn", "đám tang", "tang lễ"};
            String reasonLowerCase = app.getReason().toLowerCase();
            boolean autoApproved = false;
            for (String keyword : autoApproveKeywords) {
                if (reasonLowerCase.contains(keyword)) {
                    autoApproved = true;
                    break;
                }
            }
            if (autoApproved) {
                app.getStatus().setSid(2); // 2: Approved
                app.setApproverAccount(app.getAccount()); // Tự duyệt để ghi nhận
                app.setApprovalTime(new Date());
            } else {
                // ... LOGIC TÌM NGƯỜI DUYỆT (ESCALATION) ...
                Account applicant = em.find(Account.class, app.getAccount().getAid());
                Account approver = findNextApprover(applicant, em);
                if (approver == null) {
                    throw new IllegalStateException("Không tìm thấy người quản lý hợp lệ để duyệt đơn.");
                }
                app.setApproverAccount(approver);
                app.getStatus().setSid(1); // 1: Pending
            }
            app.setCreateTime(new Date());
            em.persist(app);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Tìm người quản lý trực tiếp của một nhân viên.
     */
    private Account getImmediateManager(Account account, EntityManager em) {
        // Một người có thể vừa là member của group này, vừa là leader của group khác
        // Quy tắc ở đây là tìm người quản lý của vai trò cao nhất mà họ đang giữ
        
        // Nếu là Division Leader -> không có manager
        if (isDivisionLeader(account)) {
            return null;
        }

        // Nếu là Group Leader -> manager là Division Leader
        if (isGroupLeader(account)) {
             if (account.getGroup().getDivision() != null && account.getGroup().getDivision().getHead() != null) {
                return em.find(Account.class, account.getGroup().getDivision().getHead().getAid());
            }
        }
        
        // Nếu là Member -> manager là Group Leader
        if (account.getGroup() != null && account.getGroup().getManager() != null) {
            return em.find(Account.class, account.getGroup().getManager().getAid());
        }

        return null; // Không có manager
    }

    public List<Leave_Application> getApplicationsByAccountId(int aid) {
        EntityManager em = createEntityManager();
        try {
            TypedQuery<Leave_Application> query = em.createQuery(
                "SELECT la FROM Leave_Application la WHERE la.account.aid = :aid ORDER BY la.createTime DESC",
                Leave_Application.class);
            query.setParameter("aid", aid);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
} 