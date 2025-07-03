<%-- 
    Document   : work_schedule
    Created on : 22 thg 6, 2025, 23:35:19
    Author     : FPTSHOP
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Lịch làm việc" scope="request"/>

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
                    <a href="${pageContext.request.contextPath}${f.link}" class="${pageContext.request.servletPath.endsWith(f.link) ? 'active' : ''}">${f.description}</a>
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
                <h2>Lịch làm việc của nhóm trong tuần</h2>
                <c:if test="${empty weekDates || empty workStatusWeekMap}">
                    <p>Không có dữ liệu lịch làm việc để hiển thị.</p>
                </c:if>
                <c:if test="${not empty weekDates && not empty workStatusWeekMap}">
                    <table>
                        <thead>
                            <tr>
                                <th>Nhân viên</th>
                                <c:forEach items="${weekDateLabels}" var="label">
                                    <th>${label}</th>
                                </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${workStatusWeekMap}" var="entry">
                                <tr>
                                    <td>
                                        ${entry.key.user.firstname} ${entry.key.user.lastname}
                                        <c:if test="${entry.key.aid == sessionScope.account.aid}"><strong>(Bạn)</strong></c:if>
                                    </td>
                                    <c:forEach items="${weekDates}" var="date">
                                        <c:set var="isOnLeave" value="${entry.value[date]}" />
                                        <td style="background-color: ${isOnLeave ? '#e74c3c' : '#2ecc71'}; color: white; font-weight: bold;">
                                            ${isOnLeave ? 'Nghỉ phép' : 'Làm việc'}
                                        </td>
                                    </c:forEach>
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
