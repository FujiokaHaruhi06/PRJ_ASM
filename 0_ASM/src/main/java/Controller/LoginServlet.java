package Controller;

import dal.UserDBContext; // Giả định bạn có lớp này
import Entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import auth.PasswordUtil;
import dal.FeatureDBContext;
import Entity.Feature;
import dal.AccountDBContext;
import Entity.Account;
import dal.RoleDBContext;
import Entity.Role;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UserDBContext userDBContext;
    private AccountDBContext accountDBContext;
    private FeatureDBContext featureDBContext;
    private RoleDBContext roleDBContext;

    @Override
    public void init() throws ServletException {
        userDBContext = new UserDBContext();
        accountDBContext = new AccountDBContext();
        featureDBContext = new FeatureDBContext();
        roleDBContext = new RoleDBContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("view/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        Account account = accountDBContext.getByUsername(user);

        if (account != null && account.isActive() && PasswordUtil.verifyPassword(pass, account.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("account", account);
            
            // Lấy và lưu các feature của account vào session
            List<Feature> features = featureDBContext.getFeaturesByAccountId(account.getAid());
            session.setAttribute("features", features);
            
            // Lấy và lưu roles của account vào session
            List<Role> roles = roleDBContext.getRolesByAccountId(account.getAid());
            session.setAttribute("roles", roles);
            
            response.sendRedirect("home"); // Chuyển đến trang chủ sau khi đăng nhập thành công
        } else {
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("view/login.jsp").forward(request, response);
        }
    }
} 