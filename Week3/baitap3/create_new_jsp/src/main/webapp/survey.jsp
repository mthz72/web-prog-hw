<%-- JSTL 2.0 (Jakarta EE 9) van dung URI cu; URI "jakarta.tags.core" chi co tu JSTL 3.0 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="main.css" type="text/css">
</head>
<body>
    <h1>Thanks for taking our survey!</h1>
    <p>Here is the information that you entered:</p>

    <table>
        <tr>
            <td class="label">Email:</td>
            <td>${user.email}</td>
        </tr>
        <tr>
            <td class="label">First Name:</td>
            <td>${user.firstName}</td>
        </tr>
        <tr>
            <td class="label">Last Name:</td>
            <td>${user.lastName}</td>
        </tr>
        <tr>
            <td class="label">Heard From:</td>
            <td>${user.heardFrom}</td>
        </tr>
        <tr>
            <td class="label">Updates:</td>
            <td>${user.updates}</td>
        </tr>
        <%-- Buoc 8: chi hien dong Contact Via khi nguoi dung muon nhan updates --%>
        <c:if test="${user.updates == 'Yes'}">
        <tr>
            <td class="label">Contact Via:</td>
            <td>${user.contactVia}</td>
        </tr>
        </c:if>
    </table>

    <p><a href="index.html">Return to the survey</a></p>
</body>
</html>
