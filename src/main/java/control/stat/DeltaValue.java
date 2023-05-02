package control.stat;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

public abstract class DeltaValue {
    protected double income;
    protected double outcome;

    public LocalDate getDate() {
        return null;
    }

    public void setDate(LocalDate date){}

    public double getIncome() {
        return income;
    }
    public double getOutcome() {
        return outcome;
    }

    public void setIncome(double value) {
        this.income = value;
    }

    public void setOutcome(double value) {
        this.outcome = value;
    }

    public void addToIncome(double value){
        this.income+=value;
    }
    public void addToOutcome(double value){
        this.outcome+=value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeltaValue that = (DeltaValue) o;
        return Double.compare(that.income, income) == 0 && Double.compare(that.outcome, outcome) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(income, outcome);
    }

    @Override
    public String toString() {
        return "DeltaValue{" +
                "income=" + income +
                ", outcome=" + outcome +
                '}';
    }
}
