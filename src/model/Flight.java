package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Flight extends TourService {
    private String origin;
    private String destination;
    private String flightNumber;
    private boolean baggageIncluded;


    public Flight() {
        super();
    }

    public Flight(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to,
                  String origin, String destination, String flightNumber, boolean baggageIncluded) {
        super(id, name, price, from, to);
        this.origin = origin;
        this.destination = destination;
        this.flightNumber = flightNumber;
        this.baggageIncluded = baggageIncluded;
    }

    // Getters and Setters
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public boolean isBaggageIncluded() {
        return baggageIncluded;
    }

    public void setBaggageIncluded(boolean baggageIncluded) {
        this.baggageIncluded = baggageIncluded;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        var totalPrice = getPrice().multiply(BigDecimal.valueOf(participants));
        return baggageIncluded ? totalPrice.multiply(new BigDecimal("1.3")) : totalPrice;
    }

    @Override
    public String toString() {
        String parentString = super.toString();
        String baseString = parentString.substring(0, parentString.length() - 1);

        return baseString +
                ", origin=\"" + origin + "\"" +
                ", destination=\"" + destination + "\"" +
                ", flightNumber=\"" + flightNumber + "\"" +
                ", baggageIncluded=" + baggageIncluded +
                "}";
    }
}