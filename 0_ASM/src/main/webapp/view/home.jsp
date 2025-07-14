<%-- 
    Document   : home
    Created on : 22 thg 6, 2025, 23:33:30
    Author     : FPTSHOP
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Trang chủ" scope="request"/>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${not empty pageTitle ? pageTitle : "Hệ thống Quản lý nghỉ phép"}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
    <div class="page-container">
        <nav class="sidebar">
            <div class="sidebar-header">
                <h2>Menu</h2>
            </div>
            <c:if test="${not empty sessionScope.account}">
                <c:forEach items="${sessionScope.features}" var="f">
                    <a href="${pageContext.request.contextPath}${f.link}" class="${pageContext.request.servletPath.endsWith(f.link) ? 'active' : ''}">${f.fname}</a>
                </c:forEach>
                <a href="${pageContext.request.contextPath}/logout" class="logout-link">Đăng xuất</a>
            </c:if>
            <c:if test="${empty sessionScope.account}">
                 <a href="${pageContext.request.contextPath}/login" class="${pageContext.request.servletPath.endsWith('/login') ? 'active' : ''}">Đăng nhập</a>
            </c:if>
        </nav>
        <div class="main-content">
            <header>
                 <h1>${not empty pageTitle ? pageTitle : "Chào mừng"}</h1>
                 <c:if test="${not empty sessionScope.account}">
                    <div class="user-info">
                        <span>Chào, <strong>${sessionScope.account.user.lastname}</strong>!</span>
                    </div>
                 </c:if>
            </header>
            <main>
                <h2>Chào mừng trở lại, ${sessionScope.account.user.lastname}!</h2>
            </main>
        </div>
    </div>
    </body>
</html>
