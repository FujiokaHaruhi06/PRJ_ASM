package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import Entity.Account;
import Entity.Role;
import Entity.Feature;
import dal.FeatureDBContext;

@WebFilter("/*") // Lọc tất cả các request
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI();
        // Lấy account và roles từ session
        Entity.Account account = (session != null) ? (Entity.Account) session.getAttribute("account") : null;
        java.util.List<Entity.Role> roles = (session != null) ? (java.util.List<Entity.Role>) session.getAttribute("roles") : null;
        boolean isManager = false;
        if (roles != null) {
            for (Entity.Role r : roles) {
                if ("Group Leader".equalsIgnoreCase(r.getRname()) || "Division Leader".equalsIgnoreCase(r.getRname())) {
                    isManager = true;
                    break;
                }
            }
        }
        // Các đường dẫn chỉ cho phép quản lý truy cập
        boolean isManagerFeature = uri.contains("application_review") || uri.contains("work_schedule");
        // Các đường dẫn công khai
        boolean isPublic = uri.contains("login") || uri.contains("register") || uri.contains("css/") || uri.contains("js/");
        // Xử lý riêng cho trang đăng ký
        if (uri.contains("register")) {
            // Lấy danh sách feature từ DB (tránh phụ thuộc session)
            FeatureDBContext featureDB = new FeatureDBContext();
            java.util.List<Entity.Feature> allFeatures = featureDB.getAll();
            boolean registerActive = false;
            for (Entity.Feature f : allFeatures) {
                if (f.getLink() != null && uri.contains(f.getLink()) && f.isIsActive()) {
                    registerActive = true;
                    break;
                }
            }
            if (!registerActive) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            // Nếu là trang register và isActive=true, cho phép truy cập không cần đăng nhập
            chain.doFilter(request, response);
            return;
        }
        // Chặn truy cập home nếu chưa đăng nhập
        if ((uri.contains("home") || isManagerFeature) && account == null && !isPublic) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (isManagerFeature && !isManager && account != null) {
            res.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        // Nếu đã đăng nhập mà truy cập trang login, chuyển về home
        if (uri.contains("login") && account != null) {
            res.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        // Chặn truy cập các feature bị tắt (isActive = false)
        if (account != null && session.getAttribute("features") != null) {
            @SuppressWarnings("unchecked")
            java.util.List<Entity.Feature> features = (java.util.List<Entity.Feature>) session.getAttribute("features");
            boolean allowed = false;
            for (Entity.Feature f : features) {
                if (uri.contains(f.getLink())) {
                    allowed = true;
                    break;
                }
            }
            // Nếu không nằm trong danh sách feature active, chặn truy cập
            if (!allowed && !uri.contains("logout") && !uri.contains("home") && !uri.contains("css/") && !uri.contains("js/")) {
                res.sendRedirect(req.getContextPath() + "/home");
                return;
            }
        }
        chain.doFilter(request, response);
    }
} 