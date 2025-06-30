<%-- 
    Document   : create_leave_application
    Created on : 22 thg 6, 2025, 23:33:59
    Author     : FPTSHOP
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Tạo đơn xin nghỉ" scope="request"/>

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
                        <span>Chào, <strong>${sessionScope.account.lastname}</strong>!</span>
                    </div>
                 </c:if>
            </header>
            <main>
                <div class="form-container" style="margin: 20px auto;">
                    <h2>Tạo đơn xin nghỉ phép mới</h2>

                    <div class="user-details-box">
                        <p><strong>Người tạo đơn:</strong> ${sessionScope.account.firstname} ${sessionScope.account.lastname}</p>
                        <p><strong>Chức vụ:</strong>
                            <c:forEach items="${sessionScope.roles}" var="role" varStatus="loop">
                                ${role.rname}${!loop.last ? ', ' : ''}
                            </c:forEach>
                        </p>
                    </div>

                    <c:if test="${not empty requestScope.successMessage}">
                        <p style="color: green; font-weight: bold;">${requestScope.successMessage}</p>
                    </c:if>
                    <c:if test="${not empty requestScope.errorMessage}">
                        <p class="error-message">${requestScope.errorMessage}</p>
                    </c:if>

                    <form id="leave-form" action="create_leave_application" method="post" novalidate>
                        <div class="form-group">
                            <label for="startDate">Ngày bắt đầu nghỉ:</label>
                            <input type="date" id="startDate" name="startDate" value="${submittedValues.startDate}">
                            <p class="error-text" id="startDateError">${errors.startDate}</p>
                        </div>
                        <div class="form-group">
                            <label for="endDate">Ngày kết thúc nghỉ:</label>
                            <input type="date" id="endDate" name="endDate" value="${submittedValues.endDate}">
                             <p class="error-text" id="endDateError">${errors.endDate}</p>
                        </div>
                        <div class="form-group">
                            <label for="reason">Lý do:</label>
                            <textarea id="reason" name="reason" rows="5" style="width: 100%; padding: 12px; border: 1px solid #ccc; border-radius: 5px; box-sizing: border-box; font-family: 'Segoe UI';">${submittedValues.reason}</textarea>
                             <p class="error-text" id="reasonError">${errors.reason}</p>
                        </div>
                        <button type="submit" class="btn">Gửi đơn</button>
                    </form>
                </div>
            </main>
        </div>
    </div>
    <script>
        // Add event listener to the form to trigger validation on submit
        document.getElementById('leave-form').addEventListener('submit', function(event) {
            // Prevent the form from submitting by default
            event.preventDefault();
            
            // Clear all previous error messages
            document.getElementById('startDateError').textContent = '';
            document.getElementById('endDateError').textContent = '';
            document.getElementById('reasonError').textContent = '';

            let isValid = true;

            // Get form values
            const startDateInput = document.getElementById('startDate');
            const endDateInput = document.getElementById('endDate');
            const reasonInput = document.getElementById('reason');

            const startDate = startDateInput.value;
            const endDate = endDateInput.value;
            const reason = reasonInput.value.trim();
            
            // Get today's date in YYYY-MM-DD format
            const today = new Date();
            today.setHours(0, 0, 0, 0); // Normalize to midnight
            const todayString = today.toISOString().split('T')[0];

            // 1. Validate Start Date
            if (!startDate) {
                document.getElementById('startDateError').textContent = 'Vui lòng chọn ngày bắt đầu.';
                isValid = false;
            } else if (startDate < todayString) {
                document.getElementById('startDateError').textContent = 'Ngày bắt đầu không được là ngày trong quá khứ.';
                isValid = false;
            }

            // 2. Validate End Date
            if (!endDate) {
                document.getElementById('endDateError').textContent = 'Vui lòng chọn ngày kết thúc.';
                isValid = false;
            } else if (endDate < startDate) {
                document.getElementById('endDateError').textContent = 'Ngày kết thúc không được trước ngày bắt đầu.';
                isValid = false;
            }

            // 3. Validate Reason
            if (!reason) {
                document.getElementById('reasonError').textContent = 'Vui lòng nhập lý do.';
                isValid = false;
            } else if (!/[a-zA-Z]/.test(reason)) { // Check if reason contains at least one letter
                document.getElementById('reasonError').textContent = 'Lý do phải chứa ít nhất một ký tự chữ cái.';
                isValid = false;
            }
            
            // If all checks pass, submit the form
            if (isValid) {
                this.submit();
            }
        });
    </script>
</body>
</html>
