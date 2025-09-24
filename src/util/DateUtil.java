package util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateUtil {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static Optional<LocalDate> parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(LocalDate.parse(dateStr.trim(), DEFAULT_FORMATTER));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DEFAULT_FORMATTER) : "";
    }

    public static boolean isDateInPast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    public static boolean isDateInFuture(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    public static LocalDate getNextMonthDate(LocalDate date) {
        return date.plusMonths(1);
    }

    public static LocalDate addMonths(LocalDate date, int months) {
        return date.plusMonths(months);
    }
}
