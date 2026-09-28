<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="main.css" type="text/css">
</head>
<body>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${sqlStatement == null}">
    <c:set var="sqlStatement" value="SELECT * FROM User" />
</c:if>
<h1>The SQL Gateway</h1>
<p>Enter an SQL statement and click the Execute button</p>

<p><b>SQL Statement:</b></p>
<form action="sqlGateway" method="post">
    <textarea name="sqlStatement" rows="8" cols="60">${sqlStatement}</textarea>
    <br>
    <input type="submit" value="Execute">
</form>

<p><b>SQL result:</b></p>
${sqlResult}
</body>
</html>
