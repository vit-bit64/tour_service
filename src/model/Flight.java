package model;

import java.math.BigDecimal;

public class Flight extends TourService {
    private String origin;
    private String destination;
    private String flightNumber;
    private boolean baggageInclude;

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        var totalPrice = getPrice().multiply(BigDecimal.valueOf(participants));
        return baggageInclude ? totalPrice.multiply(new BigDecimal("1.3")) : totalPrice;
    }

    @Override
    public String toString() {
        return "Flight{origin=\"" + origin + "\", destination=\"" + destination + "\", flightNumber=\"" + flightNumber + "\"}";
    }
}