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

        // 2. Nếu là head của division nào đó, lấy tất cả đơn của các account thuộc các group trong division đó
        dal.DivisionDBContext divisionDB = new dal.DivisionDBContext();
        List<Entity.Division> divisions = divisionDB.getAll();
        boolean isDivisionHead = false;
        for (Entity.Division div : divisions) {
            if (div.getHead() != null && div.getHead().getAid() == account.getAid()) {
                isDivisionHead = true;
                dal.GroupDBContext groupDB = new dal.GroupDBContext();
                List<Entity.Group> groups = groupDB.getByDivisionId(div.getDivid());
                for (Entity.Group g : groups) {
                    dal.AccountDBContext accDB = new dal.AccountDBContext();
                    Set<Account> groupMembers = new java.util.HashSet<>(accDB.getSubordinates(g.getManager()));
                    // Thêm cả trưởng nhóm
                    if (g.getManager() != null) groupMembers.add(g.getManager());
                    for (Account acc : groupMembers) {
                        applications.addAll(laDB.getApplicationsByAccountId(acc.getAid()));
                    }
                }
            }
        }
        if (!isDivisionHead) {
            // Nếu không phải trưởng phòng, giữ logic cũ (có thể lấy thêm đơn nếu là group leader...)
            dal.RoleDBContext roleDB = new dal.RoleDBContext();
            List<Entity.Role> roles = roleDB.getRolesByAccountId(account.getAid());
            for (Entity.Role role : roles) {
                if (account.getGroup() != null) {
                    if ("Group Leader".equalsIgnoreCase(role.getRname())) {
                        applications.addAll(laDB.getApplicationsByGroupId(account.getGroup().getGid()));
                    }
                    if ("Division Leader".equalsIgnoreCase(role.getRname()) && account.getGroup().getDivision() != null) {
                        applications.addAll(laDB.getApplicationsByDivisionId(account.getGroup().getDivision().getDivid()));
                    }
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