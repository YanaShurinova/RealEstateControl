package control.db;

import models.dto.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

public class UploadService extends DbService{

    private static final Queue<Transaction> transactions= new LinkedList<>();

    public static synchronized void upload() throws SQLException{
        Exception exception = null;
        for (Transaction t: transactions){
            try {
                t.execute();
                connection.commit();
            }catch (SQLException|IllegalArgumentException e){
                connection.rollback();
                exception = e;
            }
        }
        transactions.clear();
        if (exception != null){
            if (exception instanceof SQLException){
                throw (SQLException) exception;
            } else {
                throw (IllegalArgumentException) exception;
            }
        }
    }

    public static void deleteUserById(int usid) throws SQLException {
        PreparedStatement prIncome = incomeDao.deleteAllIncomeByUserId(usid);
        PreparedStatement prOutcome = outcomeDao.deleteAllOutcomeByUserId(usid);
        PreparedStatement prStatus = estateDao.deleteAllEstateStatusByUserId(usid);
        PreparedStatement prEstate = estateDao.deleteAllRealEstateByUserID(usid);
        PreparedStatement prInfo = userDao.deleteUserInfoById(usid);
        PreparedStatement prUser = userDao.delete(usid);
        transactions.add(new Transaction(prIncome, prOutcome,
                prStatus, prEstate, prInfo, prUser));
    }

    public static void createUser(User user, UserInfo info) throws SQLException {
            Integer uid = userDao.getNewId();
            user.setUid(uid);
            info.setUid(uid);
            for (String login : userDao.getAllLogins()) {
                if (user.getLogin().equals(login)) {
                    throw new IllegalArgumentException("Такой логин уже существует");
                }
            }
            PreparedStatement prUser = userDao.create(user);
            PreparedStatement prInfo = userDao.createUserInfo(info);
            transactions.add(new Transaction(prUser, prInfo));

    }
    public static void changeUser(User user, UserInfo info) throws SQLException {

        PreparedStatement prUser = null;
        PreparedStatement prInfo = null;
        if (user != null && info != null) {
            if (user.getUid() != info.getUid()) {
                throw new IllegalArgumentException(" УИД пользователя не совпадают");
            }
        }

        if (user != null) {
            if (user.getUid() == null) {
                throw new IllegalArgumentException("УИД пользователя не может быть null");
            }
            prUser = userDao.update(user);
        }
        if (info != null) {
            if (info.getUid() == null) {
                throw new IllegalArgumentException("УИД информации о пользователе не может быть null");
            }
            prInfo = userDao.updateUserInfo(info);
        }

        if (prUser == null){
            transactions.add(new Transaction(prInfo));
        } else if (prInfo == null){
            transactions.add(new Transaction(prUser));
        } else {
            transactions.add(new Transaction(prUser, prInfo));
        }
    }

    public static void createRealEstate(RealEstate estate, EstateStatus status) throws SQLException {

        if (estate == null) {
            throw new IllegalArgumentException("Объект недвижимости не может быть null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Статус недвижимости не может быть null");
        }
        int eid = estateDao.getNewId();
        estate.setEid(eid);
        status.setEid(eid);
        PreparedStatement prEstate = estateDao.create(estate);
        PreparedStatement prStatus = estateDao.createEstateStatus(status);
        transactions.add(new Transaction(prEstate, prStatus));
    }
    public static void changeRealEstate(RealEstate estate, EstateStatus status) throws SQLException {
    PreparedStatement prEstate = null;
    PreparedStatement prStatus = null;
        if (estate != null && status != null) {
            if (estate.getEid() != status.getEid()) {
                throw new IllegalArgumentException("УИД недвижимостей не совпадают");
            }
        }

        if (estate != null) {
            if (estate.getEid() == null) {
                throw new IllegalArgumentException("УИД недвижимости не может быть null");
            }
            prEstate = estateDao.update(estate);
        }
        if (status != null) {
            if (status.getEid() == null) {
                throw new IllegalArgumentException("УИД статуса недвижимости не может быть null");
            }
            prStatus = estateDao.updateEstateStatus(status);
        }
        if (prEstate == null){
            transactions.add(new Transaction(prStatus));
        } else if (prStatus == null) {
            transactions.add(new Transaction(prEstate));
        } else {
            transactions.add(new Transaction(prEstate, prStatus));
        }
    }
    public static void deleteRealEstateById(int eid) throws SQLException {
        PreparedStatement prIncome = incomeDao.deleteAllIncomeByEstateId(eid);
        PreparedStatement prOutcome = outcomeDao.deleteAllOutcomeByEstateId(eid);
        PreparedStatement prStatus = estateDao.deleteEstateStatusById(eid);
        PreparedStatement prEstate = estateDao.delete(eid);
        transactions.add(new Transaction(prIncome, prOutcome, prStatus, prEstate));
    }

