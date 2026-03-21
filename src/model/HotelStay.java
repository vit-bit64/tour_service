package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HotelStay extends TourService {
    private int stars;
    private int nights;
    private RoomType roomType;


    public HotelStay() {
        super();
    }

    // Конструктор со всеми параметрами
    public HotelStay(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to,
                     int stars, int nights, RoomType roomType) {
        super(id, name, price, from, to); // вызов конструктора родителя
        this.stars = stars;
        this.nights = nights;
        this.roomType = roomType;
    }

    // Getters and Setters
    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        return getPrice()
                .multiply(BigDecimal.valueOf(participants))
                .multiply(getStarMultiplier())
                .multiply(getNightMultiplier());
    }

    private BigDecimal getStarMultiplier() {
        return switch (stars) {
            case 0 -> new BigDecimal("1.0");
            case 1 -> new BigDecimal("1.1");
            case 2 -> new BigDecimal("1.2");
            case 3 -> new BigDecimal("1.3");
            case 4 -> new BigDecimal("1.4");
            case 5 -> new BigDecimal("1.5");
            default -> BigDecimal.ZERO;
        };
    }

    private BigDecimal getNightMultiplier() {
        return switch (nights) {
            case 1 -> new BigDecimal("1.2");
            case 2 -> new BigDecimal("1.4");
            case 3 -> new BigDecimal("1.6");
            default -> new BigDecimal("2.0");
        };
    }

    @Override
    public String toString() {
        String parentString = super.toString();
        String baseString = parentString.substring(0, parentString.length() - 1);

        return baseString +
                ", stars=" + stars +
                ", nights=" + nights +
                ", roomType=" + (roomType != null ? roomType.name() : "null") +
                "}";
    }
}