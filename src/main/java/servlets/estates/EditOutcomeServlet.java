package servlets.estates;

import control.Controller;
import models.dto.Outcome;
import models.dto.RealEstate;
import models.dto.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("EditOutcomeServlet")
public class EditOutcomeServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user= (User)req.getSession().getAttribute("user");
        RealEstate estate= Controller.getInstance().getRealEstate(user.getUid(),Integer.parseInt(req.getParameter("eid")));
        Outcome outcome;
        String name2=req.getParameter("name2");


        String description2=req.getParameter("description_come2");
        LocalDate date2=LocalDate.parse(req.getParameter("date2"));
        try{
            if(!req.getParameter("value2").matches("[^a-zA-Z\\[\\]\\?\\*\\-\\+\\\\\\n\\=]+")){
                throw new NumberFormatException("Вы ввели некорректные данные.\nПоле \"Сумма\" не являтся числом. ");
            }
            Double value2=Double.parseDouble(req.getParameter("value2"));
            if(name2.isEmpty()){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nДанные поля \"Название\" должны быть заполнены. Попробуйте снова.\n");
            }
            if(!name2.matches("[^\\[\\]\\?\\*\\-\\+\\\\\\n\\=]+")){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"Название\" не должно содержать\"[],?,*,-,+,\\,/,=\"." +
                        " Попробуйте снова.\n");
            }
            if(value2<1){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"Сумма\" должно быть больше 1");
            }
            if(description2!=null){
                if(!description2.isEmpty()&&!(description2.matches("[^\\[\\]\\?\\*\\-\\+\\\\\\n\\=]+"))) {
                    throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"Описание\" не должно содержать\"[],?,*,-,+,\\,/,=\"." +
                            " Попробуйте снова.\n");
                }
            }
            if(req.getParameter("oid")!=null) {
                outcome = new Outcome(Integer.parseInt(req.getParameter("oid")), estate.getEid(), date2, name2, value2, description2);
                Controller.getInstance().changeOutcome(user.getUid(), outcome);
                resp.sendRedirect("estate?eid=" + estate.getEid());
            }
        }catch(NumberFormatException e){
            req.setAttribute("error",e.getMessage());
            getServletContext().getRequestDispatcher("/editOutcomeError?eid="+ estate.getEid()+"&oid="+req.getParameter("oid")).forward(req, resp);
        }catch( IllegalArgumentException|SQLException message){
            req.setAttribute("error", message.getMessage());
            getServletContext().getRequestDispatcher("/editOutcomeError?eid="+ estate.getEid()+"&oid="+req.getParameter("oid")).forward(req, resp);
        }

    }
}
