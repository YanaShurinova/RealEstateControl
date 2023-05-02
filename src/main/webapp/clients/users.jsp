<%@ page import="models.dto.User" %>
<%@ page import="control.Controller" %>
<%@ page import="models.dto.UserInfo" %><%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 23.03.2022
  Time: 13:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style><%@include file="/style.css"%></style>
    <title>Пользователи</title>
</head>
<body>
<jsp:include page="/menu/menu.jsp"></jsp:include>

    <ul class="list_users">
        <%for(User user: Controller.getInstance().getAllUsers()){
            UserInfo info = Controller.getInstance().getUserInfo(user.getUid());
        %>
        <li>
            <a href="userinfo?usid=<%=user.getUid()%>"><%=info.getName()%></a>
        </li>
        <%}%>
    </ul>
</body>
</html>
