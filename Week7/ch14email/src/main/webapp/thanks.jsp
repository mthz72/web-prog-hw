<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="main.css" type="text/css"/>
</head>
<body>
    <h1>Thanks for joining our email list</h1>
    <p>Here is the information that you entered:</p>
    <label>Email:</label>
    <span>${user.email}</span><br>
    <label>First Name:</label>
    <span>${user.firstName}</span><br>
    <label>Last Name:</label>
    <span>${user.lastName}</span><br>

    <%--
        Slide 51: mail gui hong thi bao loi ngay tai day, nhung nguoi dung VAN
        da duoc ghi danh thanh cong nen khong bat nhap lai.
        errorMessage do servlet tu tao va co chua the <br> nen in thang bang
        ${...}; khong bao gio in du lieu nguoi dung go theo kieu nay.
    --%>
    <c:if test="${not empty errorMessage}">
        <p><i>${errorMessage}</i></p>
    </c:if>
    <c:if test="${not empty mailNotice}">
        <p><i>${mailNotice}</i></p>
    </c:if>

    <p>To enter another email address, click on the Back
       button in your browser or the Return button shown below.</p>
    <form action="emailList" method="get">
        <input type="hidden" name="action" value="join">
        <input type="submit" value="Return" class="margin_left">
    </form>
</body>
</html>
