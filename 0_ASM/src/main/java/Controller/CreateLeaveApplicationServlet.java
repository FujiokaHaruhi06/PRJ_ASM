package Controller;

import Entity.LA_status;
import Entity.Leave_Application;
import Entity.User;
import Entity.Account;
import dal.LeaveApplicationDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpSession;
import dal.RoleDBContext;
import Entity.Role;

@WebServlet(name = "CreateLeaveApplicationServlet", urlPatterns = {"/create_leave_application"})
public class CreateLeaveApplicationServlet extends HttpServlet {

    private LeaveApplicationDBContext laDB;
    private RoleDBContext roleDBContext;

    @Override
    public void init() throws ServletException {
        laDB = new LeaveApplicationDBContext();
        roleDBContext = new RoleDBContext();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("roles") == null) {
            Account account = (Account) session.getAttribute("account");
            if (account != null) {
                List<Role> roles = roleDBContext.getRolesByAccountId(account.getAid());
                session.setAttribute("roles", roles);
            }
        }
        request.getRequestDispatcher("view/create_leave_application.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Account account = (Account) request.getSession().getAttribute("account");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String reason = request.getParameter("reason");

        Map<String, String> errors = new HashMap<>();
        Map<String, String> submittedValues = new HashMap<>();
        submittedValues.put("startDate", startDateStr);
        submittedValues.put("endDate", endDateStr);
        submittedValues.put("reason", reason);
        
        LocalDate today = LocalDate.now();
        
        // 1. Validate Start Date
        Date startDate = null;
        if (startDateStr == null || startDateStr.isEmpty()) {
            errors.put("startDate", "Vui lòng chọn ngày bắt đầu.");
        } else {
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
                if (startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(today)) {
                    errors.put("startDate", "Ngày bắt đầu không được là ngày trong quá khứ.");
                }
            } catch (ParseException e) {
                errors.put("startDate", "Định dạng ngày bắt đầu không hợp lệ.");
            }
        }
        
        // 2. Validate End Date
        Date endDate = null;
        if (endDateStr == null || endDateStr.isEmpty()) {
            errors.put("endDate", "Vui lòng chọn ngày kết thúc.");
        } else {
            try {
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
                 if (startDate != null && endDate.before(startDate)) {
                    errors.put("endDate", "Ngày kết thúc không được trước ngày bắt đầu.");
                }
            } catch (ParseException e) {
                errors.put("endDate", "Định dạng ngày kết thúc không hợp lệ.");
            }
        }

        // 3. Validate Reason
        if (reason == null || reason.trim().isEmpty()) {
            errors.put("reason", "Vui lòng nhập lý do.");
        } else if (!reason.matches(".*[a-zA-Z]+.*")) {
            errors.put("reason", "Lý do phải chứa ít nhất một ký tự chữ cái.");
        }

        // If there are errors, forward back to the form
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("submittedValues", submittedValues);
            request.getRequestDispatcher("view/create_leave_application.jsp").forward(request, response);
            return;
        }

        // If all validation passes, proceed to create the application
        try {
            Leave_Application app = new Leave_Application();
            app.setAccount(account);
            app.setStartDate(startDate);
            app.setEndDate(endDate);
            app.setReason(reason.trim());
            app.setStatus(laDB.getStatusById(1)); // 1: Pending

            laDB.createApplication(app);
            
            request.setAttribute("successMessage", "Đã gửi đơn xin nghỉ thành công!");
            request.getRequestDispatcher("view/create_leave_application.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
            if (msg != null && msg.contains("không tìm thấy người quản lý hợp lệ")) {
                errors.put("general", "Không thể gửi đơn: Không tìm thấy người quản lý hợp lệ để duyệt đơn. Vui lòng kiểm tra lại thông tin nhóm/phòng hoặc liên hệ quản trị viên.");
            } else {
                errors.put("general", "Đã xảy ra lỗi hệ thống khi tạo đơn: " + e.getMessage());
            }
            request.setAttribute("errors", errors);
            request.setAttribute("submittedValues", submittedValues);
            request.getRequestDispatcher("view/create_leave_application.jsp").forward(request, response);
        }
    }
} 