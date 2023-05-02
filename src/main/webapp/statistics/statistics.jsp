<%@ page import="models.dto.RealEstate" %>
<%@ page import="control.Controller" %>
<%@ page import="models.dto.User" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: Яна
  Date: 23.03.2022
  Time: 13:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style><%@include file="/style.css"%></style>
    <title>Статистика</title>
    <script src="https://www.google.com/jsapi"></script>
    <script src="http://code.jquery.com/jquery-2.0.2.min.js"></script>
</head>
<body>
    <jsp:include page="/menu/menu.jsp"></jsp:include>
    <%
    User user = (User)request.getSession().getAttribute("user");
        int selectedEid = 0;
        RealEstate selectedEstate = null;
        if (request.getParameter("eid")!=null){
            selectedEid=Integer.parseInt(request.getParameter("eid"));
            selectedEstate = Controller.getInstance().getRealEstate(user.getUid(), selectedEid);
        }
    %>
    <div class="text2cols-item">
        <select class="choose_estate" id="choose_estate">
            <% if (selectedEid==0){%>
            <option value = "nullptr" disabled selected>Выберите недвижимость</option>
            <%}%>
           <%

               for(RealEstate estate: Controller.getInstance().getAllEstateUser(user.getUid())){
                   %>
                <option value="<%=estate.getEid()%>" <%=(estate.getEid()==selectedEid?"selected":"")%>><%=estate.getAddress()%></option>
                   <%
               }
           %>
        </select>
        <br>
        <h2 class="main_h1">Итог по недвижимости за все время</h2>
        <p class="result_values">
            <% try {
                HashMap<String, Double> estateTotal = Controller.getInstance().getEstateTotal(user.getUid(), selectedEid);
                %>
            Доходы от недвижимости:<%=estateTotal.get("income")%> рублей<br>
            Расходы от недвижимости:<%=estateTotal.get("outcome")%> рублей<br>
            Итог недвижимости: <%=estateTotal.get("income")-estateTotal.get("outcome")%> рублей<br>
            <%
            } catch (Exception e){
                %>
            Доходы от недвижимости: выберете недвижимость<br>
            Расходы от недвижимости: выберете недвижимость<br>
            Итог недвижимости: выберете недвижимость<br>
            <%
            }
            %>

        </p>
        <h2 class="main_h1"><%=selectedEid==0?"Выберете недвижимость":("Дневная статистика по "+selectedEstate.getType()+" на "+selectedEstate.getAddress())%></h2>
         <p class="result_values" >
            Выберете период: <br>
            От <input type="date" id="estate_day_from" onchange="saveValue(this)"> до <input type="date" id="estate_day_to" onchange="saveValue(this)">
    </p>

        <div  id="comeMonth_estate" class="bar_graph1"></div>
        <h2 class="main_h1"><%=selectedEid==0?"Выберете недвижимость":("Месячная статистика по "+selectedEstate.getType()+" на "+selectedEstate.getAddress())%></h2>
        <p class="result_values">

            Выберете период:<br>
            От <input type="date" id="estate_month_from" onchange="saveValue(this)"> до <input type="date" id="estate_month_to" onchange="saveValue(this)">

        </p>
        <div  id="comeYear_estate" class="bar_graph1"></div>

    </div>
    <div class="text2cols-item">
        <h2 class="main_h1">Итог по всем недвижимостям за все время</h2>
        <p class="result_values2">

            <%
                HashMap<String, Double> userTotal = Controller.getInstance().getUserTotal(user.getUid());
            %>
            Сумма доходов: <%=userTotal.get("income")%><br>
            Сумма расходов: <%=userTotal.get("outcome")%><br>
            Итог: <%=userTotal.get("income")-userTotal.get("outcome")%><br>
        </p>
        <h2 class="main_h1">Дневная статистика по всем вашим недвижимостям</h2>
        <p class="result_values">
            Выберете период:<br>
            От <input type="date" id="user_day_from" onchange="saveValue(this)"> до <input type="date" id="user_day_to" onchange="saveValue(this)">
        </p>
        <div  id="comeMonth" class="bar_graph1"></div>
        <h2 class="main_h1">Месячная статистика по всем вашим недвижимостям</h2>
        <p class="result_values">
            Выберете период:<br>
            От <input type="date" id="user_month_from" onchange="saveValue(this)"> до <input type="date" id="user_month_to" onchange="saveValue(this)">
        </p>
        <div  id="comeYear" class="bar_graph1"></div>
    </div>
