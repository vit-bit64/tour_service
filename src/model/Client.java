package model;

import exception.TourServiceValidationException;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;

public class Client {

    private UUID clientId;
    private String fullName;
    private String email;
    private String phone;
    private String passportNumber;
    private int loyaltyPoints;

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public Client(String fullName,
                  String email,
                  String phone,
                  String passportNumber,
                  int loyaltyPoints) throws TourServiceValidationException {

        ClientValidation.validateFullName(fullName);
        ClientValidation.validateEmail(email);
        ClientValidation.validatePhone(phone);
        ClientValidation.validatePassportNumber(passportNumber);
        ClientValidation.validateLoyaltyPoints(loyaltyPoints);

        this.clientId = UUID.randomUUID();
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passportNumber = passportNumber;
        this.loyaltyPoints = loyaltyPoints;
    }

    public BigDecimal getDiscountRate() {
        if (loyaltyPoints >= 5000) {
            return BigDecimal.valueOf(0.20);
        } else if (loyaltyPoints >= 1000) {
            return BigDecimal.valueOf(0.15);
        } else if (loyaltyPoints >= 500) {
            return BigDecimal.valueOf(0.10);
        } else if (loyaltyPoints >= 100) {
            return BigDecimal.valueOf(0.05);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public String getMaskedPassportNumber() {
        String lastFour = passportNumber.substring(passportNumber.length() - 4);
        return "**" + lastFour;
    }

    public UUID getClientId() {
        return clientId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", fullName=\"" + fullName + "\"" +
                ", email=\"" + email + "\"" +
                ", phone=\"" + phone + "\"" +
                ", passportNumber=\"" + getMaskedPassportNumber() + "\"" +
                ", loyaltyPoints=" + loyaltyPoints +
                "}";
    }

    public void addLoyaltyPoints(int pointsToAdd) {
    }
    static class ClientValidation {
        private int loyaltyPoints;

        private static void validateFullName(String fullName) throws TourServiceValidationException {
            if (fullName == null) {
                throw new TourServiceValidationException("fullName=null (не может быть null)");
            }

            String trimmed = fullName.trim();

            if (trimmed.isBlank()) {
                throw new TourServiceValidationException(
                        "fullName=" + fullName + " (не может быть пустым)"
                );
            }

            String[] words = trimmed.split("\\s+");

            if (words.length < 2) {
                throw new TourServiceValidationException(
                        "fullName=" + fullName + " (должно содержать минимум 2 слова)"
                );
            }

            for (String word : words) {
                if (word.length() < 2) {
                    throw new TourServiceValidationException(
                            "fullName=" + fullName + " (каждое слово должно содержать минимум 2 символа)"
                    );
                }
            }
        }

        private static void validateEmail(String email) throws TourServiceValidationException {
            if (email == null) {
                throw new TourServiceValidationException(
                        "email=null (не может быть null)"
                );
            }

            Pattern pattern = Pattern.compile(EMAIL_REGEX);

            if (!pattern.matcher(email).matches()) {
                throw new TourServiceValidationException(
                        "email=" + email + " (некорректный формат)"
                );
            }
        }

        private  static void validatePhone(String phone) throws TourServiceValidationException {
            if (phone == null || !phone.matches("^\\+[0-9]{10,15}$")) {
                throw new TourServiceValidationException(
                        "phone=" + phone + " (должен начинаться с + и содержать 10-15 цифр)"
                );
            }
        }

        private static void validatePassportNumber(String passportNumber) throws TourServiceValidationException  {
            if (passportNumber == null || passportNumber.length() != 10) {
                throw new TourServiceValidationException(
                        "passportNumber=" + passportNumber + " (должен быть 10 символов и не null)"
                );
            }
        }

        private static void validateLoyaltyPoints(int loyaltyPoints) throws TourServiceValidationException  {
            if (loyaltyPoints < 0) {
                throw new TourServiceValidationException(
                        "loyaltyPoints=" + loyaltyPoints + " (не может быть отрицательным)"
                );
            }
        }

        public void addLoyaltyPoints(int points) throws TourServiceValidationException {
            if (points < 0) {
                throw new TourServiceValidationException(
                        "points=" + points + " (не может быть отрицательным)"
                );
            }
            this.loyaltyPoints += points;
        }
    }

}