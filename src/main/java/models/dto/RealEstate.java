package models.dto;

import java.io.Serializable;
import java.util.*;

public class RealEstate implements Serializable {
    private Integer eid;
    private Integer usid;
    private String type;
    private String address;

    private EstateStatus status;
    private HashMap<Integer, Income> incomes = new HashMap<>();
    private HashMap<Integer, Outcome> outcomes = new HashMap<>();

    public RealEstate(Integer eid, Integer uid, String type, String address) {
        this.eid = eid;
        this.usid = uid;
        this.type = type;
        this.address = address;
    }

    public RealEstate(Integer eid, Integer uid, String type, String address, EstateStatus status) {
        this.eid = eid;
        this.usid = uid;
        this.type = type;
        this.address = address;
        this.status=status;
    }

    public List<Income> getAllIncome(){
        ArrayList<Income> income = new ArrayList<Income>(incomes.values());
        Collections.sort(income);
        return income;
    }
    public void addIncome(Income income){
        income.setEid(eid);
        incomes.put(income.getIid(), income);
    }
    public Income getIncome(int iid){
        return incomes.get(iid);
    }
    public void deleteIncome(int iid){
        incomes.remove(iid);
    }
    public List<Outcome> getAllOutcome(){
        ArrayList<Outcome> outcome = new ArrayList<Outcome>(outcomes.values());
        Collections.sort(outcome);
        return outcome;
    }
    public void addOutcome(Outcome outcome){
        outcome.setEid(eid);
        outcomes.put(outcome.getOid(), outcome);
    }
    public Outcome getOutcome(int oid){
        return outcomes.get(oid);
    }
    public void deleteOutcome(int oid){
        outcomes.remove(oid);
    }


    public EstateStatus getStatus() {
        return status;
    }

    public void setStatus(EstateStatus status) {
        this.status = status;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getUsid() {
        return usid;
    }

    public void setUsid(Integer usid) {
        this.usid = usid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealEstate that = (RealEstate) o;
        return eid == that.eid && usid == that.usid && Objects.equals(type, that.type) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eid, usid, type, address);
    }

    @Override
    public String toString() {
        return "RealEstate{" +
                "eid=" + eid +
                ", uid='" + usid + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public RealEstate clone(){
        return new RealEstate(eid, usid, type, address, status);
    }
}
