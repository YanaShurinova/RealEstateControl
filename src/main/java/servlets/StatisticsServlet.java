package servlets;

import control.Controller;
import control.stat.DeltaValue;
import control.stat.StatisticsHandler;
import models.dto.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@WebServlet("statistics/getstat")
public class StatisticsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate from = LocalDate.parse(req.getParameter("from"));
        LocalDate to = LocalDate.parse(req.getParameter("to"));
        String mode = req.getParameter("mode");
        int period = Integer.parseInt(req.getParameter("period"));
        List<DeltaValue> vals = null;
        int usid = ((User)req.getSession().getAttribute("user")).getUid();
        if (mode.equals("user")){
            vals = Controller.getInstance().getUserStat(usid, from,to,period);
        } else if (mode.equals("estate")){
            vals=Controller.getInstance().getEstateStat(usid, Integer.parseInt(req.getParameter("eid")), from, to, period);
        } else {
            throw new IllegalArgumentException("Неправильный тип запроса");
        }
        resp.setContentType("text/plain");
        Writer out =resp.getWriter();
        out.write("[");
        out.write("[\"Дата\",\"Доходы\",\"Расходы\"], ");
        if (period==StatisticsHandler.DAY) {
            for (int i = 0; i < vals.size() - 1; ++i) {
                out.write("[");
                out.write("\"" + stringifyDate(vals.get(i).getDate()) + "\", " + vals.get(i).getIncome() / 1000 + ", " + vals.get(i).getOutcome() / 1000);
                out.write("],");
            }
            out.write("[\"" + stringifyDate(vals.get(vals.size() - 1).getDate()) + "\", " + vals.get(vals.size() - 1).getIncome() / 1000 + ", " + vals.get(vals.size() - 1).getOutcome() / 1000);
        } else {
            for (int i = 0; i < vals.size() - 1; ++i) {
                out.write("[");
                out.write("\"" + stringifyDateMonth(vals.get(i).getDate()) + "\", " + vals.get(i).getIncome() / 1000 + ", " + vals.get(i).getOutcome() / 1000);
                out.write("],");
            }
            out.write("[\"" + stringifyDateMonth(vals.get(vals.size() - 1).getDate()) + "\", " + vals.get(vals.size() - 1).getIncome() / 1000 + ", " + vals.get(vals.size() - 1).getOutcome() / 1000);
        }
        out.write("]]");
        out.close();
    }
    private String stringifyDate(LocalDate date){

        return (date.getDayOfMonth()<10?"0":"")+date.getDayOfMonth()+"."+(date.getMonthValue()<10?"0":"")+date.getMonthValue()+"."+date.getYear();
    }
    private String stringifyDateMonth(LocalDate date){

        return date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"))+" "+date.getYear();
    }
}