    public static void createIncome(Income income, EstateStatus parentEstate) throws SQLException {
        if (parentEstate.isSold()){
            if (income.getIdate().isAfter(parentEstate.getSoldDate())||
            income.getIdate().isBefore(parentEstate.getPurchaseDate())){
                throw new IllegalArgumentException("Дата дохода должна быть между датами покупки и продажи");
            }
        } else {
            if (income.getIdate().isBefore(parentEstate.getPurchaseDate())||
                    income.getIdate().isAfter(LocalDate.now())){
                throw new IllegalArgumentException("Дата дохода не может быть раньше покупки недвижимости, а также не может быть в будущем");
            }
        }
        int iid = incomeDao.getNewId();
        income.setIid(iid);
        PreparedStatement pr = incomeDao.create(income);
        transactions.add(new Transaction(pr));
    }
    public static void changeIncome(Income income, EstateStatus parentEstate) throws SQLException {
        if (income.getIid() == null){
            throw new IllegalArgumentException("Income id cannot be null");
        }

        if (income.getIdate() != null){
            if (parentEstate.isSold()){
                if (income.getIdate().isAfter(parentEstate.getSoldDate())||
                        income.getIdate().isBefore(parentEstate.getPurchaseDate())){
                    throw new IllegalArgumentException("Дата дохода должна быть между датами покупки и продажи");
                }
            } else {
                if (income.getIdate().isBefore(parentEstate.getPurchaseDate()) ||
                income.getIdate().isAfter(LocalDate.now())){
                    throw new IllegalArgumentException("Дата дохода не может быть раньше покупки недвижимости, а также не может быть в будущем");
                }
            }
        }

        PreparedStatement pr = incomeDao.update(income);
        transactions.add(new Transaction(pr));
    }
    public static void deleteIncomeById(int iid) throws SQLException {
        PreparedStatement pr = incomeDao.delete(iid);
        transactions.add(new Transaction(pr));
    }

    public static void createOutcome(Outcome outcome, EstateStatus parentEstate) throws SQLException {
        if (parentEstate.isSold()){
            if (outcome.getOdate().isAfter(parentEstate.getSoldDate())||
                    outcome.getOdate().isBefore(parentEstate.getPurchaseDate())){
                throw new IllegalArgumentException("Дата расхода должна быть между датами покупки и продажи");
            }
        } else {
            if (outcome.getOdate().isBefore(parentEstate.getPurchaseDate())||
                    outcome.getOdate().isAfter(LocalDate.now())){
                throw new IllegalArgumentException("Дата расхода не может быть раньше покупки недвижимости, а также не может быть в будущем");
            }
        }

        int oid = outcomeDao.getNewId();
        outcome.setOid(oid);
        PreparedStatement pr = outcomeDao.create(outcome);
        transactions.add(new Transaction(pr));
    }
    public static void changeOutcome(Outcome outcome, EstateStatus parentEstate) throws SQLException {
        if (outcome.getOid() == null){
            throw new IllegalArgumentException("Outcome id cannot be null");
        }

        if (outcome.getOdate() != null){
            if (parentEstate.isSold()){
                if (outcome.getOdate().isAfter(parentEstate.getSoldDate())||
                        outcome.getOdate().isBefore(parentEstate.getPurchaseDate())){
                    throw new IllegalArgumentException("Дата расхода должна быть между датами покупки и продажи");
                }
            } else {
                if (outcome.getOdate().isBefore(parentEstate.getPurchaseDate())||
                        outcome.getOdate().isAfter(LocalDate.now())){
                    throw new IllegalArgumentException("Дата расхода не может быть раньше покупки недвижимости, а также не может быть в будущем");
                }
            }
        }
        PreparedStatement pr =outcomeDao.update(outcome);
        transactions.add(new Transaction(pr));
    }
    public static void deleteOutcomeById(int oid) throws SQLException {
        PreparedStatement pr =incomeDao.delete(oid);
        transactions.add(new Transaction(pr));
    }

