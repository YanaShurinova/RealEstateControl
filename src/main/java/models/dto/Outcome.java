package models.dto;

import java.time.LocalDate;
import java.util.Objects;

public class Outcome implements Comparable<Outcome>{
    private Integer oid;
    private Integer eid;
    private LocalDate odate;
    private String name;
    private Double value;
    private String ocomment;

    public Outcome(Integer oid, Integer eid, LocalDate odate, String name, Double value, String comment) {
        this.oid = oid;
        this.eid = eid;
        this.odate = odate;
        this.name = name;
        this.value = value;
        this.ocomment = comment;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public LocalDate getOdate() {
        return odate;
    }

    public void setOdate(LocalDate odate) {
        this.odate = odate;
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

    public String getOcomment() {
        return ocomment;
    }

    public void setOcomment(String ocomment) {
        this.ocomment = ocomment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Outcome outcome = (Outcome) o;
        return oid == outcome.oid && eid == outcome.eid && Double.compare(outcome.value, value) == 0 && Objects.equals(odate, outcome.odate) && Objects.equals(name, outcome.name) && Objects.equals(ocomment, outcome.ocomment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oid, eid, odate, name, value, ocomment);
    }

    @Override
    public String toString() {
        return "Outcome{" +
                "oid=" + oid +
                ", eid=" + eid +
                ", odate=" + odate +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", comment='" + ocomment + '\'' +
                '}';
    }
    public Outcome clone(){
        return new Outcome(oid,eid, odate, name, value, ocomment);
    }

    @Override
    public int compareTo(Outcome o) {
        return this.odate.compareTo(o.odate);
    }
}
