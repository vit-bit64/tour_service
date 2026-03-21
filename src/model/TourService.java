package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;

public abstract class TourService {
    private Integer y;
    private String name;
    private BigDecimal price;
    private LocalDate from;
    private LocalDate to;

    public TourService() {
    }

    public TourService(Integer y, String name, BigDecimal price, LocalDate from, LocalDate to) {
        this.y = y;
        this.name = name;
        this.price = price;
        this.from = from;
        this.to = to;
    }

    // Getters and Setters
    public Integer gety() {
        return y;
    }

    public void setId(Integer y) {
        this.y = y;
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

    public boolean isAvailableOn(LocalDate date) {
        return !(from.isAfter(date) || to.isBefore(date));
    }

    public abstract BigDecimal calculateTotalPrice(int participants);

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DecimalFormat priceFormatter = new DecimalFormat("#,##0.00");

        return getClass().getSimpleName() + "{" +
                "y=" + y +
                ", name=\"" + name + "\"" +
                ", price=" + (price != null ? priceFormatter.format(price) : "null") +
                ", from=" + (from != null ? from.format(dateFormatter) : "null") +
                ", to=" + (to != null ? to.format(dateFormatter) : "null") +
                "}";
    }
}