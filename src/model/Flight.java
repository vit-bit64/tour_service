package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Flight extends TourService {
    private String origin;
    private String destination;
    private String flightNumber;
    private boolean baggageInclude;

    public Flight() {
        super();
    }

    public Flight(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to,
                  String origin, String destination, String flightNumber, boolean baggageInclude) {
        super(id, name, price, from, to);
        this.origin = origin;
        this.destination = destination;
        this.flightNumber = flightNumber;
        this.baggageInclude = baggageInclude;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        var totalPrice = getPrice().multiply(BigDecimal.valueOf(participants));
        return baggageInclude ? totalPrice.multiply(new BigDecimal("1.3")) : totalPrice;
    }

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

    public boolean isBaggageInclude() {
        return baggageInclude;
    }

    public void setBaggageInclude(boolean baggageInclude) {
        this.baggageInclude = baggageInclude;
    }

    @Override
    public String toString() {
        return "Flight{" + "id=" + getId() + ", name=" + getName() + ", price=" + getPrice() + ", from=" + getFrom() + ", to=" + getTo() + ", origin='" + origin + '\'' + ", destination='" + destination + '\'' + ", flightNumber='" + flightNumber + '\'' + ", baggageInclude=" + baggageInclude + '}';
    }
}