<script>
    let select = document.getElementById('choose_estate');
    select.addEventListener('input', function(){
        window.location.replace('/realestatecontrol/statistics?eid='+select.options[select.selectedIndex].value);
    });
    function saveValue(e){
        var id = e.id;  // get the sender's id to save it .
        var val = e.value; // get the value.
        sessionStorage.setItem(id, val);// Every time user writing something, the sessionStorage's value will override .
    }

    //get the saved value function - return the value of "v" from sessionStorage.
    function getSavedValue  (v){
        if (!sessionStorage.getItem(v)) {
            return "";// You can change this to your defualt value.
        }
        return sessionStorage.getItem(v);
    }
    document.getElementById("user_day_from").value=getSavedValue("user_day_from");
    document.getElementById("user_day_to").value=getSavedValue("user_day_to");
    document.getElementById("user_month_from").value=getSavedValue("user_month_from");
    document.getElementById("user_month_to").value=getSavedValue("user_month_to");
    document.getElementById("estate_month_from").value=getSavedValue("estate_month_from");
    document.getElementById("estate_month_to").value=getSavedValue("estate_month_to");
    document.getElementById("estate_day_from").value=getSavedValue("estate_day_from");
    document.getElementById("estate_day_to").value=getSavedValue("estate_day_to");
