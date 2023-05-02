<%@ page import="models.dto.User" %>
<%@ page import="models.dto.UserInfo" %>
<%@ page import="control.Controller" %><%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 04.04.2022
  Time: 23:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Редактирование Пользователя</title>
    <style><%@include file="/style.css"%></style>
</head>
<body>
    <jsp:include page="/menu/menu.jsp"></jsp:include>
    <%User user=(User)request.getSession().getAttribute("user");
    UserInfo info= Controller.getInstance().getUserInfo(user.getUid());
    %>
    <form action="ChangeUserInfoServlet" method="post" >
        <p class="user_info">
            <label for = "name">ФИО</label><br>
            <input type="text" name="name" id="name" value="<%=info.getName()%>"><br>
            <label for = "password1">Пароль</label><br>
            <input type="password" id="password1" name="Password1" ><br>
            <label for = "password2">Повторите пароль</label><br>
            <input type="password" id="password2" name="Password2" ><br>
            <label for="description">О себе</label><br>
            <%if(info.getDesc()!=null){%>
            <!--не выводится описание -->
            <textarea class="text_area" id="description" name="depiction" ><%=info.getDesc()%></textarea><br>
            <%}else{%>
            <textarea class="text_area" id="description" name="depiction" placeholder="О себе..."></textarea><br>
            <%}%>
        </p>
        <button type="submit" name="EditInfo" class="edit_info_but">Сохранить изменения</button>
    </form>

</body>
</html>
