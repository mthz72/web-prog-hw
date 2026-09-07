<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="main.css" type="text/css"/>
</head>
<body>
    <%-- Buoc 10: include luc bien dich (compile-time) thay cho <c:import> luc chay --%>
    <%@ include file="/includes/header.html" %>

    <h1>Thanks for joining our email list</h1>
    <p>Here is the information that you entered:</p>
    <label>Email:</label>
    <span>${user.email}</span><br>
    <label>First Name:</label>
    <span>${user.firstName}</span><br>
    <label>Last Name:</label>
    <span>${user.lastName}</span><br>
    <label>Date of Birth:</label>
    <span>${dob}</span><br>
    <label>You know us from:</label>
    <span>${source}</span><br>
    <label>Contact by:</label>
    <span>${contact}</span><br>
    <p>To enter another email address, click on the Back
    button in your browser or the Return button shown
    below.</p>
    <form action="index.jsp" method="get">
        <input type="hidden" name="action" value="join">
        <input type="submit" value="Return">
    </form>

    <%@ include file="/includes/footer.jsp" %>
</body>
</html>
