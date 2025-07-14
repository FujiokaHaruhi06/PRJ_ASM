<%-- 
    Document   : application_status
    Created on : 22 thg 6, 2025, 23:34:25
    Author     : FPTSHOP
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<c:set var="pageTitle" value="Tình trạng đơn" scope="request"/>

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
                <h2>Tình trạng đơn xin nghỉ phép</h2>
    
                <c:if test="${empty requestScope.applications}">
                    <p>Không có đơn xin nghỉ phép nào để hiển thị.</p>
                </c:if>
                
                <c:if test="${not empty requestScope.applications}">
                    <table>
                        <thead>
                            <tr>
                                <!-- <th>ID</th> -->
                                <th>Người nộp đơn</th>
                                <th>Nghỉ từ</th>
                                <th>Nghỉ đến</th>
                                <th>Lý do</th>
                                <th>Trạng thái</th>
                                <th>Người duyệt</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${requestScope.applications}" var="app">
                                <tr>
                                    <!-- <td>${app.lid}</td> -->
                                    <td>${app.account.user.firstname} ${app.account.user.lastname}</td>
                                    <td><fmt:formatDate value="${app.startDate}" pattern="dd/MM/yyyy" /></td>
                                    <td><fmt:formatDate value="${app.endDate}" pattern="dd/MM/yyyy" /></td>
                                    <td>${app.reason}</td>
                                    <td>${app.status.sname}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty app.approverAccount && app.status.sname != 'Pending'}">
                                                ${app.approverAccount.user.firstname} ${app.approverAccount.user.lastname}
                                            </c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </main>
        </div>
    </div>
    </body>
</html>
