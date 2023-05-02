<%@ page import="models.dto.User" %>
<%@ page import="models.dto.RealEstate" %>
<%@ page import="control.Controller" %><%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 18.05.2022
  Time: 0:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавить расходы</title>
    <style><%@include file="/style.css"%></style>
</head>
<body>
<jsp:include page="/menu/menu.jsp"></jsp:include>
<%
    User user=(User)request.getSession().getAttribute("user");
    RealEstate estate= Controller.getInstance().getRealEstate(user.getUid(),Integer.parseInt(request.getParameter("eid")));
%>
<form action = "AddOutcomeServlet?eid=<%=estate.getEid()%>" method="post" class="user_info spend_button" >
    <label for="type">Название:</label><br>
    <input type="text" id="type" name="name" ><br>
    <label for="summa"> Сумма: </label> <br>
    <input type="text" id="summa" name="value" ><br>
    <label for="desc_come"> Описание: </label> <br>
    <input type="text" id="desc_come"name="description"><br>
    <label for="date">Дата</label><br>
    <input type="date" name="date" id="date">
    <p>
        <button type = "submit" name="submit3" >ОК</button>
    </p>
</form>
</body>
</html>
