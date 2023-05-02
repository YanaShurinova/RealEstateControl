<%@ page import="models.dto.User" %>
<%@ page import="control.Controller" %>
<%@ page import="models.dto.UserInfo" %><%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 21.03.2022
  Time: 21:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Информация о пользователе</title>
    <style><%@include file="/style.css"%></style>
</head>
<body>
    <jsp:include page="/menu/menu.jsp"></jsp:include>
    <%User user = (User)request.getSession().getAttribute("user");
        UserInfo info;
        if((request.getParameter("usid")!=null)&&(Integer.parseInt(request.getParameter("usid"))!=user.getUid())){
            info=Controller.getInstance().getUserInfo(Integer.parseInt(request.getParameter("usid")));
        }
        else {
            info= Controller.getInstance().getUserInfo(user.getUid());
    %>
    <button type="button"  class="user_info_but">
        <a href="changeUserInfo">Редактировать</a></button>
    <%}%>
    <p class="user_info">
        <label for="name">ФИО</label><br>
        <input type="text" id="name" name="FIO" disabled="disabled" value="<%=info.getName()%>"><br>
        <label for="datareg">Дата регистрации</label><br>
        <input type="date" id="datareg" name="Regd" disabled="disabled" value="<%=info.getRegd()%>"><br>
        <%if(info.getDesc()!=null){%>
            <label for="description">О себе</label><br>
            <textarea class="text_area" name="depiction" id="description" disabled="disabled"><%=info.getDesc()%></textarea><br>
        <%}
        if((request.getParameter("usid")!=null)&&(Integer.parseInt(request.getParameter("usid"))!=user.getUid())){%>
            <a href="estates?usid=<%=Integer.parseInt(request.getParameter("usid"))%>">Недвижимость пользователя</a>
        <%}else{%>
            <a href="estates">Недвижимость пользователя</a>
        <%}%>
    </p>

</body>
</html>
