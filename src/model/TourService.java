package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class TourService {
    private Integer id;
    private String name;
    private BigDecimal price;
    private LocalDate from;
    private LocalDate to;

    public boolean isAvailableOn (LocalDate date) {
        return !(from.isAfter(date) || to.isBefore(date));
    }

    public abstract BigDecimal calculateTotalPrice(int participants);

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "TourService{name=\"" + name + "\"}";
    }
}