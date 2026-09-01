package drax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Converts the date and time text used by commands into values that can be compared and formatted reliably.
 */
public final class ScheduleDateTime {
    private static final DateTimeFormatter COMMAND_DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("d/M/uuuu HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd uuuu");
    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd uuuu h:mm a");
    private static final String ACCEPTED_FORMATS = "yyyy-MM-dd, yyyy-MM-ddTHH:mm, or d/M/yyyy HHmm";

    private ScheduleDateTime() {
    }

    /**
     * Parses an ISO date, an ISO date-time, or a day/month/year time such as {@code 2/12/2019 1800}.
     * A date without a time is stored at midnight.
     *
     * @param text text supplied after a date command flag
     * @return the parsed date and time
     * @throws IllegalArgumentException if the text is not in a supported format
     */
    public static LocalDateTime parse(String text) {
        try {
            if (text.contains(" ")) {
                return LocalDateTime.parse(text, COMMAND_DATE_TIME_FORMATTER);
            }
            if (text.contains("T")) {
                return LocalDateTime.parse(text);
            }
            return LocalDate.parse(text).atStartOfDay();
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Please use a valid date and time: " + ACCEPTED_FORMATS + ".");
        }
    }

    /**
     * Formats a saved date and time for display. Midnight values are shown as dates only.
     *
     * @param dateTime date and time to display
     * @return a readable date or date and time
     */
    public static String format(LocalDateTime dateTime) {
        if (dateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return dateTime.format(DISPLAY_DATE_FORMATTER);
        }
        return dateTime.format(DISPLAY_DATE_TIME_FORMATTER);
    }
}
