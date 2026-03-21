package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Excursion extends TourService {

    public Excursion() {
        super();
    }

    public Excursion(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to) {
        super(id, name, price, from, to);
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        if (participants > 10) {
            return getPrice().multiply(new BigDecimal("0.9"));
        }
        return getPrice();
    }

    @Override
    public String toString() {
        return "Excursion{" + "id=" + getId() + ", name=" + getName() + ", price=" + getPrice() + ", from=" + getFrom() + ", to=" + getTo() + "}";
    }
}