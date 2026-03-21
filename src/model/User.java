package model;

import java.math.BigDecimal;

public class User {
    private String name;
    private Integer age;
    private BigDecimal loyaltyPoints;

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
        this.loyaltyPoints = BigDecimal.ZERO;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public BigDecimal getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void addLoyaltyPoints(BigDecimal points) {
        if (points != null && points.compareTo(BigDecimal.ZERO) > 0) {
            this.loyaltyPoints = this.loyaltyPoints.add(points);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{name=\"" + name + "\", age=\"" + age + "\", loyaltyPoints=\"" + loyaltyPoints + "\"}";
    }
}