<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title> Murach's Java Servlets and JSP </title>
    <link rel = "stylesheet" href="main.css" type="text/css">
</head>
<body>
    <img src="image/images.jpg" alt="Murach's Java Servlets and JSP" width="200" height="200">
    <h1>Survey</h1>
    <p>If you have a moment, we'd apprreciate it if you fill out the survey.</p>
    <h2>Join our mailist</h2>
    <p>To join our email list, enter your name and email address below.</p>
    <form action="emailList" method ="post">
    <input type="hidden" name = "action" value ="add">
    <label>Email:</label>
    <input type="email" name="email" ><br>
    <label>First Name:</label>
    <input type="text" name="firstName" ><br>
    <label>Last Name:</label>
    <input type="text" name="lastName" ><br>
    <label>Date of Birth</label>
    <input type="text" name="dob" ><br>

    <h2>How did you hear about us?</h2>
    <input type="radio" name="source" value="Search Engine"> Search Engine
    <input type="radio" name="source" value="Word of mouth"> Word of mouth
    <input type="radio" name="source" value="Social Media"> Social Media
    <input type="radio" name="source" value="Other"> Other
    <h2>Would you like to receive annoucements about new CDs and speical offers?</h2>
    <input type="checkbox" name="announcements" value="Yes"> YES, i'd like that<br>
    <input type="checkbox" name="announcements" value="Yes"> YES, please send me email annoucements.<br>
    <p>Please contact me by:
        <select name="contact">
            <option value="Email" selected>Email or postal mail</option>
            <option value="Phone">Phone</option>
            <option value="Text">Text</option>
        </select>
    </p>
    <input type="submit" value="Submit" id="submit">
    </form>
</body>
</html>
