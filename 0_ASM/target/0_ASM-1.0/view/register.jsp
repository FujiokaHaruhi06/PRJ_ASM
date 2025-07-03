<%-- 
    Document   : register
    Created on : 23 thg 6, 2025, 01:17:21
    Author     : FPTSHOP
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tạo tài khoản</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <main>
            <div class="form-container">
                <h2>Tạo tài khoản người dùng mới</h2>
                
                <c:if test="${not empty requestScope.errorMessage}">
                    <p class="error-message">${requestScope.errorMessage}</p>
                </c:if>
                
                <form action="register" method="post">
                    <div class="form-group">
                        <label for="username">Tên đăng nhập:</label>
                        <input type="text" id="username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Mật khẩu:</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                     <div class="form-group">
                        <label for="firstname">Họ và tên đệm:</label>
                        <input type="text" id="firstname" name="firstname" required>
                    </div>
                    <div class="form-group">
                        <label for="lastname">Tên:</label>
                        <input type="text" id="lastname" name="lastname" required>
                    </div>
                    <div class="form-group">
                        <label for="divisionId">Phòng (Division):</label>
                        <select id="divisionId" name="divisionId">
                            <option value="">-- Không chọn --</option>
                            <c:forEach items="${divisions}" var="d">
                                <option value="${d.divid}">${d.divname}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="groupId">Nhóm:</label>
                        <select id="groupId" name="groupId">
                            <option value="">-- Không chọn --</option>
                        </select>
                    </div>
                     <div class="form-group">
                        <label for="roleId">Chức vụ:</label>
                        <select id="roleId" name="roleId" required>
                             <option value="">-- Chọn chức vụ --</option>
                            <c:forEach items="${roles}" var="role">
                                <option value="${role.rid}">${role.rname}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <button type="submit" class="btn">Tạo tài khoản</button>
                </form>
                <a href="${pageContext.request.contextPath}/login" class="form-link">Quay lại trang Đăng nhập</a>
            </div>
        </main>
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                var divisionSelect = document.getElementById('divisionId');
                var groupSelect = document.getElementById('groupId');
                divisionSelect.addEventListener('change', function() {
                    var divisionId = this.value;
                    groupSelect.innerHTML = '<option value="">-- Không chọn --</option>';
                    if (divisionId) {
                        fetch('getGroupsByDivision?divisionId=' + divisionId)
                            .then(response => response.json())
                            .then(data => {
                                data.forEach(function(g) {
                                    var opt = document.createElement('option');
                                    opt.value = g.gid;
                                    opt.textContent = g.name;
                                    groupSelect.appendChild(opt);
                                });
                            });
                    }
                });
            });
        </script>
    </body>
</html>
