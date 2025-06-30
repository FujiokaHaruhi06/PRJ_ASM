package Controller;

import Entity.Leave_Application;
import Entity.Account;
import dal.LeaveApplicationDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ApplicationReviewServlet", urlPatterns = {"/application_review"})
public class ApplicationReviewServlet extends HttpServlet {

    private LeaveApplicationDBContext laDB;
    private final int APPROVED_STATUS_ID = 2;
    private final int REJECTED_STATUS_ID = 3;

    @Override
    public void init() throws ServletException {
        laDB = new LeaveApplicationDBContext();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("account");
        request.setAttribute("reviewableApplications", laDB.getReviewableApplications(account));
        request.getRequestDispatcher("view/application_review.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        try {
            int appId = Integer.parseInt(request.getParameter("appId"));
            String action = request.getParameter("action");
            List<Leave_Application> userCanReview = laDB.getReviewableApplications(account);
            boolean hasPermission = userCanReview.stream().anyMatch(app -> app.getLid() == appId);
            if (!hasPermission) {
                throw new SecurityException("User does not have permission to review this application.");
            }
            if ("approve".equals(action)) {
                laDB.updateApplicationStatus(appId, APPROVED_STATUS_ID, account.getAid());
                request.setAttribute("successMessage", "Đã duyệt đơn thành công.");
            } else if ("reject".equals(action)) {
                laDB.updateApplicationStatus(appId, REJECTED_STATUS_ID, account.getAid());
                request.setAttribute("successMessage", "Đã từ chối đơn thành công.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID đơn không hợp lệ.");
        } catch (SecurityException e) {
            request.setAttribute("errorMessage", "Lỗi bảo mật: Bạn không có quyền thực hiện hành động này.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi xử lý: " + e.getMessage());
        }
        doGet(request, response);
    }
} 