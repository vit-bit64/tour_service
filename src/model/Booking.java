package model;

import exception.TourServiceValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private static final Random random = new Random();
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public Booking(Client client, Map<TourService, Integer> serviceParticipants)
            throws TourServiceValidationException {

        validateClient(client);
        validateServiceParticipants(serviceParticipants);

        this.client = client;
        this.serviceParticipants = new HashMap<>(serviceParticipants);
        this.bookingDate = LocalDate.now();
        this.bookingId = generateBookingId();
        this.status = Status.PENDING;
    }

    // Валидационные методы
    private void validateClient(Client client) throws TourServiceValidationException {
        if (client == null) {
            throw new TourServiceValidationException("Клиент не может быть null");
        }
    }

    private void validateServiceParticipants(Map<TourService, Integer> services)
            throws TourServiceValidationException {

        if (services == null || services.isEmpty()) {
            throw new TourServiceValidationException(
                    "Список услуг не может быть null или пустым"
            );
        }

        LocalDate today = LocalDate.now();
        for (Map.Entry<TourService, Integer> entry : services.entrySet()) {
            TourService service = entry.getKey();
            Integer participants = entry.getValue();

            validateServiceNotNull(service);
            validateServiceAvailability(service, today);
            validateParticipantsCount(service, participants);
            validateHotelStayCapacity(service, participants);
        }
    }

    private void validateServiceNotNull(TourService service) throws TourServiceValidationException {
        if (service == null) {
            throw new TourServiceValidationException("Обнаружена null услуга");
        }
    }

    private void validateServiceAvailability(TourService service, LocalDate date)
            throws TourServiceValidationException {
        if (!service.isAvailableOn(date)) {
            throw new TourServiceValidationException(
                    "Услуга \"" + service.getName() + "\" недоступна на " + date
            );
        }
    }

    private void validateParticipantsCount(TourService service, Integer participants)
            throws TourServiceValidationException {
        if (participants == null || participants <= 0) {
            throw new TourServiceValidationException(
                    "Количество участников для \"" + service.getName() +
                            "\" должно быть положительным, текущее: " + participants
            );
        }
    }

    private void validateHotelStayCapacity(TourService service, int participants)
            throws TourServiceValidationException {
        if (service instanceof HotelStay hotel) {
            int max = switch (hotel.getRoomType()) {
                case SINGLE -> 1;
                case DOUBLE -> 2;
                case FAMILY -> 4;
                case null -> 1;
            };
            if (participants > max) {
                throw new TourServiceValidationException(
                        "Для \"" + service.getName() + "\" макс. участников: " + max +
                                ", передано: " + participants
                );
            }
        }
    }

    private void checkPendingStatus() throws TourServiceValidationException {
        if (status != Status.PENDING) {
            throw new TourServiceValidationException(
                    "Операция доступна только в статусе PENDING, текущий: " + status
            );
        }
    }

    private String generateBookingId() {
        return "BK" + LocalDateTime.now().format(TIMESTAMP_FORMATTER) +
                (1000 + random.nextInt(9000));
    }

    // Основные методы
    public void addService(TourService service, int participants)
            throws TourServiceValidationException {
        checkPendingStatus();
        validateServiceNotNull(service);
        validateParticipantsCount(service, participants);
        validateServiceAvailability(service, LocalDate.now());
        validateHotelStayCapacity(service, participants);

        serviceParticipants.put(service, participants);
    }

    public void removeService(TourService service) throws TourServiceValidationException {
        checkPendingStatus();
        validateServiceNotNull(service);

        if (!serviceParticipants.containsKey(service)) {
            throw new TourServiceValidationException(
                    "Услуга \"" + service.getName() + "\" не найдена"
            );
        }

        serviceParticipants.remove(service);

        if (serviceParticipants.isEmpty()) {
            throw new TourServiceValidationException("Бронирование не может остаться без услуг");
        }
    }

    public void updateParticipants(TourService service, int participants)
            throws TourServiceValidationException {
        checkPendingStatus();

        if (!serviceParticipants.containsKey(service)) {
            throw new TourServiceValidationException(
                    "Услуга \"" + service.getName() + "\" не найдена"
            );
        }

        validateParticipantsCount(service, participants);
        validateHotelStayCapacity(service, participants);

        serviceParticipants.put(service, participants);
    }

    public void confirm() throws TourServiceValidationException {
        if (status != Status.PENDING) {
            throw new TourServiceValidationException(
                    "Подтверждение возможно только из PENDING, текущий: " + status
            );
        }
        status = Status.CONFIRMED;
    }

    public void complete() throws TourServiceValidationException {
        if (status != Status.CONFIRMED) {
            throw new TourServiceValidationException(
                    "Завершение возможно только из CONFIRMED, текущий: " + status
            );
        }

        client.addLoyaltyPoints(
                calculateTotalPrice()
                        .multiply(BigDecimal.valueOf(0.1))
                        .setScale(0, RoundingMode.DOWN)
                        .intValue()
        );

        status = Status.COMPLETED;
    }

    public void cancel() throws TourServiceValidationException {
        if (status != Status.PENDING && status != Status.CONFIRMED) {
            throw new TourServiceValidationException(
                    "Отмена возможна только из PENDING или CONFIRMED, текущий: " + status
            );
        }
        status = Status.CANCELLED;
    }

    public BigDecimal calculateTotalPrice() {
        return serviceParticipants.entrySet().stream()
                .map(e -> e.getKey().calculateTotalPrice(e.getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.ONE.subtract(client.getDiscountRate()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    // Getters
    public String getBookingId() { return bookingId; }
    public Client getClient() { return client; }
    public Map<TourService, Integer> getServiceParticipants() { return new HashMap<>(serviceParticipants); }
    public LocalDate getBookingDate() { return bookingDate; }
    public Status getStatus() { return status; }
    public int getTotalParticipants() {
        return serviceParticipants.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Booking{id='%s', date=%s, status=%s, client=%s, discount=%s%%, total=%.2f, services=[\n",
                bookingId, bookingDate, status, client.getFullName(),
                client.getDiscountRate().multiply(BigDecimal.valueOf(100)).setScale(0),
                calculateTotalPrice()));

        serviceParticipants.forEach((service, participants) ->
                sb.append(String.format("  %s x%d = %.2f\n",
                        service.getName(), participants,
                        service.calculateTotalPrice(participants)))
        );

        return sb.append("]}").toString();
    }
}