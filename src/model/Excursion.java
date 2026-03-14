package model;

import java.math.BigDecimal;

public class Excursion extends TourService {

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        if (participants > 10) {
            return getPrice().multiply(new BigDecimal("0.9"));
        }
        return getPrice();
    }
}
