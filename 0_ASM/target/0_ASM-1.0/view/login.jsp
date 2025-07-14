<%-- 
    Document   : login
    Created on : 22 thg 6, 2025, 23:36:31
    Author     : FPTSHOP
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Đăng nhập" scope="request"/>
<%-- Không include header vì trang login không có menu --%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${pageTitle}</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <main>
            <div class="form-container">
                <h2 class="form-title">Đăng nhập hệ thống</h2>
                
                <c:if test="${not empty requestScope.errorMessage}">
                    <p class="error-message">${requestScope.errorMessage}</p>
                </c:if>

                <form action="login" method="post">
                    <div class="form-group">
                        <label for="username">Tên đăng nhập:</label>
                        <input type="text" id="username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Mật khẩu:</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn">Đăng nhập</button>
                </form>
                
                <!--
                <div class="register-link">
                    <a href="register">Đăng ký tài khoản mới</a>
                </div>
                -->
            </div>
        </main>
    </body>
</html>
