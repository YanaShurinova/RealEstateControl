package control.stat;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

public class MonthDeltaValue extends DeltaValue{
    private int year;
    private Month month;

    public MonthDeltaValue(){}
    public MonthDeltaValue(LocalDate date, double income, double outcome) {
        this.year = date.getYear();
        this.month = date.getMonth();
        this.income = income;
        this.outcome = outcome;
    }

    public LocalDate getDate() {
        return LocalDate.of(year, month.getValue(), 1);
    }

    public void setDate(LocalDate date){
        this.year = date.getYear();
        this.month = date.getMonth();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MonthDeltaValue that = (MonthDeltaValue) o;
        return year == that.year && month == that.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), year, month);
    }

    @Override
    public String toString() {
        return "MonthDeltaValue{" +
                "income=" + income +
                ", outcome=" + outcome +
                ", year=" + year +
                ", month=" + month +
                '}';
    }
}
