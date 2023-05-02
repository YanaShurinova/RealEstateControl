package control.stat;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

public class DayDeltaValue extends DeltaValue{
    private LocalDate date;


    public DayDeltaValue(LocalDate date, double income, double outcome) {
        this.date = date;
        this.income=income;
        this.outcome=outcome;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DayDeltaValue that = (DayDeltaValue) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date);
    }

    @Override
    public String toString() {
        return "DayDeltaValue{" +
                "date=" + date +
                ", income=" + income +
                ", outcome=" + outcome +
                '}';
    }
}
