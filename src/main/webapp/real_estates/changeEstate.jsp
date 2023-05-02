<%@ page import="models.dto.User" %>
<%@ page import="models.dto.RealEstate" %>
<%@ page import="control.Controller" %><%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 18.05.2022
  Time: 16:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style><%@include file="/style.css"%></style>
    <title>Изменить информацию о недвижимости</title>
</head>
<body>
<jsp:include page="/menu/menu.jsp"></jsp:include>
<%User user=(User)request.getSession().getAttribute("user");
    RealEstate estate= Controller.getInstance().getRealEstate(user.getUid(),Integer.parseInt(request.getParameter("eid")));
%>
<form action="ChangeEstateServlet?eid=<%=estate.getEid()%>"  method="post" class="user_info" enctype="multipart/form-data">
    <label for="type">Тип:</label><br>
    <input type="text" id="type" name="type" value="<%=estate.getType()%>"><br>
    <label for="address">Адрес:</label><br>
    <input type="text" id="address" name="address" value="<%=estate.getAddress()%>"><br>

    <%--<label for="status">Статус: </label><br>
    <%if (estate.getStatus().isSold()){%>
    <input type="text" id="status" name="status" value="продан"><br>
    <%} else {%>
    <input type="text" id="status" name="status" value="в собственности"><br>
    <%}%>--%>
    <div>
        Статус:
        <%if(estate.getStatus().isSold()){%>
        <div>
            <input type="radio" id="notsold" name="status" value="В собственности" onclick="hideSoldWindow()">
            <label for="notsold">В собственности</label><br>
        </div>
        <div>
            <input type="radio" id="issold" name="status" value="Продана" checked onclick="showSoldWindow()">
            <label for="issold">Продана</label><br>
        </div>
        <%}else{%>
        <div>
            <input type="radio" id="notsold1" name="status" value="В собственности" checked onclick="hideSoldWindow()">
            <label for="notsold">В собственности</label><br>
        </div>
        <div>
            <input type="radio" id="issold1" name="status" value="Продана" onclick="showSoldWindow()">
            <label for="issold">Продана</label><br>
        </div>
        <%}%>
    </div>
    <label for="purchaseDate">Дата покупки:</label><br>
    <input type="date" id="purchaseDate" name="purchaseDate" value="<%=estate.getStatus().getPurchaseDate()%>"><br>
    <label for="purchasePrice">Стоимость покупки:</label><br>
    <input type="text" id="purchasePrice" name="purchasePrice" value="<%=estate.getStatus().getPurchasePrice()%>"><br>
    <%if(estate.getStatus().isSold()){%>
        <label for="soldDate">Дата продажи:</label><br>
        <input type="date" id="soldDate" name="soldDate" value="<%=estate.getStatus().getSoldDate()%>"><br>
        <label for="soldPrice">Стоимость продажи:</label><br>
        <input type="text" id="soldPrice" name="soldPrice" value="<%=estate.getStatus().getSoldPrice()%>"><br><br>
    <%}else{%>
    <div id="sold">
        <label for="soldDate">Дата продажи:</label><br>
        <input type="date" id="soldDate1" name="soldDate"><br>
        <label for="soldPrice">Стоимость продажи:</label><br>
        <input type="text" id="soldPrice1" name="soldPrice"><br>
    </div>
    <%}%>
    <br><input  type="file" name="file" accept="image/jpeg,image/png"><br>
    <p class="add_button2">
        <button type="submit" name="but_add_estate">ОК </button>
    </p>
</form>
<script>
    document.getElementById("sold").hidden=true;
    function showSoldWindow(){
        document.getElementById("sold").hidden=false;
    }
    function hideSoldWindow(){
        document.getElementById("sold").hidden=true;
    }
</script>
</body>
</html>
