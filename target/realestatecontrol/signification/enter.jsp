<%@ page import="java.util.Date" %>
<%@ page import="java.time.LocalDate" %><%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 20.03.2022
  Time: 13:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style><%@include file="/style.css"%></style>
    <title> Вход </title>
</head>
<body>
<form action = "EnterServlet" method="post" class="enter" >
    <p class="enter">
        <label for = "login">Логин</label><br>
        <input type="text" id="login" name="login" ><br>
        <label for="password">Пароль</label><br>
        <input type="password" name="password" id="password"><br>
        <p class="ent_button enter_and_reg">
            <a  href = "registration"> Зарегистрироваться </a>
            <button type = "submit" name="but_enter" >Войти</button>
        </p>
    </p>
</form>

</body>
</html>