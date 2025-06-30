package Controller;

import Entity.User;
import Entity.Account;
import dal.LeaveApplicationDBContext;
import dal.UserDBContext;
import dal.AccountDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "WorkScheduleServlet", urlPatterns = {"/work_schedule"})
public class WorkScheduleServlet extends HttpServlet {

    private LeaveApplicationDBContext leaveDBContext;
    private UserDBContext userDBContext;
    private AccountDBContext accountDBContext;

    @Override
    public void init() throws ServletException {
        leaveDBContext = new LeaveApplicationDBContext();
        userDBContext = new UserDBContext();
        accountDBContext = new AccountDBContext();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Account account = (Account) session.getAttribute("account");
        
        // Dùng Map để lưu Account và trạng thái làm việc (boolean: true = on leave)
        // LinkedHashMap giữ nguyên thứ tự chèn
        Map<Account, Boolean> workStatusMap = new LinkedHashMap<>();
        Date today = new Date();

        // 1. Luôn thêm chính account hiện tại vào danh sách
        workStatusMap.put(account, leaveDBContext.isAccountOnLeaveOnDate(account.getAid(), today));
        
        // 2. Lấy danh sách tất cả account cấp dưới và kiểm tra trạng thái của họ
        Set<Account> subordinates = accountDBContext.getSubordinates(account);
        for (Account subordinate : subordinates) {
            workStatusMap.put(subordinate, leaveDBContext.isAccountOnLeaveOnDate(subordinate.getAid(), today));
        }
        
        request.setAttribute("workStatusMap", workStatusMap);
        request.setAttribute("checkDate", today);
        request.getRequestDispatcher("view/work_schedule.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles the work schedule checking logic for user and their subordinates.";
    }
} 