</script>
    <script>
        function getStatUser(from, to, period){
            var data=null;
            $.ajax({
                async: false,
                url: 'statistics/getstat',
                method: 'GET',
                data: {
                    from: from,
                    to: to,
                    mode: 'user',
                    period: period
                },
                success: function (response){
                    data = JSON.parse(response);
                }
            });
            return data;
        }

        function getStatEstate(from, to, period, eid){
            var data=null;
            $.ajax({
                async: false,
                url: 'statistics/getstat',
                method: 'GET',
                data: {
                    from: from,
                    to: to,
                    eid: eid,
                    mode: 'estate',
                    period: period
                },
                success: function (response){
                    data =  JSON.parse(response);
                }
            });
            return data;
        }

    </script>
    <script>
        function drawUserDay() {
            google.load("visualization", "1", {packages: ["corechart"]});
            google.setOnLoadCallback(drawChart);

            function drawChart() {
                let from = null;
                let to = null;
                if (document.getElementById("user_day_from").value === '' && document.getElementById("user_day_to").value === '') {
                    to = new Date();
                    from = new Date();
                    from.setMonth(from.getMonth() - 1);
                } else if (document.getElementById("user_day_from").value === '') {
                    to = new Date(document.getElementById("user_day_to").value);
                    from = new Date(document.getElementById("user_day_to").value);
                    from.setMonth(from.getMonth() - 1);
                } else if (document.getElementById("user_day_to").value === '') {
                    from = new Date(document.getElementById("user_day_from").value);
                    to = new Date(document.getElementById("user_day_from").value);
                    to.setMonth(to.getMonth() + 1);
                } else {
                    from = new Date(document.getElementById("user_day_from").value);
                    to = new Date(document.getElementById("user_day_to").value);
                }
                if ((to-from)/(1000 * 3600 * 24)>31){
                     alert('Период отслеживания статистики не может превышать 31 дня');
                     return;
                }
                if ((to-from)<0){
                    alert('Дата окончания периода отслеживания статистики не может быть раньше даты начала отслеживания');
                    return;
                }
                from = from.getFullYear() + '-' + (from.getMonth() + 1 > 9 ? '' : '0') + (from.getMonth() + 1) + '-' + (from.getDate() > 9 ? '' : '0') + from.getDate();
                to = to.getFullYear() + '-' + (to.getMonth() + 1 > 9 ? '' : '0') + (to.getMonth() + 1) + '-' + (to.getDate() > 9 ? '' : '0') + to.getDate();
                let data = google.visualization.arrayToDataTable(getStatUser(from, to, 1));
                let options = {
                    title: 'Итоговые Доходы/Расходы за '+from+' - '+to,
                    hAxis: {title: 'Даты'},
                    vAxis: {title: 'Тыс. рублей'}
                };
                var chart = new google.visualization.ColumnChart(document.getElementById('comeMonth'));
                chart.draw(data, options);
            }
        }
    </script>
    <script>
        function drawEstateDay() {
            google.load("visualization", "1", {packages: ["corechart"]});
            google.setOnLoadCallback(drawChart);

            function drawChart() {
                let eid = document.getElementById('choose_estate').options[select.selectedIndex].value;
                let from = null;
                let to = null;
                if (document.getElementById("estate_day_from").value === '' && document.getElementById("estate_day_to").value === '') {
                    to = new Date();
                    from = new Date();
                    from.setMonth(from.getMonth() - 1);
                } else if (document.getElementById("estate_day_from").value === '') {
                    to = new Date(document.getElementById("estate_day_to").value);
                    from = new Date(document.getElementById("estate_day_to").value);
                    from.setMonth(from.getMonth() - 1);
                } else if (document.getElementById("estate_day_to").value === '') {
                    from = new Date(document.getElementById("estate_day_from").value);
                    to = new Date(document.getElementById("estate_day_from").value);
                    to.setMonth(to.getMonth() + 1);
                } else {
                    from = new Date(document.getElementById("estate_day_from").value);
                    to = new Date(document.getElementById("estate_day_to").value);
                }
                if ((to-from)/(1000 * 3600 * 24)>31){
                    alert('Период отслеживания статистики не может превышать 31 дня');
                    return;
                }
                if ((to-from)<0){
                    alert('Дата окончания периода отслеживания статистики не может быть раньше даты начала отслеживания');
                    return;
                }
                from = from.getFullYear() + '-' + (from.getMonth() + 1 > 9 ? '' : '0') + (from.getMonth() + 1) + '-' + (from.getDate() > 9 ? '' : '0') + from.getDate();
                to = to.getFullYear() + '-' + (to.getMonth() + 1 > 9 ? '' : '0') + (to.getMonth() + 1) + '-' + (to.getDate() > 9 ? '' : '0') + to.getDate();
                let data = google.visualization.arrayToDataTable(getStatEstate(from, to, 1, eid));
                let options = {
                    title: 'Итоговые Доходы/Расходы за '+from+' - '+to,
                    hAxis: {title: 'Даты'},
                    vAxis: {title: 'Тыс. рублей'}
                };
                var chart = new google.visualization.ColumnChart(document.getElementById('comeMonth_estate'));
                chart.draw(data, options);
            }
        }

    </script>

    <script>
        function drawUserMonth() {
            google.load("visualization", "1", {packages: ["corechart"]});
            google.setOnLoadCallback(drawChart);

            function drawChart() {
                let from = null;
                let to = null;
                if (document.getElementById("user_month_from").value === '' && document.getElementById("user_month_to").value === '') {
                    to = new Date();
                    from = new Date();
                    from.setFullYear(from.getFullYear() - 1);
                } else if (document.getElementById("user_month_from").value === '') {
                    to = new Date(document.getElementById("user_month_to").value);
                    from = new Date(document.getElementById("user_month_to").value);
                    from.setFullYear(from.getFullYear() - 1);
                } else if (document.getElementById("user_month_to").value === '') {
                    from = new Date(document.getElementById("user_month_from").value);
                    to = new Date(document.getElementById("user_month_from").value);
                    to.setFullYear(to.getFullYear() + 1);
                } else {
                    from = new Date(document.getElementById("user_month_from").value);
                    to = new Date(document.getElementById("user_month_to").value);
                }
                if ((to-from)/(1000 * 3600 * 24*7)>104){
                    alert('Период отслеживания статистики не может превышать 2 лет');
                    return;
                }
                if ((to-from)<0){
                    alert('Дата окончания периода отслеживания статистики не может быть раньше даты начала отслеживания');
                    return;
                }
                from = from.getFullYear() + '-' + (from.getMonth() + 1 > 9 ? '' : '0') + (from.getMonth() + 1) + '-' + (from.getDate() > 9 ? '' : '0') + from.getDate();
                to = to.getFullYear() + '-' + (to.getMonth() + 1 > 9 ? '' : '0') + (to.getMonth() + 1) + '-' + (to.getDate() > 9 ? '' : '0') + to.getDate();
                let data = google.visualization.arrayToDataTable(getStatUser(from, to, 2));
                let options = {
                    title: 'Итоговые Доходы/Расходы за '+from+' - '+to,
                    hAxis: {title: 'Даты'},
                    vAxis: {title: 'Тыс. рублей'}
                };
                var chart = new google.visualization.ColumnChart(document.getElementById('comeYear'));
                chart.draw(data, options);
            }
        }
    </script>
    <script>
        function drawEstateMonth() {
            google.load("visualization", "1", {packages: ["corechart"]});
            google.setOnLoadCallback(drawChart);

            function drawChart() {
                let eid = document.getElementById('choose_estate').options[select.selectedIndex].value;
                let from = null;
                let to = null;
                if (document.getElementById("estate_month_from").value === '' && document.getElementById("estate_month_to").value === '') {
                    to = new Date();
                    from = new Date();
                    from.setFullYear(from.getFullYear() - 1);
                } else if (document.getElementById("estate_month_from").value === '') {
                    to = new Date(document.getElementById("estate_month_to").value);
                    from = new Date(document.getElementById("estate_month_to").value);
                    from.setFullYear(from.getFullYear() - 1);
                } else if (document.getElementById("estate_month_to").value === '') {
                    from = new Date(document.getElementById("estate_month_from").value);
                    to = new Date(document.getElementById("estate_month_from").value);
                    to.setFullYear(to.getFullYear() + 1);
                } else {
                    from = new Date(document.getElementById("estate_month_from").value);
                    to = new Date(document.getElementById("estate_month_to").value);
                }
                if ((to-from)/(1000 * 3600 * 24*7)>104){
                    alert('Период отслеживания статистики не может превышать 2 лет');
                    return;
                }
                if ((to-from)<0){
                    alert('Дата окончания периода отслеживания статистики не может быть раньше даты начала отслеживания');
                    return;
                }
                from = from.getFullYear() + '-' + (from.getMonth() + 1 > 9 ? '' : '0') + (from.getMonth() + 1) + '-' + (from.getDate() > 9 ? '' : '0') + from.getDate();
                to = to.getFullYear() + '-' + (to.getMonth() + 1 > 9 ? '' : '0') + (to.getMonth() + 1) + '-' + (to.getDate() > 9 ? '' : '0') + to.getDate();
                let data = google.visualization.arrayToDataTable(getStatEstate(from, to, 2,eid));
                let options = {
                    title: 'Итоговые Доходы/Расходы за '+from+' - '+to,
                    hAxis: {title: 'Даты'},
                    vAxis: {title: 'Тыс. рублей'}
                };
                var chart = new google.visualization.ColumnChart(document.getElementById('comeYear_estate'));
                chart.draw(data, options);
            }
        }
        drawUserMonth();
        drawUserDay();
        drawEstateMonth();
        drawEstateDay();
        document.getElementById("user_day_from").addEventListener('change', function(){
            drawUserDay();
        });
        document.getElementById("user_day_to").addEventListener('change', function(){
            drawUserDay();
        });
        document.getElementById("user_month_from").addEventListener('change', function(){
            drawUserMonth();
        });
        document.getElementById("user_month_to").addEventListener('change', function(){
            drawUserMonth();
        });
        document.getElementById("estate_day_from").addEventListener('change', function(){
            drawEstateDay();
        });
        document.getElementById("estate_day_to").addEventListener('change', function(){
            drawEstateDay();
        });
        document.getElementById("estate_month_from").addEventListener('change', function(){
            drawEstateMonth();
        });
        document.getElementById("estate_month_to").addEventListener('change', function(){
            drawEstateMonth();
        });
    </script>
</body>
</html>
