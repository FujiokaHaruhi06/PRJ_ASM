package Controller;

import Entity.Leave_Application;
import Entity.Role;
import Entity.Account;
import dal.LeaveApplicationDBContext;
import dal.RoleDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "ApplicationStatusServlet", urlPatterns = {"/application_status"})
public class ApplicationStatusServlet extends HttpServlet {
    
    private LeaveApplicationDBContext laDB;
    private RoleDBContext roleDB;

    @Override
    public void init() throws ServletException {
        laDB = new LeaveApplicationDBContext();
        roleDB = new RoleDBContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        
        Set<Leave_Application> applications = new HashSet<>();
        // 1. Luôn lấy các đơn của chính mình
        applications.addAll(laDB.getApplicationsByAccountId(account.getAid()));
        
        // 2. Kiểm tra các vai trò và lấy thêm đơn nếu có quyền
        List<Role> roles = roleDB.getRolesByAccountId(account.getAid());
        for (Role role : roles) {
            // Chỉ thực hiện nếu account có một group được gán
            if (account.getGroup() != null) {
                if ("Group Leader".equalsIgnoreCase(role.getRname())) {
                    // Lấy đơn của các thành viên trong nhóm mình quản lý
                    applications.addAll(laDB.getApplicationsByGroupId(account.getGroup().getGid()));
                }
                // Chỉ thực hiện nếu group của account có một division được gán
                if ("Division Leader".equalsIgnoreCase(role.getRname()) && account.getGroup().getDivision() != null) {
                    // Lấy đơn của các trưởng nhóm trong phòng ban mình quản lý
                    applications.addAll(laDB.getApplicationsByDivisionId(account.getGroup().getDivision().getDivid()));
                }
            }
        }
        
        // Sắp xếp lại danh sách theo ngày tạo mới nhất
        List<Leave_Application> sortedApplications = applications.stream()
                .sorted((a1, a2) -> a2.getCreateTime().compareTo(a1.getCreateTime()))
                .collect(Collectors.toList());

        request.setAttribute("applications", sortedApplications);
        request.getRequestDispatcher("view/application_status.jsp").forward(request, response);
    }
} 