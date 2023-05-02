package models.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class EstateStatus implements Serializable {
    private Integer eid;
    private Integer purchasePrice;
    private LocalDate purchaseDate;
    private Boolean isSold;
    private Integer soldPrice;
    private LocalDate soldDate;


    public EstateStatus(Integer eid, Integer purchasePrice, LocalDate purchaseDate, Boolean isSold, Integer soldPrice, LocalDate soldDate) {
        this.eid = eid;
        this.purchasePrice = purchasePrice;
        this.soldPrice = soldPrice;
        this.purchaseDate = purchaseDate;
        this.soldDate = soldDate;
        this.isSold = isSold;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(Integer soldPrice) {
        this.soldPrice = soldPrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDate soldDate) {
        this.soldDate = soldDate;
    }

    public Boolean isSold() {
        return isSold;
    }

    public void setSold(Boolean sold) {
        isSold = sold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstateStatus that = (EstateStatus) o;
        return eid == that.eid && purchasePrice == that.purchasePrice && soldPrice == that.soldPrice && isSold == that.isSold && Objects.equals(purchaseDate, that.purchaseDate) && Objects.equals(soldDate, that.soldDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eid, purchasePrice, soldPrice, purchaseDate, soldDate, isSold);
    }

    @Override
    public String toString() {
        return "EstateStatus{" +
                "eid=" + eid +
                ", purchasePrice=" + purchasePrice +
                ", soldPrice=" + soldPrice +
                ", purchaseDate=" + purchaseDate +
                ", soldDate=" + soldDate +
                ", isSold=" + isSold +
                '}';
    }

    public EstateStatus clone(){
        return new EstateStatus(eid, purchasePrice, purchaseDate, isSold, soldPrice, soldDate);
    }
}
