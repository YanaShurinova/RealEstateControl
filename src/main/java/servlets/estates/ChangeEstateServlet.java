package servlets.estates;

import control.Controller;
import models.dto.EstateStatus;
import models.dto.RealEstate;
import models.dto.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("ChangeEstateServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class ChangeEstateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            if(req.getParameter("type")==null||req.getParameter("type").isEmpty()){
                throw new NumberFormatException("Вы ввели некорректные данные.\nПоле \"Тип\" не должно быть пустым. ");
            }
            if(!req.getParameter("type").matches("[^\\[\\]\\?\\*\\-\\+\\\\\\n\\=]+")){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"Тип\" не должно содержать\"[],?,*,-,+,\\,/,=\"." +
                        " Попробуйте снова.\n");
            }
            String type=req.getParameter("type");
            if(req.getParameter("address")==null||req.getParameter("address").isEmpty()){
                throw new NumberFormatException("Вы ввели некорректные данные.\nПоле \"Адрес\" не должно быть пустым. ");
            }
            if(!req.getParameter("address").matches("[^\\[\\]\\?\\*\\-\\+\\\\\\n\\=]+")){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"Адрес\" не должно содержать\"[],?,*,-,+,\\,/,=\"." +
                        " Попробуйте снова.\n");
            }
            String address=req.getParameter("address");
            if(req.getParameter("status")==null){
                throw new NumberFormatException("Вы ввели некорректные данные.\nПоле \"Адрес\" не должно быть пустым. ");
            }
            String status=req.getParameter("status");
            if(req.getParameter("purchasePrice")==null||req.getParameter("purchasePrice").isEmpty()){
                throw new NumberFormatException("Вы ввели некорректные данные.\nПоле \"Стоимость покупки\" не должно быть пустым. ");
            }
            if(!req.getParameter("purchasePrice").matches("[0-9]*\\.?[0-9]+")){
                throw new NumberFormatException("Вы ввели некорректные данные.\nПоле \"Стоимость покупки\" не являтся числом. ");
            }
            Integer purchasePrice=Integer.parseInt(req.getParameter("purchasePrice"));
            if(req.getParameter("purchaseDate")==null){
                throw new NumberFormatException("Вы ввели некорректные данные.\nПоле \"Дата покупки\" не должно быть пустым. ");
            }
            LocalDate purchaseDate=LocalDate.parse(req.getParameter("purchaseDate"));
            if(purchasePrice<1){
                throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"Стоимость покупки\" должно быть больше 1");
            }
            RealEstate estate = new RealEstate(Integer.parseInt(req.getParameter("eid")),user.getUid(),type,address);
            EstateStatus statusEstate;
            if(status.equals("Продана")) {
                if(req.getParameter("soldPrice")==null||req.getParameter("soldDate")==null||req.getParameter("soldPrice").isEmpty()){
                    throw new IllegalArgumentException("Вы ввели некорректные данные.Если недвижимость продана, поля\"Стоимость продажи\" и \"Дата продажи\" должны быть заполнены.");
                }
                if(!req.getParameter("soldPrice").matches("[0-9]*\\.?[0-9]+")){
                    throw new NumberFormatException("Вы ввели некорректные данные.\nПоле \"Стоимость продажи\" не являтся числом. ");
                }
                Integer soldPrice=Integer.parseInt(req.getParameter("soldPrice"));
                if(soldPrice<1){
                    throw new IllegalArgumentException("Вы ввели некорректные данные.\nПоле \"Сумма\" должно быть больше 1");
                }
                LocalDate soldDate=LocalDate.parse(req.getParameter("soldDate"));
                if(soldDate.isBefore(purchaseDate)){
                    throw new IllegalArgumentException("Вы ввели некорректные данные. Дата продажи не может быть раньше даты покупки");
                }
                statusEstate = new EstateStatus(estate.getEid(), purchasePrice, purchaseDate,true,soldPrice,soldDate );
            }
            else{
                statusEstate=new EstateStatus(estate.getEid(),purchasePrice,purchaseDate,false,null,null);
            }
            Controller.getInstance().changeEstate(user.getUid(),estate,statusEstate);
            //избражение
            //Part file=req.getPart("file");
            //String filename=file.getSubmittedFileName();
            if(req.getPart("file").getSize()!=0){
                Path path= Paths.get("cash");
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                }
                Part filePart = req.getPart("file");
                File file= new File("cash/" +  LocalDate.now().getYear()+"."+LocalDate.now().getMonthValue()+"."+LocalDate.now().getDayOfMonth() +"."+ LocalDateTime.now().getHour()+"."+LocalDateTime.now().getMinute());

                for (Part part : req.getParts()) {
                    part.write(file.getAbsolutePath());
                }
                Controller.getInstance().changeEstateImage(estate.getEid(),file);
            }

            resp.sendRedirect("estate?eid="+estate.getEid());
        }catch (NumberFormatException message){
            req.setAttribute("error", message.getMessage());
            getServletContext().getRequestDispatcher("/ChangeEstateError").forward(req, resp);
        }
        catch (IllegalArgumentException| SQLException message){
            req.setAttribute("error", message.getMessage());
            getServletContext().getRequestDispatcher("/ChangeEstateError").forward(req, resp);
        }
    }
}
