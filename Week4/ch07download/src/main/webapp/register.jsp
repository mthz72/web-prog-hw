<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="main.css" type="text/css">
    <title>Download page</title>
</head>
<body>
    <h1>Download registration</h1>
    <p>To register for our downloads, please enter your name and email address below. Then, click on the submit button.</p>
    <form action="download" method="post">
        <intput type="hidden" name="action" value="registerUser">
        <label for="Email">Email:</label>
        <input type="email" name="Email" value="${user.email}"><br>
        <label for="firstName">First Name:</label>
        <input type="text" name="FirstName" value="${user.firstName}"><br>
        <label for="lastName">Last Name:</label>
        <input type="text" name="lastName" value="${user.lastName}"><br>
        <input type="submit" value="Register">
</body>
</html>