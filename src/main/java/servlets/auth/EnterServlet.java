package servlets.auth;

import control.Controller;
import models.dto.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("EnterServlet")
public class EnterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login=req.getParameter("login");
        String password=req.getParameter("password");
        try{
            if(login.equals("")||password.equals("")){
                throw new IllegalArgumentException("Поля должны быть заполнены.\nПопробуйте еще раз.");
            }
            for(User user: Controller.getInstance().getAllUsers()){
                if(login.equals(user.getLogin())){
                    if(password.hashCode()==user.getPassword()){
                        //все ок, берем айди и посылаем
                        req.getSession().setAttribute("user",user);
                        resp.sendRedirect("");

                    }
                }
            }
            throw new IllegalArgumentException("Данный пользователь не существует.\nПроверьте данные и попробуйте еще раз");
        }catch(IllegalArgumentException message){
            req.setAttribute("error",message.getMessage());
            getServletContext().getRequestDispatcher("/enterError").forward(req, resp);
        }

    }
}
