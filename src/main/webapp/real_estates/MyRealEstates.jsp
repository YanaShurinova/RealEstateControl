<%@ page import="models.dto.User" %>
<%@ page import="control.Controller" %>
<%@ page import="models.dto.UserInfo" %>
<%@ page import="models.dto.RealEstate" %><%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 23.03.2022
  Time: 12:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style><%@include file="/style.css"%></style>
    <script src="http://code.jquery.com/jquery-2.2.4.js"></script>
    <title>Моя недвижимость</title>
</head>
<body>
<jsp:include page="/menu/menu.jsp"></jsp:include>
<h1 align="center" class="color_header_1">
    <%if(request.getParameter("usid")!=null){%>
    Недвижимость пользователя: <%=Controller.getInstance().getUserInfo(Integer.parseInt(request.getParameter("usid"))).getName()%>
    <%}else{%>
    Моя недвижимость
    <%}%>
</h1>
<%if(request.getParameter("usid")==null){%>
<form action="NewEstate" method="post">
    <p align="right" class = "add_button">
        <button type="submit" name="addEst1">Добавить</button>
    </p>
</form>
<%}
    User user = (User)request.getSession().getAttribute("user");
    UserInfo info= user.getInfo();

%>
<ul class="list_estates">
    <%if(request.getParameter("usid")!=null&&(Integer.parseInt(request.getParameter("usid"))!=user.getUid())){
    for(RealEstate estates:Controller.getInstance().getAllEstateUser(Integer.parseInt(request.getParameter("usid")))){%>
    <li>
        <img src="estates/image?eid=<%=estates.getEid()%>" align="middle" width="250" height="250">
        <p>
            <%=estates.getType()%> <Br>
            <%=estates.getAddress()%>
        </p><Br>
    </li>
    <%}
    }else{
    for(RealEstate estates:Controller.getInstance().getAllEstateUser(user.getUid())){%>
    <li>
        <a href="estate?eid=<%=estates.getEid()%>"><img src="estates/image?eid=<%=estates.getEid()%>" align="middle" width="250" height="250"></a>
        <p>
            <a href="estate?eid=<%=estates.getEid()%>"><%=estates.getType()%> <Br>
            <%=estates.getAddress()%></a>
        </p><Br>
            <div align="right">
                <button type="submit" value="<%=estates.getEid()%>" onclick="deleteEstate(this)">Удалить</button>
            </div>
    </li>
    <%}
    }%>
</ul>
<input type="text" id="usid" name="usid" readonly hidden value="<%=user.getUid()%>">
<script>

    function deleteEstate(element){
        $.ajax({
            url: 'estates/delete',
            method: 'POST',
            data: {
                usid: $('#usid').val(),
                eid: element.value
            },
            success: function(response){
                if (response!=='success'){
                    alert(response);
                } else {
                    element.parentNode.parentNode.hidden=true;
                }
            }

        });

    }
</script>
</body>
</html>
