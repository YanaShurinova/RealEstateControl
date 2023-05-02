<%@ page import="java.time.LocalDate" %>
<%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 20.03.2022
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style><%@include file="/style.css"%></style>
    <title> Регистрация </title>
</head>
<body>
<form action = "RegistrationServlet" method="post" class="enter" >
    <label for = "login">Логин</label><br>
    <input type="text" id="login" name="login"><br>
    <label for = "password1">Пароль</label><br>
    <input type="password" id="password1" name="password1"><br>
    <label for = "password2">Повторите пароль</label><br>
    <input type="password" id="password2" name="password2"><br>
    <label for = "name">ФИО</label><br>
    <input type="text" name="FIO" id="name"><br><br>
    <input type = "submit" name="submit2" value="OK"><br>
</form>
</body>
</html>