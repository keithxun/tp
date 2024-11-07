package hallpointer.address.model.session;

import static hallpointer.address.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a SessionDate.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class SessionDate {

    public static final String MESSAGE_CONSTRAINTS =
            "Dates should be in the format d MMM yyyy or dd MMM yyyy.\n"
                    + "Examples: 4 Sep 2024, 24 Sep 2024";

    // Desired date formats
    public static final DateTimeFormatter DATE_FORMATTER_1 = DateTimeFormatter.ofPattern("d MMM yyyy"); // e.g., 4 Sep 2024
    public static final DateTimeFormatter DATE_FORMATTER_2 = DateTimeFormatter.ofPattern("dd MMM yyyy"); // e.g., 24 Sep 2024

    public final LocalDate fullDate;

    /**
     * Constructs a {@code SessionDate}.
     *
     * @param date A valid date string.
     */
    public SessionDate(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        fullDate = parseDate(date);
    }

    /**
     * Returns true if a given string is a valid date and has the expected format.
     */
    public static boolean isValidDate(String test) {
        return parseDate(test) != null;
    }

    private static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER_1);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(dateString, DATE_FORMATTER_2);
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
    }

    @Override
    public String toString() {
        return fullDate.format(DATE_FORMATTER_2); // Assume dd MMM yyyy for consistent output
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SessionDate)) {
            return false;
        }

        SessionDate otherDate = (SessionDate) other;
        return fullDate.equals(otherDate.fullDate);
    }

    @Override
    public int hashCode() {
        return fullDate.hashCode();
    }
}
