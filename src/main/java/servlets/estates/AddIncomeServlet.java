package servlets.estates;

import control.Controller;
import models.dto.Income;
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

@WebServlet("AddIncomeServlet")
public class AddIncomeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user=(User)req.getSession().getAttribute("user");
        RealEstate estate= Controller.getInstance().getRealEstate(user.getUid(),Integer.parseInt(req.getParameter("eid")));
        try{
            String name=req.getParameter("name");
            String desc=req.getParameter("description");
            LocalDate date=LocalDate.parse(req.getParameter("date"));
            if(!req.getParameter("value").matches("[^a-zA-Z\\[\\]\\?\\*\\-\\+\\\\\\n\\=]+")){
                throw new NumberFormatException("Вы ввели некорректные данные.\nПоле \"Сумма\" не являтся числом. ");
            }
            Double value=Double.parseDouble(req.getParameter("value"));
            if(name.isEmpty()){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nДанные поля \"Название\" должны быть заполнены. Попробуйте снова.\n");
            }
            if(!name.matches("[^\\[\\]\\?\\*\\-\\+\\\\\\n\\=]+")){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"Название\" не должно содержать\"[],?,*,-,+,\\,/,=\"." +
                        " Попробуйте снова.\n");
            }
            if(value<1){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"Сумма\" должно быть больше 1");
            }
            if(desc!=null){
                if(!desc.isEmpty()&&!(desc.matches("[^\\[\\]\\?\\*\\-\\+\\\\\\n\\=]+"))) {
                    throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"Описание\" не должно содержать\"[],?,*,-,+,\\,/,=\"." +
                            " Попробуйте снова.\n");
                }
            }
            Income income= new Income(null,estate.getEid(),date,name,value,desc);
            Controller.getInstance().createIncome(user.getUid(),income);
            resp.sendRedirect("estate?eid="+estate.getEid());

        }catch(NumberFormatException e){
            req.setAttribute("error",e.getMessage());
            getServletContext().getRequestDispatcher("/AddIncomeError?eid="+ estate.getEid()).forward(req, resp);
        }catch(IllegalArgumentException|SQLException message){
            req.setAttribute("error", message.getMessage());
            //resp.sendRedirect("/WrongEditSpending?eid=" + estate.getEid()+"&iid="+req.getParameter("iid"));
            getServletContext().getRequestDispatcher("/AddIncomeError?eid="+ estate.getEid()).forward(req, resp);
        }
    }
}
