package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class TourService {
    private Integer id;
    private String name;
    private BigDecimal price;
    private LocalDate from;
    private LocalDate to;

    public TourService() {
    }

    public TourService(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.from = from;
        this.to = to;
    }

    public boolean isAvailableOn(LocalDate date) {
        return !(from.isAfter(date) || to.isBefore(date));
    }

    public abstract BigDecimal calculateTotalPrice(int participants);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "TourService{name=\"" + name + "\"}";
    }
}