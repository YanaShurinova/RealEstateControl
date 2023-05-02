package servlets.auth;

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
import java.time.LocalDate;
import java.util.regex.*;


@WebServlet("RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int usid=0;
        String name=req.getParameter("FIO");
        String login=req.getParameter("login");
        String password1=req.getParameter("password1");
        String password2=req.getParameter("password2");
        LocalDate ldate=LocalDate.now();
        try{
            if((name=="")||(login=="")){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nДанные поля должны быть заполнены. Попробуйте снова.\n");
            }
            if(!login.matches("[^\\[\\]\\?\\*\\-\\+\\\\\\/ \\n\\=]+")){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nЛогин не должен содержать\"[],?,*,-,+,\\,/,=\"." +
                        " Попробуйте снова.\n");
            }
            if(!password1.matches("[^\\[\\]\\?\\*\\-\\+\\\\\\/ \\n\\=]+")){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПароль не должен содержать\"[],?,*,-,+,\\,/,=\"." +
                        " Попробуйте снова.\n");
            }
            if(!password1.equals(password2)){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПароли не совпадают. Попробуйте снова.\n");
            }
            for(User user: Controller.getInstance().getAllUsers()){
                if(login.equals(user.getLogin())){
                    throw new IllegalArgumentException("Вы ввели некорректные данные.\nДанный логин занят. Попробуйте снова.\n");
                }
            }
            Controller.getInstance().createUser(new User(usid, login, password1.hashCode()), new UserInfo(usid, name, ldate, null));


        }catch(SQLException|IllegalArgumentException message){
            req.setAttribute("error", message.getMessage());
            getServletContext().getRequestDispatcher("/registrationError").forward(req, resp);
        }
        resp.sendRedirect("enter");

    }

}
