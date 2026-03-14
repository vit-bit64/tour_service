package model;

import java.math.BigDecimal;

public class HotelStay extends TourService {
    private int stars;
    private int nights;
    private RoomType roomType;

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        return getPrice()
                .multiply(BigDecimal.valueOf(participants))
                .multiply(getStarsMultiply())
                .multiply(getNightsMultiply());
    }

    private BigDecimal getStarsMultiply() {
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

    private BigDecimal getNightsMultiply() {
        return switch (nights) {
            case 1 -> new BigDecimal("1.2");
            case 2 -> new BigDecimal("1.4");
            case 3 -> new BigDecimal("1.6");
            default -> new BigDecimal("2.0");
        };
    }
}
