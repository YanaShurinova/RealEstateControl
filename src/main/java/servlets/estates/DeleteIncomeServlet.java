package servlets.estates;

import control.Controller;
import models.dto.User;
import servlets.auth.AccessDeniedException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;

@WebServlet("DeleteIncomeServlet")
public class DeleteIncomeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            int usid=Integer.parseInt(req.getParameter("usid"));
            int eid=Integer.parseInt(req.getParameter("eid"));
            int iid=Integer.parseInt(req.getParameter("iid"));
            if (!Controller.getInstance().hasPropertyRights(usid, eid)) {
                throw new AccessDeniedException("Недостаточно прав");
            }
            Controller.getInstance().deleteIncome(usid,eid,iid);
            Writer out = new OutputStreamWriter(resp.getOutputStream());
            resp.setContentType("text/plain");
            out.write("success");
            out.close();
        } catch (Exception e){
            e.printStackTrace();
            Writer out = new OutputStreamWriter(resp.getOutputStream());
            resp.setContentType("text/plain");
            out.write(e.getMessage());
            out.close();
        }
    }
}
