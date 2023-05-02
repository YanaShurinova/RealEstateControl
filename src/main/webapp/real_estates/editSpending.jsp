<%@ page import="models.dto.User" %>
<%@ page import="models.dto.RealEstate" %>
<%@ page import="control.Controller" %>
<%@ page import="models.dto.Income" %>
<%@ page import="models.dto.Outcome" %><%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 02.04.2022
  Time: 19:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Изменить доходы/расходы</title>
    <style><%@include file="/style.css"%></style>
</head>
<body>
<jsp:include page="/menu/menu.jsp"></jsp:include>
<%
    response.setCharacterEncoding("UTF-8");
    User user=(User)request.getSession().getAttribute("user");
    RealEstate estate=Controller.getInstance().getRealEstate(user.getUid(),Integer.parseInt(request.getParameter("eid")));
    Income income;
    Outcome outcome;
    if(request.getParameter("iid")!=null){
        income=Controller.getInstance().getIncome(user.getUid(),estate.getEid(),Integer.parseInt(request.getParameter("iid")) );
    %>
<form action = "EditIncomeServlet?eid=<%=estate.getEid()%>&iid=<%=income.getIid()%>" method="post" class="user_info spend_button" >
    <label for="type">Название:</label><br>
    <input type="text" id="type" name="name" value="<%=income.getName()%>"><br>
    <label for="summa"> Сумма: </label> <br>
    <input type="text" id="summa" name="value" value="<%=income.getValue()%>"><br>
    <label for="desc_come"> Описание: </label> <br>
    <%if (income.getComment()==null||income.getComment().isEmpty()){%>
    <input type="text" id="desc_come" name="description_come" value=""><br>
    <%}else{%>
    <input type="text" id="desc_come" name="description_come" value="<%=income.getComment()%>"><br>
    <%}%>
    <label for="date">Дата</label><br>
    <input type="date" name="date" id="date"value="<%=income.getIdate()%>">
    <p>
        <button type = "submit" name="submit3" onclick="changeIncome()" >ОК</button>
    </p>
</form>
<%}
    else{
        outcome=Controller.getInstance().getOutcome(user.getUid(),estate.getEid(),Integer.parseInt(request.getParameter("oid")));
%>
<form action = "EditOutcomeServlet?eid=<%=estate.getEid()%>&oid=<%=outcome.getOid()%>" method="post" class="user_info spend_button" >
    <label for="type">Название:</label><br>
    <input type="text" id="type2" name="name2" value="<%=outcome.getName()%>"><br>
    <label for="summa"> Сумма: </label> <br>
    <input type="text" id="summa2" name="value2" value="<%=outcome.getValue()%>"><br>
    <label for="desc_come"> Описание: </label> <br>
    <%if (outcome.getOcomment()==null||outcome.getOcomment().isEmpty()){%>
    <input type="text" id="desc_come" name="description_come2" value=""><br>
    <%}else{%>
    <input type="text" id="desc_come" name="description_come2" value="<%=outcome.getOcomment()%>"><br>
    <%}%>
    <label for="date">Дата</label><br>
    <input type="date" name="date2" id="date2"value="<%=outcome.getOdate()%>">
    <p>
        <button type = "submit" name="submit3" >ОК</button>
    </p>
</form>
<% }
%>
<script>
    function changeIncome(){
        $.ajax({
            url: 'EditIncomeServlet',
            method: 'POST',
            data: {
                usid: $('#usid').val(),
                eid:$('#eid').val(),
                iid:$('#iid').val()
            },
            success: function(response){
                if (response!='success'){
                    alert(response);
                }
            }

        });
    }
</script>
</body>
</html>
