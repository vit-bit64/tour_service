package model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;

public class Excursion extends TourService {
    private String guidName;
    private Duration duration;
    private Difficulty difficulty;

    // Конструктор по умолчанию (без параметров)
    public Excursion() {
        super(); // вызов конструктора родителя по умолчанию
    }

    // Конструктор со всеми параметрами
    public Excursion(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to,
                     String guidName, Duration duration, Difficulty difficulty) {
        super(id, name, price, from, to); // вызов конструктора родителя
        this.guidName = guidName;
        this.duration = duration;
        this.difficulty = difficulty;
    }

    // Getters and Setters
    public String getGuidName() {
        return guidName;
    }

    public void setGuidName(String guidName) {
        this.guidName = guidName;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        BigDecimal total = getPrice()
                .multiply(BigDecimal.valueOf(participants))
                .multiply(difficulty.getMultiplier());

        if (participants > 10) {
            return total.multiply(BigDecimal.valueOf(0.9));
        }
        return total;
    }

    @Override
    public String toString() {
        String parentString = super.toString();
        String baseString = parentString.substring(0, parentString.length() - 1);

        String durationStr = "";
        if (duration != null) {
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            durationStr = hours + "ч " + minutes + "мин";
        }

        return baseString +
                ", guidName=\"" + guidName + "\"" +
                ", duration=" + (duration != null ? durationStr : "null") +
                ", difficulty=" + (difficulty != null ? difficulty.name() : "null") +
                "}";
    }
}