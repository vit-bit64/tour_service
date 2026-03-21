package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.regex.Pattern;

public class Client {
    private final UUID clientId;
    private String fullName;
    private String email;
    private String phone;
    private String passportNumber;
    private int loyaltyPoints;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public Client(String fullName, String email, String phone, String passportNumber, int loyaltyPoints) {
        validateFullName(fullName);
        validateEmail(email);
        validatePhone(phone);
        validatePassportNumber(passportNumber);
        validateLoyaltyPoints(loyaltyPoints);

        this.clientId = UUID.randomUUID();
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passportNumber = passportNumber;
        this.loyaltyPoints = loyaltyPoints;
    }

    private void validateFullName(String fullName) {
        if (fullName == null) {
            throw new TourServiceValidationException("fullName=null (не может быть null)");
        }

        String[] words = fullName.trim().split("\\s+");
        if (words.length < 2) {
            throw new TourServiceValidationException("fullName=" + fullName + " (должно быть минимум 2 слова)");
        }

        for (String word : words) {
            if (word.length() < 2) {
                throw new TourServiceValidationException("fullName=" + fullName + " (каждое слово должно содержать минимум 2 символа)");
            }
        }
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new TourServiceValidationException("email=null (не может быть null)");
        }

        boolean isValid = EMAIL_PATTERN.matcher(email).matches();
        if (!isValid) {
            throw new TourServiceValidationException("email=" + email + " (не соответствует формату email)");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null) {
            throw new TourServiceValidationException("phone=null (не может быть null)");
        }

        if (!phone.startsWith("+")) {
            throw new TourServiceValidationException("phone=" + phone + " (первый символ должен быть +)");
        }

        String digits = phone.substring(1);
        if (!digits.matches("\\d+")) {
            throw new TourServiceValidationException("phone=" + phone + " (после + должны быть только цифры)");
        }

        if (digits.length() < 10 || digits.length() > 15) {
            throw new TourServiceValidationException("phone=" + phone + " (должно быть от 10 до 15 цифр после +)");
        }
    }

    private void validatePassportNumber(String passportNumber) {
        if (passportNumber == null) {
            throw new TourServiceValidationException("passportNumber=null (не может быть null)");
        }

        if (passportNumber.length() != 10) {
            throw new TourServiceValidationException("passportNumber=" + passportNumber + " (должен содержать ровно 10 символов)");
        }
    }

    private void validateLoyaltyPoints(int loyaltyPoints) {
        if (loyaltyPoints < 0) {
            throw new TourServiceValidationException("loyaltyPoints=" + loyaltyPoints + " (не может быть отрицательным)");
        }
    }

    public UUID getClientId() {
        return clientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        validateFullName(fullName);
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        validatePhone(phone);
        this.phone = phone;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        validatePassportNumber(passportNumber);
        this.passportNumber = passportNumber;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void addLoyaltyPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Нельзя добавить отрицательное количество баллов");
        }
        this.loyaltyPoints += points;
    }

    public BigDecimal getDiscountRate() {
        if (loyaltyPoints >= 0 && loyaltyPoints <= 99) {
            return BigDecimal.ZERO;
        } else if (loyaltyPoints <= 499) {
            return new BigDecimal("0.05");
        } else if (loyaltyPoints <= 999) {
            return new BigDecimal("0.10");
        } else if (loyaltyPoints <= 4999) {
            return new BigDecimal("0.15");
        } else {
            return new BigDecimal("0.20");
        }
    }

    public BigDecimal getDiscountMultiplier() {
        return BigDecimal.ONE.subtract(getDiscountRate()).setScale(2, RoundingMode.HALF_UP);
    }

    public String getMaskedPassportNumber() {
        if (passportNumber == null || passportNumber.length() < 4) {
            return "****";
        }
        String lastFour = passportNumber.substring(passportNumber.length() - 4);
        return "*".repeat(passportNumber.length() - 4) + lastFour;
    }

    @Override
    public String toString() {
        return "Client{" + "clientId=" + clientId + ", fullName='" + fullName + '\'' + ", email='" + email + '\'' + ", phone='" + phone + '\'' + ", passportNumber='" + getMaskedPassportNumber() + '\'' + ", loyaltyPoints=" + loyaltyPoints + ", discountRate=" + getDiscountRate() + '}';
    }
}