    /*public static double getUserIncomeTotalStat(int usid) throws SQLException {
        return statisticsHandler.getTotalIncomeByUserId(usid);
    }
    public static double getUserOutcomeTotalStat(int usid) throws SQLException {
        return statisticsHandler.getTotalOutcomeByUserId(usid);
    }
    public static double getUserTotalStat(int usid) throws SQLException{
        double result = getUserIncomeTotalStat(usid)-getUserOutcomeTotalStat(usid);
        return result;
    }
    public static double getEstateIncomeTotalStat(int eid) throws SQLException{
        return statisticsHandler.getTotalIncomeByEid(eid);
    }
    public static double getEstateOutcomeTotalStat(int eid) throws SQLException{
        return statisticsHandler.getTotalOutcomeByEid(eid);
    }
    public static double getEstateTotalStat(int eid) throws SQLException{
        double result = getEstateIncomeTotalStat(eid) - getEstateOutcomeTotalStat(eid);
        return result;
    }
    public static List<DayDeltaValue> getEstateIncomeDailyStat(int eid, LocalDate d1, LocalDate d2) throws SQLException {
        if (d1 == null || d2 == null){
            throw new IllegalArgumentException("Dates must not be null");
        }
        if (d1.isAfter(d2)||d1.equals(d2)){
            throw new IllegalArgumentException("First statistics date must be earlier than second one");
        }
        return statisticsHandler.getIncomeDaily(eid,d1,d2);
    }
    public static List<DayDeltaValue> getEstateOutcomeDailyStat(int eid, LocalDate d1, LocalDate d2) throws SQLException {
        if (d1 == null || d2 == null){
            throw new IllegalArgumentException("Dates must not be null");
        }
        if (d1.isAfter(d2)||d1.equals(d2)){
            throw new IllegalArgumentException("First statistics date must be earlier than second one");
        }
        return statisticsHandler.getOutcomeDaily(eid,d1,d2);
    }
    public static List<MonthDeltaValue> getEstateIncomeMonthlyStat(int eid, LocalDate d1, LocalDate d2) throws SQLException {
        if (d1 == null || d2 == null){
            throw new IllegalArgumentException("Dates must not be null");
        }
        if (d1.isAfter(d2)||d1.equals(d2)){
            throw new IllegalArgumentException("First statistics date must be earlier than second one");
        }
        if (d1.until(d2).getMonths()<=0){
            throw new IllegalArgumentException("Dates delta ust be lager than a month");
        }
        return statisticsHandler.getIncomeMonthly(eid,d1,d2);
    }
    public static List<MonthDeltaValue> getEstateOutcomeMonthlyStat(int eid, LocalDate d1, LocalDate d2) throws SQLException {
        if (d1 == null || d2 == null){
            throw new IllegalArgumentException("Dates must not be null");
        }
        if (d1.isAfter(d2)||d1.equals(d2)){
            throw new IllegalArgumentException("First statistics date must be earlier than second one");
        }
        if (d1.until(d2).getMonths()<=0){
            throw new IllegalArgumentException("Dates delta ust be lager than a month");
        }

        return statisticsHandler.getOutcomeMonthly(eid,d1, d2);
    }*/

}
