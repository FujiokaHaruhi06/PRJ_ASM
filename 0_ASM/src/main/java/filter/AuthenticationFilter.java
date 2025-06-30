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

@WebFilter("/*") // Lọc tất cả các request
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Cho phép truy cập các tài nguyên công khai (login, register, css, js,...)
        boolean isPublicResource = path.startsWith("/login") || 
                                   path.startsWith("/register") || 
                                   path.startsWith("/css/") || 
                                   path.startsWith("/js/");

        boolean isLoggedIn = (session != null && session.getAttribute("account") != null);

        if (isLoggedIn || isPublicResource) {
            // Nếu đã đăng nhập hoặc là tài nguyên công khai, cho phép đi tiếp
            chain.doFilter(request, response);
        } else {
            // Nếu chưa đăng nhập và truy cập trang bảo mật, chuyển hướng về trang login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }
} 