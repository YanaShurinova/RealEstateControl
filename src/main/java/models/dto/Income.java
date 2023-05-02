package models.dto;

import java.time.LocalDate;
import java.util.Objects;

public class Income implements Comparable<Income>{
    private Integer iid;
    private Integer eid;
    private LocalDate idate;
    private String name;

    private Double value;
    private String icomment;

    public Income(Integer iid, Integer eid, LocalDate idate, String name, Double value, String comment) {
        this.iid = iid;
        this.eid = eid;
        this.idate = idate;
        this.name = name;
        this.value = value;
        this.icomment = comment;
    }

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public LocalDate getIdate() {
        return idate;
    }

    public void setIdate(LocalDate idate) {
        this.idate = idate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getComment() {
        return icomment;
    }

    public void setComment(String comment) {
        this.icomment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Income income = (Income) o;
        return iid == income.iid && eid == income.eid && Double.compare(income.value, value) == 0 && Objects.equals(idate, income.idate) && Objects.equals(name, income.name) && Objects.equals(icomment, income.icomment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iid, eid, idate, name, value, icomment);
    }

    @Override
    public String toString() {
        return "Income{" +
                "iid=" + iid +
                ", eid=" + eid +
                ", idate=" + idate +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", comment='" + icomment + '\'' +
                '}';
    }
    public Income clone(){
        return new Income(iid,eid, idate, name, value, icomment);
    }

    @Override
    public int compareTo(Income o) {
        return this.idate.compareTo(o.idate);
    }
}
