<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title> Murach's Java Servlets and JSP </title>
    <link rel = "stylesheet" href="main.css" type="text/css">
</head>
<body>
    <h1>Join our email list</h1>
    <p>To join our email list, enter your name and email address below.</p>
    <form action="emailList" method ="post">
    <input type="hidden" name = "action" value ="add">
    <label>Email:</label>
    <input type="email" name="email" required><br>
    <label>First Name:</label>
    <input type="text" name="firstName" required><br>
    <label>Last Name:</label>
    <input type="text" name="lastName" required><br>
    <label>Date of Birth</label>
    <input type="text" name="dob" required><br>

    <input type="submit" value="Submit" id="submit">
    </form>
</body>
</html>
