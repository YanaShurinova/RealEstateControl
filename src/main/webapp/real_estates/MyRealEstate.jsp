<%@ page import="control.Controller" %>
<%@ page import="models.dto.User" %>
<%@ page import="models.dto.RealEstate" %>
<%@ page import="models.dto.Income" %>
<%@ page import="models.dto.Outcome" %><%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 24.03.2022
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <!-- Настройка viewport -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style><%@include file="/style.css"%></style>
    <title>Страница Недвижимости</title>
    <!-- Bootstrap CSS (Cloudflare CDN) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.6.1/css/bootstrap.min.css" integrity="sha512-T584yQ/tdRR5QwOpfvDfVQUidzfgc2339Lc8uBDtcp/wYu80d7jwBgAxbyMh0a9YM9F8N3tdErpFI8iaGx6x5g==" crossorigin="anonymous" referrerpolicy="no-referrer">
    <!-- jQuery (Cloudflare CDN) -->
    <script defer src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js" integrity="sha512-894YE6QWD5I59HgZOGReFYm4dnWc1Qt5NtvYSaNcOP+u1T9qYdvdihz0PPSiiqn/+/3e7Jo4EaG7TubfWGUrMQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <!-- Bootstrap Bundle JS (Cloudflare CDN) -->
    <script defer src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.6.1/js/bootstrap.min.js" integrity="sha512-UR25UO94eTnCVwjbXozyeVd6ZqpaAE9naiEUBK/A+QDbfSTQFhPGj5lOR6d8tsgbBk84Ggb5A3EkjsOgPRPcKA==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

</head>
<body>
<jsp:include page="/menu/menu.jsp"></jsp:include>
<%
    User user = (User)request.getSession().getAttribute("user");
    RealEstate estate = Controller.getInstance().getRealEstate(user.getUid(), Integer.parseInt(request.getParameter("eid")));
%>
    <p class="enter_and_reg" align="right">
        <a href = "ChangeEstate?eid=<%=estate.getEid()%>">Редактировать</a>
    </p>
    <img src="estates/image?eid=<%=estate.getEid()%>" align="left" width="240" height="240"></a>
    <p>
        <%=estate.getType()%>
    </p>
    <p>
        Адрес: <%=estate.getAddress()%>
    </p>
    <p>
        Статус:
        <%if (estate.getStatus().isSold()){%>
            продан
        <%} else {%>
            в собственности
        <%}%>
    </p>
    <p>
        Дополнительная информация:<br>
        Куплена: <%=estate.getStatus().getPurchaseDate()%> за <%=estate.getStatus().getPurchasePrice()%> руб.
        <%
        if (estate.getStatus().isSold()){
        %>
        Продана: <%=estate.getStatus().getSoldDate()%> за <%=estate.getStatus().getSoldPrice()%> руб.
        <%
        }
        %>
    </p>
    <Br>
    <ul class="nav nav-tabs">
        <li class="nav-item">
            <a class="nav-link active" data-toggle="tab" href="#income">Доходы</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="tab" href="#outcome">Расходы</a>
        </li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane fade show active" id="income">
            <form action="addSpending?eid=<%=estate.getEid()%>" method="post">
                <button type="submit" name="addIncome">Добавить</button>
            </form>
            <%for(Income income:Controller.getInstance().getAllIncomeEstate(user.getUid(), estate.getEid())){%>
            <div class="flex-container">
                <div class = "flex-item">
                    <p class="tab-pane-text"><%=income.getName()%><br> Дата: <%=income.getIdate()%> Сумма: <%=income.getValue()%>
                        <%if(income.getComment()!=null&&!income.getComment().isEmpty()){%>
                        <br>
                        Комментарий: <%=income.getComment()%>
                        <%}%>
                    </p>
                </div>
                <div class = flex-item>
                    <div class = "flex-container-col">
                        <div class = "flex-item">
                            <form action="editSpending?eid=<%=estate.getEid()%>&iid=<%=income.getIid()%>" method="post" class="changeBut">
                                <button type="submit" name="changeIncome">Изменить</button>
                            </form>
                        </div>
                        <div class = "flex-item">

                                <button type="submit" name="deleteIncome" value="<%=income.getIid()%>" onclick="deleteIncome(this)">Удалить</button>

                        </div>
                    </div>
                </div>
            </div>
            <hr>
            <%}%>
        </div>
        <div class="tab-pane fade" id="outcome">
            <form action="addOutcome?eid=<%=estate.getEid()%>" method="post">
                <button type="submit" name="addOutcome">Добавить</button>
            </form>
            <%for(Outcome outcome:Controller.getInstance().getAllOutcomeEstate(user.getUid(), estate.getEid())){%>
            <div class="flex-container">
                <div class = "flex-item">
                    <p class="tab-pane-text"><%=outcome.getName()%><br> Дата: <%=outcome.getOdate()%> Сумма: <%=outcome.getValue()%>
                    <%if(outcome.getOcomment()!=null&&!outcome.getOcomment().isEmpty()){%>
                        <br>
                        Комментарий: <%=outcome.getOcomment()%>
                        <%}%>
                    </p>
                </div>
                <div class = flex-item>
                    <div class = "flex-container-col">
                        <div class = "flex-item">
                            <form action="editSpending?eid=<%=estate.getEid()%>&oid=<%=outcome.getOid()%>" method="post" class="changeBut">
                                <button type="submit" name="changeOutcome">Изменить</button>
                            </form>
                        </div>
                        <div class = "flex-item">

                                <button type="submit" name="deleteOutcome" value="<%=outcome.getOid()%>" onclick="deleteOutcome(this)">Удалить</button>

                        </div>
                    </div>
                </div>
            </div>
            <hr>
            <%}%>
        </div>
    </div>
<input type="text" id="usid" name="usid" readonly hidden value="<%=user.getUid()%>">
<input type="text" id="eid" name="eid" readonly hidden value="<%=estate.getEid()%>">
<script>
    function deleteIncome(element){
        $.ajax({
            url:'DeleteIncomeServlet',
            method:'POST',
            data:{
                usid:$('#usid').val(),
                eid:$('#eid').val(),
                iid:element.value

            },
            success:function (response){
                if (response!=='success'){
                    alert(response);
                } else {
                    element.parentNode.parentNode.parentNode.parentNode.hidden=true;
                    element.parentNode.parentNode.parentNode.parentNode.nextSibling.nextSibling.hidden=true;
                }
            }
        });

    }
    function deleteOutcome(element){
        $.ajax({
            url:'DeleteOutcomeServlet',
            method:'POST',
            data:{
                usid:$('#usid').val(),
                eid:$('#eid').val(),
                oid:element.value
            },
            success:function (response){
                if (response!=='success'){
                    alert(response);
                } else {
                    element.parentNode.parentNode.parentNode.parentNode.hidden=true;
                    element.parentNode.parentNode.parentNode.parentNode.nextSibling.nextSibling.hidden=true;
                }
            }
        });
    }
</script>
</body>
</html>
