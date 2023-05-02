package control;

import control.db.DownloadService;
import control.db.UploadService;
import control.stat.DeltaValue;
import control.stat.StatisticsHandler;
import models.Model;
import models.dto.*;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private static final int UPLOAD_DELAY = 30;
    private Model model;
    private StatisticsHandler stat;
    private static final Controller INSTANCE = new Controller();


    public static synchronized Controller getInstance(){
        return INSTANCE;
    }
    public Controller() {
        try {
            model = new Model();
            stat = new StatisticsHandler(model);
            load();
            ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
            service.scheduleWithFixedDelay(new UploadSummoner(), 60, UPLOAD_DELAY, TimeUnit.SECONDS);
        }catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void load() throws SQLException {
        List<User> users = DownloadService.getAllUsers();
        for (User user: users){
            user.setInfo(DownloadService.getUserInfoById(user.getUid()));
            List<RealEstate> estates = DownloadService.getAllEstatesByUserId(user.getUid());
            for(RealEstate estate: estates){
                estate.setStatus(DownloadService.getEstateStatusById(estate.getEid()));

                List<Income> incomes= DownloadService.getAllIncomeByEstateId(estate.getEid());
                for (Income income: incomes){
                    estate.addIncome(income);
                }

                List<Outcome> outcomes= DownloadService.getAllOutcomeByEstateId(estate.getEid());
                for (Outcome outcome: outcomes){
                    estate.addOutcome(outcome);
                }

                user.addEstate(estate, estate.getStatus());
            }
            model.addUser(user, user.getInfo());
        }
    }


    public List<User> getAllUsers(){
        return model.getAllUsers();
    }
    public User getUser(int usid){
        return model.getUser(usid).clone();
    }
    public UserInfo getUserInfo(int usid){
        return model.getUser(usid).getInfo().clone();
    }
    public boolean hasPropertyRights(int usid, int eid){
        User user = model.getUser(usid);
        return user.getEstate(eid)!=null;
    }
    public boolean hasAccessIncome(int usid, int eid, int iid){
        if (!hasPropertyRights(usid, eid)){
            return false;
        }
        RealEstate estate = model.getUser(usid).getEstate(eid);
        return estate.getIncome(iid)!=null;
    }
    public boolean hasAccessOutcome(int usid, int eid, int oid){
        if (!hasPropertyRights(usid, eid)){
            return false;
        }
        RealEstate estate = model.getUser(usid).getEstate(eid);
        return estate.getOutcome(oid)!=null;
    }
    public List<RealEstate> getAllEstateUser(int usid){
        return model.getUser(usid).getAllEstates();
    }
    public RealEstate getRealEstate(int usid, int eid){
        return model.getUser(usid).getEstate(eid).clone();
    }
    public EstateStatus getEstateStatus(int usid, int eid){
        return model.getUser(usid).getEstate(eid).getStatus();
    }

    public List<Income> getAllIncomeEstate(int usid, int eid){
        return model.getUser(usid).getEstate(eid).getAllIncome();
    }
    public Income getIncome(int usid, int eid, int iid){
        return model.getUser(usid).getEstate(eid).getIncome(iid).clone();
    }
    public List<Outcome> getAllOutcomeEstate(int usid, int eid){
        return model.getUser(usid).getEstate(eid).getAllOutcome();
    }
    public Outcome getOutcome(int usid, int eid, int oid){
        return model.getUser(usid).getEstate(eid).getOutcome(oid).clone();
    }

    public void createUser(User user, UserInfo info) throws SQLException {
        if (user.getLogin()==null || user.getPassword() ==null){
            throw new IllegalArgumentException("Логин или пароль не заполнены");
        }
        if (info.getName() == null || info.getRegd()==null){
            throw new IllegalArgumentException("ФИО не заполнено");
        }
        if (!model.isLoginUnique(user.getLogin())){
            throw new IllegalArgumentException("Такой логин уже есть");
        }
        UploadService.createUser(user, info);
        model.addUser(user, info);
    }
    public void createEstate(RealEstate estate, EstateStatus status) throws SQLException {
        if (estate.getUsid()==null || estate.getAddress()==null || estate.getType()==null){
            throw new IllegalArgumentException("Заполнены не все поля");
        }
        if (status.getPurchasePrice()==null||status.getPurchaseDate()==null
        ||status.isSold()==null){
            throw new IllegalArgumentException("Заполнены не все поля");
        }
        if (status.isSold() &&(status.getSoldPrice()==null || status.getSoldDate()==null)){
            throw new IllegalArgumentException("Заполнены не все поля");
        }
        UploadService.createRealEstate(estate, status);
        model.getUser(estate.getUsid()).addEstate(estate, status);
    }
    public void createIncome(int usid,Income income) throws SQLException {
        if (income.getEid() == null|| income.getIdate() ==null||income.getName()==null||income.getValue()==null){
            throw new IllegalArgumentException("Заполнены не все поля");
        }
        UploadService.createIncome(income, model.getUser(usid).getEstate(income.getEid()).getStatus());
        model.getUser(usid).getEstate(income.getEid()).addIncome(income);
    }
    public void createOutcome(int usid,Outcome outcome) throws SQLException {
        if (outcome.getEid() == null|| outcome.getOdate() ==null||outcome.getName()==null||outcome.getValue()==null){
            throw new IllegalArgumentException("Заполнены не все поля");
        }
        UploadService.createOutcome(outcome, model.getUser(usid).getEstate(outcome.getEid()).getStatus());
        model.getUser(usid).getEstate(outcome.getEid()).addOutcome(outcome);
    }

    public void changeUser(User user, UserInfo info) throws SQLException {
        if (user != null && info != null) {
            if (user.getUid() != info.getUid()) {
                throw new IllegalArgumentException("УИД совпадают");
            }
        }
        UploadService.changeUser(user, info);

        if (user != null){
            User oldUser = model.getUser(user.getUid());
            if (user.getLogin()!=null){
                oldUser.setLogin(user.getLogin());
            }
            if(user.getPassword()!=null){
                oldUser.setPassword(user.getPassword());
            }
        }
        if(info!=null){
            UserInfo oldInfo = model.getUser(info.getUid()).getInfo();
            if (info.getName()!=null){
                oldInfo.setName(info.getName());
            }
            if(info.getDesc()!=null){
                oldInfo.setDesc(info.getDesc());
            }
        }
    }
    public void changeEstate(int usid, RealEstate estate, EstateStatus status) throws SQLException {
        if (status!=null && estate!=null){
            if(status.getEid() != estate.getEid()){
                throw new IllegalArgumentException("УИД недвижимости не совпадают");
            }
        }
        UploadService.changeRealEstate(estate, status);
        if (estate!=null){
           RealEstate oldEstate = model.getUser(usid).getEstate(estate.getEid());
           if (estate.getType()!=null){
               oldEstate.setType(estate.getType());
           }
           if(estate.getAddress()!=null){
               oldEstate.setAddress(estate.getAddress());
           }
        }
        if (status!=null){
            EstateStatus oldStatus = model.getUser(usid).getEstate(status.getEid()).getStatus();
            if (status.getPurchasePrice()!=null){
                oldStatus.setPurchasePrice(status.getPurchasePrice());
            }
            if(status.getPurchaseDate()!=null){
                oldStatus.setPurchaseDate(status.getPurchaseDate());
            }
            if(status.isSold()!=null){
                if (status.isSold()){
                    oldStatus.setSold(true);
                    oldStatus.setSoldPrice(status.getSoldPrice());
                    oldStatus.setSoldDate(status.getSoldDate());
                } else {
                    oldStatus.setSoldDate(null);
                    oldStatus.setSoldPrice(null);
                }
            } else if(oldStatus.isSold()){
                if (status.getSoldPrice()!=null) {
                    oldStatus.setSoldPrice(status.getSoldPrice());
                }
                if(status.getSoldDate()!=null){
                    oldStatus.setSoldDate(status.getSoldDate());
                }
            }
        }
    }
    public void changeIncome(int usid, Income income) throws SQLException {
        if (income.getEid()==null){
            throw new IllegalArgumentException("Заполнены не все поля");
        }
        UploadService.changeIncome(income, model.getUser(usid).getEstate(income.getEid()).getStatus());
        Income oldIncome = model.getUser(usid).getEstate(income.getEid()).getIncome(income.getIid());
        if (income.getValue()!=null){
            oldIncome.setValue(income.getValue());
        }
        if(income.getName()!=null){
            oldIncome.setName(income.getName());
        }
        if(income.getIdate()!=null){
            oldIncome.setIdate(income.getIdate());
        }
        if(income.getComment()!=null){
            oldIncome.setComment(income.getComment());
        }
    }
    public void changeOutcome(int usid, Outcome outcome) throws SQLException {
        if (outcome.getEid()==null){
            throw new IllegalArgumentException("Заполнены не все поля");
        }
        UploadService.changeOutcome(outcome, model.getUser(usid).getEstate(outcome.getEid()).getStatus());
        Outcome oldOutcome = model.getUser(usid).getEstate(outcome.getEid()).getOutcome(outcome.getOid());
        if (outcome.getValue()!=null){
            oldOutcome.setValue(outcome.getValue());
        }
        if(outcome.getName()!=null){
            oldOutcome.setName(outcome.getName());
        }
        if(outcome.getOdate()!=null){
            oldOutcome.setOdate(outcome.getOdate());
        }
        if(outcome.getOcomment()!=null){
            oldOutcome.setOcomment(outcome.getOcomment());
        }
    }

    public void deleteUser(int usid) throws SQLException {
        UploadService.deleteUserById(usid);
        model.deleteUser(usid);
    }
    public void deleteEstate(int usid, int eid) throws SQLException {
        UploadService.deleteRealEstateById(eid);
        model.getUser(usid).deleteEstate(eid);
    }
    public void deleteIncome(int usid, int eid, int iid) throws SQLException {
        UploadService.deleteIncomeById(iid);
        model.getUser(usid).getEstate(eid).deleteIncome(iid);
    }
    public void deleteOutcome(int usid, int eid, int oid) throws SQLException {
        UploadService.deleteOutcomeById(oid);
        model.getUser(usid).getEstate(eid).deleteOutcome(oid);
    }

    public List<DeltaValue> getUserStat(int usid, LocalDate from, LocalDate to, int period){
        if (period == StatisticsHandler.DAY){
            return stat.getUserDayStat(usid, from, to);
        }
        if (period == StatisticsHandler.MONTH){
            return stat.getUserMonthStat(usid, from, to);
        }
        throw new IllegalArgumentException("Неверный период");
    }
    public List<DeltaValue> getEstateStat(int usid, int eid, LocalDate from, LocalDate to, int period){
        if (period == StatisticsHandler.DAY){
            return stat.getEstateDayStat(usid, eid, from, to);
        }
        if (period == StatisticsHandler.MONTH){
            return stat.getEstateMonthStat(usid, eid, from, to);
        }
        throw new IllegalArgumentException("Неверный период");
    }
    public HashMap<String, Double> getUserTotal(int usid){
        return stat.getUserTotal(usid);
    }
    public HashMap<String, Double> getEstateTotal(int usid, int eid){
        return stat.getEstateTotal(usid, eid);
    }

    public String getEstateImage(int eid){
        File image = new File("images\\"+eid+".jpg");
        if (image.exists()){
            return "images\\"+eid+".jpg";
        } else {
            return null;
        }
    }

    public void createEstateImage(int eid, File tempFile) throws IOException {
        if(!tempFile.exists()||!tempFile.canRead()||!tempFile.isFile()){
            throw new IllegalArgumentException("Файл был поврежден");
        }
        File image = new File("images\\"+eid+".jpg");
        OutputStream imageStream = new FileOutputStream(image);
        InputStream tempStream = new FileInputStream(tempFile);
        byte[] buffer = new byte[tempStream.available()];
        tempStream.read(buffer);
        tempStream.close();
        imageStream.write(buffer);
        imageStream.close();
    }
    public void changeEstateImage(int eid, File tempFile) throws IOException {
        createEstateImage(eid, tempFile);
    }
    public boolean deleteEstateImage(int eid){
        File image = new File("images\\"+eid+".jpg");
        if (image.exists()){
            return image.delete();
        }
        return false;
    }
}
