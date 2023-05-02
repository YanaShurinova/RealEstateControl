<%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 17.05.2022
  Time: 19:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style><%@include file="/style.css"%></style>
    <title>Ошибка при изменении расходов</title>
    <script src="http://code.jquery.com/jquery-2.0.2.min.js"></script>
    <script>
        //Функция отображения PopUp
        function PopUpShow(){
            $("#popup1").show();
        }
        //Функция скрытия PopUp
        function PopUpHide(){
            $("#popup1").hide();
        }
    </script>
</head>
<body>

<jsp:include page="/real_estates/editSpending.jsp">
    <jsp:param name="eid" value='<%=request.getParameter("eid")%>'/>
    <jsp:param name="oid" value='<%=request.getParameter("oid")%>'/>
</jsp:include>
<div class="b-popup" id="popup1">
    <div class="b-popup-content" align="center">
        <br><br>
        ${error}<br><br>
        <a href="javascript:PopUpHide()">Закрыть</a>
    </div>
</div>
</body>
</html>

