package drax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class ScheduleDateTimeTest {
    @Test
    public void parse_isoDate_returnsDateAtMidnight() {
        LocalDateTime expected = LocalDateTime.of(2026, 12, 2, 0, 0);
        LocalDateTime actual = ScheduleDateTime.parse("2026-12-02");
        assertEquals(expected, actual);
    }

    @Test
    public void parse_isoDateTime_returnsDateAndTime() {
        LocalDateTime expected = LocalDateTime.of(2026, 12, 2, 18, 30);
        LocalDateTime actual = ScheduleDateTime.parse("2026-12-02T18:30");
        assertEquals(expected, actual);
    }
    @Test
    public void parse_commandDateAndTime_returnsDateAndTime() {
        LocalDateTime expected = LocalDateTime.of(2026, 12, 2, 18, 30);
        LocalDateTime actual = ScheduleDateTime.parse("02/12/2026 1830");
        assertEquals(expected, actual);
    }

    @Test
    public void parse_invalidDateFormat_exceptionThrown() {
        try {
            ScheduleDateTime.parse("2026-12-02 1830");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Please use a valid date and time: "
                    + "yyyy-MM-dd, yyyy-MM-ddTHH:mm, or d/M/yyyy HHmm.", e.getMessage());
        }
    }
    @Test
    public void parse_invalidDate_exceptionThrown() {
        try {
            ScheduleDateTime.parse("31/02/2026 1830");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Please use a valid date and time: "
                    + "yyyy-MM-dd, yyyy-MM-ddTHH:mm, or d/M/yyyy HHmm.", e.getMessage());
        }
    }

    @Test
    public void format_midnight_returnsDateOnly() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 12, 2, 0, 0);
        String actual = ScheduleDateTime.format(dateTime);
        assertEquals("Dec 02 2026", actual);
    }

    @Test
    public void format_nonMidnight_returnsDateAndTime() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 12, 2, 18, 30);
        String actual = ScheduleDateTime.format(dateTime);
        assertEquals("Dec 02 2026 6:30 PM", actual);
    }

}
