package control.stat;

import control.db.ConnectionMaster;
import models.Model;
import models.dto.Income;
import models.dto.Outcome;
import models.dto.RealEstate;
import models.dto.User;

import javax.validation.constraints.Null;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

public class StatisticsHandler extends ConnectionMaster {
    public static final int DAY = 1;
    public static final int MONTH = 2;
    private Model model;
    public StatisticsHandler(Model model){
        if (model == null){
            throw new NullPointerException("Tried to create stat with null model");
        }
        this.model=model;
    }

 public HashMap<String, Double> getUserTotal(int usid){
     User user = model.getUser(usid);
     if (user == null){
         throw new NullPointerException("Такого пользователя больше не существует");
     }
     HashMap<String, Double> map = new HashMap<>(3,1);
     double totalIncome = 0;
     double totalOutcome = 0;
     for (RealEstate estate: user.getAllEstates()){
         for (Income income: estate.getAllIncome()){
             totalIncome+=income.getValue();
         }
         for (Outcome outcome: estate.getAllOutcome()){
             totalOutcome+=outcome.getValue();
         }
     }
     map.put("income", totalIncome);
     map.put("outcome", totalOutcome);
     return map;
 }

    public HashMap<String, Double> getEstateTotal(int usid,int eid){
        RealEstate estate = model.getUser(usid).getEstate(eid);
        if(estate == null){
            throw new NullPointerException("Такого объекта недвижимости больше не существует");
        }
        HashMap<String, Double> map = new HashMap<>(3,1);
        double totalIncome = 0;
        double totalOutcome = 0;
        for (Income income: estate.getAllIncome()){
            totalIncome+=income.getValue();
        }
        for (Outcome outcome: estate.getAllOutcome()){
            totalOutcome+=outcome.getValue();
        }
        map.put("income", totalIncome);
        map.put("outcome", totalOutcome);
        return map;
    }

    public List<DeltaValue> getEstateDayStat(int usid, int eid, LocalDate from, LocalDate to){
        RealEstate estate = model.getUser(usid).getEstate(eid);
        if(estate == null){
            throw new NullPointerException("Такого объекта недвижимости больше не существует");
        }
        int n =(int)DAYS.between(from,to);
        List<DeltaValue> deltas = new ArrayList<DeltaValue>();
        for (int  i=0; i<=n;++i){
            deltas.add(new DayDeltaValue(from.plusDays(i), 0, 0));
        }
        for(Income income: estate.getAllIncome()){
            if (income.getIdate().isBefore(to.plusDays(1))&&income.getIdate().isAfter(from.minusDays(1))){
                deltas.get((int)DAYS.between(from, income.getIdate())).addToIncome(income.getValue());
            }
        }
        for(Outcome outcome: estate.getAllOutcome()){
            if (outcome.getOdate().isBefore(to.plusDays(1))&&outcome.getOdate().isAfter(from.minusDays(1))){
                deltas.get((int)DAYS.between(from, outcome.getOdate())).addToOutcome(outcome.getValue());
            }
        }
        return deltas;
    }
    public List<DeltaValue> getUserDayStat(int usid, LocalDate from, LocalDate to){
        User user = model.getUser(usid);
        if (user == null){
            throw new NullPointerException("Такого пользователя больше не существует");
        }
        int n =(int)DAYS.between(from,to);
        List<DeltaValue> deltas = new ArrayList<DeltaValue>();
        for (int  i=0; i<=n;++i){
            deltas.add(new DayDeltaValue(from.plusDays(i), 0, 0));
        }
        for (RealEstate estate: user.getAllEstates()) {
            for (Income income : estate.getAllIncome()) {
                if (income.getIdate().isBefore(to.plusDays(1)) && income.getIdate().isAfter(from.minusDays(1))) {
                    deltas.get((int) DAYS.between(from, income.getIdate())).addToIncome(income.getValue());
                }
            }
            for (Outcome outcome : estate.getAllOutcome()) {
                if (outcome.getOdate().isBefore(to.plusDays(1)) && outcome.getOdate().isAfter(from.minusDays(1))) {
                    deltas.get((int) DAYS.between(from, outcome.getOdate())).addToOutcome(outcome.getValue());
                }
            }
        }
        return deltas;
    }
    public List<DeltaValue> getEstateMonthStat(int usid, int eid, LocalDate from, LocalDate to){
        RealEstate estate = model.getUser(usid).getEstate(eid);
        if(estate == null){
            throw new NullPointerException("Такого объекта недвижимости больше не существует");
        }
        from = LocalDate.of(from.getYear(), from.getMonthValue(), 1);
        to = LocalDate.of(to.getYear(), to.getMonthValue()+1, 1).minusDays(1);
        int n =(int)MONTHS.between(from,to);
        List<DeltaValue> deltas = new ArrayList<DeltaValue>();
        for (int  i=0; i<=n;++i){
            deltas.add(new MonthDeltaValue(from.plusMonths(i), 0, 0));
        }
        for(Income income: estate.getAllIncome()){
            if (income.getIdate().isBefore(to.plusDays(1))&&income.getIdate().isAfter(from.minusDays(1))){
                deltas.get((int)MONTHS.between(from, income.getIdate())).addToIncome(income.getValue());
            }
        }
        for(Outcome outcome: estate.getAllOutcome()){
            if (outcome.getOdate().isBefore(to.plusDays(1))&&outcome.getOdate().isAfter(from.minusDays(1))){
                deltas.get((int)MONTHS.between(from, outcome.getOdate())).addToOutcome(outcome.getValue());
            }
        }
        return deltas;
    }
    public List<DeltaValue> getUserMonthStat(int usid, LocalDate from, LocalDate to){
        User user = model.getUser(usid);
        if (user == null){
            throw new NullPointerException("Такого пользователя больше не существует");
        }
        from = LocalDate.of(from.getYear(), from.getMonthValue(), 1);
        to = to.plusMonths(1);
        to = LocalDate.of(to.getYear(), to.getMonthValue(), 1).minusDays(1);
        int n =(int)MONTHS.between(from,to);
        List<DeltaValue> deltas = new ArrayList<DeltaValue>();
        for (int  i=0; i<=n;++i){
            deltas.add(new MonthDeltaValue(from.plusMonths(i), 0, 0));
        }
        for (RealEstate estate: user.getAllEstates()) {
            for (Income income : estate.getAllIncome()) {
                if (income.getIdate().isBefore(to.plusDays(1)) && income.getIdate().isAfter(from.minusDays(1))) {
                    deltas.get((int) MONTHS.between(from, income.getIdate())).addToIncome(income.getValue());
                }
            }
            for (Outcome outcome : estate.getAllOutcome()) {
                if (outcome.getOdate().isBefore(to.plusDays(1)) && outcome.getOdate().isAfter(from.minusDays(1))) {
                    deltas.get((int) MONTHS.between(from, outcome.getOdate())).addToOutcome(outcome.getValue());
                }
            }
        }
        return deltas;
    }
}
