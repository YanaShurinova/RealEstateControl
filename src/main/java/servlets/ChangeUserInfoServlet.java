package servlets;

import control.Controller;
import models.dto.User;
import models.dto.UserInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
@WebServlet("ChangeUserInfoServlet")
public class ChangeUserInfoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user=(User)req.getSession().getAttribute("user");
        String name=req.getParameter("name");
        String password1=req.getParameter("Password1");
        String password2=req.getParameter("Password2");
        String desc=req.getParameter("depiction");
        try{
            if(name.isEmpty()){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nДанные поля \"ФИО\" должны быть заполнены. Попробуйте снова.\n");
            }
            if(!name.matches("[^\\[\\]\\?\\*\\-\\+\\\\\\n\\=]+")){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"ФИО\" не должно содержать\"[],?,*,-,+,\\,/,=\"." +
                        " Попробуйте снова.\n");
            }
            if(password1.isEmpty()||password2.isEmpty()){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоля \"Пароль\" и \"Повторите пароль\" должны быть не пустыми.\n");
            }
            if(!password1.equals(password2)){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоля \"Пароль\" и \"Повторите пароль\" должны совпадать.\n");
            }
            if(desc!=null){
                if(!desc.isEmpty()&&!(desc.matches("[^\\[\\]\\?\\*\\-\\+\\\\\\n\\=]+"))) {
                    throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"О себе\" не должно содержать\"[],?,*,-,+,\\,/,=\"." +
                            " Попробуйте снова.\n");
                }
            }
            UserInfo info=new UserInfo(user.getUid(),name,user.getInfo().getRegd(),desc);
            Controller.getInstance().changeUser(user,info);
            resp.sendRedirect("userinfo");
        }catch(IllegalArgumentException|SQLException message){
            req.setAttribute("error", message.getMessage());
            //resp.sendRedirect("/WrongEditSpending");
            getServletContext().getRequestDispatcher("/ChangeUserInfoError").forward(req, resp);
        }
    }
}
