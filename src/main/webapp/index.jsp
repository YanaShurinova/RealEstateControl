<%@ page import="models.dto.User" %><%--
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
    <title>Главная страница</title>
</head>
<body>

<jsp:include page="menu/menu.jsp"></jsp:include>
    <Br>

<%if(request.getSession().getAttribute("user")!=null){
    User user=(User)request.getSession().getAttribute("user");%>
    <fieldset class="main_page2">Рабочее пространство пользователя: <%=user.getInfo().getName()%></fieldset>
<%}else{%>
    <h1 class = "main_h1" align="center">Главная страница</h1>
<%}%>
<div class="main_page">
<div>
    <a href="estates"><img src="imagesMain/Real_Estate1.jpg" width="350" height="350"></a><br>
    <a href="estates">Моя недвижимость</a>
</div>
<div>
    <a href="statistics"><img src="imagesMain/statistics1.jpg" width="350" height="350"></a><br>
    <a href="statistics">Статистика</a>
</div>
<div>
    <a href="users"><img src="imagesMain/users.jpg" width="350" height="350"></a><br>
    <a href="users">Пользователи</a>
</div>
</div>
</body>
</html>