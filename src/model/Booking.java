package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Booking {

    public enum Status {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }

    private final String bookingId;
    private final Client client;
    private final Map<TourService, Integer> serviceParticipants;
    private final LocalDate bookingDate;
    private Status status;

    public Booking(Client client, Map<TourService, Integer> serviceParticipants) {
        validate(client, serviceParticipants);

        this.client = client;
        this.serviceParticipants = new HashMap<>(serviceParticipants);
        this.bookingDate = LocalDate.now();
        this.status = Status.PENDING;
        this.bookingId = generateBookingId();
    }

    private void validate(Client client, Map<TourService, Integer> serviceParticipants) {
        if (client == null) {
            throw new IllegalArgumentException("Клиент не может быть null");
        }

        if (serviceParticipants == null || serviceParticipants.isEmpty()) {
            throw new IllegalArgumentException("Список услуг не может быть null или пустым");
        }

        LocalDate today = LocalDate.now();

        for (Map.Entry<TourService, Integer> entry : serviceParticipants.entrySet()) {
            TourService service = entry.getKey();
            Integer participants = entry.getValue();

            if (service == null) {
                throw new IllegalArgumentException("Услуга не может быть null");
            }

            if (participants == null || participants <= 0) {
                throw new IllegalArgumentException("Количество участников должно быть положительным для услуги: " + service);
            }

            if (!service.isAvailableOn(today)) {
                throw new IllegalArgumentException("Услуга недоступна на текущую дату: " + service);
            }

            if (service instanceof HotelStay) {
                HotelStay hotelStay = (HotelStay) service;
                int maxParticipants = getMaxParticipantsForHotel(hotelStay);
                if (participants > maxParticipants) {
                    throw new IllegalArgumentException(
                            "Для HotelStay с типом номера максимальное количество участников: " + maxParticipants +
                                    ", получено: " + participants
                    );
                }
            }
        }
    }

    private int getMaxParticipantsForHotel(HotelStay hotelStay) {
        RoomType roomType = hotelStay.getRoomType();
        switch (roomType) {
            case SINGLE: return 1;
            case DOUBLE: return 2;
            case FAMILY: return 4;
            default: return Integer.MAX_VALUE;
        }
    }

    private String generateBookingId() {
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Random random = new Random();
        int randomDigits = 1000 + random.nextInt(9000);
        return "BK" + timestamp + randomDigits;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Client getClient() {
        return client;
    }

    public Map<TourService, Integer> getServiceParticipants() {
        return new HashMap<>(serviceParticipants);
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public Status getStatus() {
        return status;
    }

    public void addService(TourService service, int participants) {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Нельзя добавить услугу, когда бронирование не в статусе PENDING");
        }

        if (service == null) {
            throw new IllegalArgumentException("Услуга не может быть null");
        }

        if (participants <= 0) {
            throw new IllegalArgumentException("Количество участников должно быть положительным");
        }

        if (!service.isAvailableOn(LocalDate.now())) {
            throw new IllegalArgumentException("Услуга недоступна на текущую дату");
        }

        if (service instanceof HotelStay) {
            int maxParticipants = getMaxParticipantsForHotel((HotelStay) service);
            if (participants > maxParticipants) {
                throw new IllegalArgumentException(
                        "Для HotelStay с типом номера максимальное количество участников: " + maxParticipants
                );
            }
        }

        serviceParticipants.put(service, participants);
    }

    public void removeService(TourService service) {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Нельзя удалить услугу, когда бронирование не в статусе PENDING");
        }

        if (service == null) {
            throw new IllegalArgumentException("Услуга не может быть null");
        }

        serviceParticipants.remove(service);
    }

    public void updateParticipants(TourService service, int participants) {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Нельзя обновить количество участников, когда бронирование не в статусе PENDING");
        }

        if (service == null) {
            throw new IllegalArgumentException("Услуга не может быть null");
        }

        if (!serviceParticipants.containsKey(service)) {
            throw new IllegalArgumentException("Услуга не найдена в бронировании");
        }

        if (participants <= 0) {
            throw new IllegalArgumentException("Количество участников должно быть положительным");
        }

        if (service instanceof HotelStay) {
            int maxParticipants = getMaxParticipantsForHotel((HotelStay) service);
            if (participants > maxParticipants) {
                throw new IllegalArgumentException(
                        "Для HotelStay с типом номера максимальное количество участников: " + maxParticipants
                );
            }
        }

        serviceParticipants.put(service, participants);
    }

    public BigDecimal calculateTotalWithDiscount() {
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<TourService, Integer> entry : serviceParticipants.entrySet()) {
            TourService service = entry.getKey();
            int participants = entry.getValue();
            total = total.add(service.calculateTotalPrice(participants));
        }

        BigDecimal discountMultiplier = client.getDiscountMultiplier();
        return total.multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);
    }

    public void confirm() {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Нельзя подтвердить бронирование: текущий статус " + status);
        }
        this.status = Status.CONFIRMED;
    }

    public void complete() {
        if (status != Status.CONFIRMED) {
            throw new IllegalStateException("Нельзя завершить бронирование: текущий статус " + status);
        }

        BigDecimal totalPrice = calculateTotalWithDiscount();
        int pointsToAdd = totalPrice.multiply(new BigDecimal("0.1")).intValue();

        client.addLoyaltyPoints(pointsToAdd);

        this.status = Status.COMPLETED;
    }

    public void cancel() {
        if (status != Status.PENDING && status != Status.CONFIRMED) {
            throw new IllegalStateException("Нельзя отменить бронирование: текущий статус " + status);
        }
        this.status = Status.CANCELLED;
    }

    @Override
    public String toString() {
        return "Booking{" + "bookingId='" + bookingId + '\'' + ", client=" + client + ", serviceParticipants=" + serviceParticipants + ", bookingDate=" + bookingDate + ", status=" + status + '}';
    }
}