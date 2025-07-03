package Controller;

import auth.PasswordUtil;
import dal.DivisionDBContext;
import dal.RoleDBContext;
import dal.UserDBContext;
import dal.AccountDBContext;
import Entity.Division;
import Entity.Role;
import Entity.User;
import Entity.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import dal.GroupDBContext;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register", "/getGroupsByDivision"})
public class RegisterServlet extends HttpServlet {
    private DivisionDBContext divisionDB;
    private RoleDBContext roleDB;
    private UserDBContext userDB;
    private AccountDBContext accountDB;
    private GroupDBContext groupDB;

    @Override
    public void init() throws ServletException {
        this.divisionDB = new DivisionDBContext();
        this.roleDB = new RoleDBContext();
        this.userDB = new UserDBContext();
        this.accountDB = new AccountDBContext();
        this.groupDB = new GroupDBContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String divisionIdParam = request.getParameter("divisionId");
        if (request.getServletPath().equals("/getGroupsByDivision") && divisionIdParam != null) {
            // AJAX request for groups by division
            try {
                int divisionId = Integer.parseInt(divisionIdParam);
                List<Entity.Group> groups = groupDB.getByDivisionId(divisionId);
                response.setContentType("application/json;charset=UTF-8");
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < groups.size(); i++) {
                    Entity.Group g = groups.get(i);
                    json.append("{\"gid\":" + g.getGid() + ",\"name\":\"Nhóm " + g.getGid() + "\"}");
                    if (i < groups.size() - 1) json.append(",");
                }
                json.append("]");
                response.getWriter().write(json.toString());
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("[]");
            }
            return;
        }
        try {
            List<Division> divisions = divisionDB.getAll();
            List<Role> roles = roleDB.getAll();
            List<Entity.Group> groups = groupDB.getAll();
            request.setAttribute("divisions", divisions);
            request.setAttribute("roles", roles);
            request.setAttribute("groups", groups);
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
        } catch (Exception e) {
            // Log the exception and maybe show an error page
            e.printStackTrace();
            throw new ServletException("Error fetching data for registration page", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String pass = request.getParameter("password");
            String email = request.getParameter("email");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String groupIdStr = request.getParameter("groupId");
            Integer groupId = (groupIdStr == null || groupIdStr.isEmpty()) ? null : Integer.parseInt(groupIdStr);
            int roleId = Integer.parseInt(request.getParameter("roleId"));

            if (accountDB.getByUsername(username) != null) {
                request.setAttribute("errorMessage", "Tên đăng nhập đã tồn tại.");
                doGet(request, response);
                return;
            }

            if (userDB.getByEmail(email) != null) {
                request.setAttribute("errorMessage", "Email đã tồn tại.");
                doGet(request, response);
                return;
            }

            String hashedPassword = PasswordUtil.hashPassword(pass);
            Entity.Group group = (groupId != null) ? groupDB.get(groupId) : null;
            Role role = roleDB.get(roleId);

            // Tạo User (thông tin cá nhân)
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstname(firstname);
            newUser.setLastname(lastname);
            userDB.insert(newUser);

            // Lấy lại user vừa tạo
            User managedUser = userDB.getById(newUser.getUid());
            if (managedUser == null) throw new ServletException("Failed to retrieve user after insertion.");

            // Tạo Account
            Account account = new Account();
            account.setUser(managedUser);
            account.setUsername(username);
            account.setPassword(hashedPassword);
            account.setActive(true);
            account.setGroup(group);
            accountDB.insert(account);

            // Lấy lại account vừa tạo
            Account managedAccount = accountDB.getByUsername(username);
            if (managedAccount != null) {
                accountDB.addRoleForAccount(managedAccount, role);
            } else {
                throw new ServletException("Failed to retrieve account after insertion.");
            }

            response.sendRedirect("login?registration=success");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ. Vui lòng thử lại.");
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình đăng ký: " + e.getMessage());
            doGet(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles user registration by interacting with the database.";
    }
} 