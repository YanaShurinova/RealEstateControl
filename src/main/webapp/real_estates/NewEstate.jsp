<%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 24.03.2022
  Time: 22:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style><%@include file="/style.css"%></style>
    <title>Добавление недвижимости</title>
</head>
<body>
<jsp:include page="/menu/menu.jsp"></jsp:include>
<form action="AddEstateServlet"  method="post" class="user_info" enctype="multipart/form-data">
    <label for="type">Тип:</label><br>
    <input type="text" id="type" name="typeEstate"><br>
    <label for="address">Адрес:</label><br>
    <input type="text" id="address" name="addressEstate"><br>
    <div>
        Статус:
        <div>
            <input type="radio" id="notsold" name="status" value="В собственности" onclick="hideSoldWindow()">
            <label for="notsold">В собственности</label><br>
        </div>
        <div>
            <input type="radio" id="issold" name="status" value="Продана" onclick="showSoldWindow()">
            <label for="issold">Продана</label><br>
        </div>
    </div>
    <label for="purchaseDate">Дата покупки:</label><br>
    <input type="date" id="purchaseDate" name="purchaseDate"><br>
    <label for="purchasePrice">Стоимость покупки:</label><br>
    <input type="text" id="purchasePrice" name="purchasePrice"><br>
    <div id="sold">
        <label for="soldDate">Дата продажи:</label><br>
        <input type="date" id="soldDate" name="soldDate"><br>
        <label for="soldPrice">Стоимость продажи:</label><br>
        <input type="text" id="soldPrice" name="soldPrice"><br>
    </div>
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
