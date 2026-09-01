package drax;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    @Test
    public void parse_byeCommand_returnsByeCommand() {
        Parser.Command expected = new Parser.Command(
                Parser.Type.BYE, "", "", "", "");
        Parser.Command actual = Parser.parse("bye");
        assertEquals(expected, actual);
    }

    @Test
    public void parse_todoCommand_returnsTodoCommand() {
        Parser.Command expected = new Parser.Command(
                Parser.Type.TODO, "", "homework", "", "");
        Parser.Command actual = Parser.parse("todo homework");
        assertEquals(expected, actual);
    }

    @Test
    public void parse_deadlineCommand_returnsDeadlineCommand() {
        Parser.Command expected = new Parser.Command(
                Parser.Type.DEADLINE, "", "return book", "06/06/2026 1800", "");
        Parser.Command actual = Parser.parse("deadline return book /by 06/06/2026 1800");
        assertEquals(expected, actual);
    }

    @Test
    public void parse_deadlineWithoutDate_returnsTaskWithEmptyDate() {
        Parser.Command expected =
                new Parser.Command(
                        Parser.Type.DEADLINE,
                        "",
                        "return book",
                        "",
                        ""
                );
        Parser.Command actual = Parser.parse("deadline return book");
        assertEquals(expected, actual);
    }

    @Test
    public void parse_eventCommand_returnsEventCommand() {
        Parser.Command expected = new Parser.Command(
                Parser.Type.EVENT,
                "",
                "meeting",
                "20/08/2026 1200",
                "20/08/2026 1400");
        Parser.Command actual = Parser.parse("event meeting /from 20/08/2026 1200 /to 20/08/2026 1400");
        assertEquals(expected, actual);
    }

    @Test
    public void parse_eventWithoutDates_returnsTaskWithEmptyDates() {
        Parser.Command expected = new Parser.Command(
                Parser.Type.EVENT,
                "",
                "meeting",
                "",
                ""
        );
        Parser.Command actual = Parser.parse("event meeting");
        assertEquals(expected, actual);
    }

    @Test
    public void parse_unknownCommand_returnsUnknownCommand() {
        Parser.Command expected = new Parser.Command(
                Parser.Type.UNKNOWN, "blah", "", "", "");
        Parser.Command actual = Parser.parse("blah");
        assertEquals(expected, actual);
    }

    @Test
    public void parse_listCommand_returnsListCommand() {
        Parser.Command expected = new Parser.Command(
                Parser.Type.LIST, "", "", "", "");
        Parser.Command actual = Parser.parse("list");
        assertEquals(expected, actual);
    }

    @Test
    public void parse_unmarkCommand_returnsUnmarkCommandWithIndex() {
        Parser.Command expected =
                new Parser.Command(
                        Parser.Type.UNMARK, "2", "", "", "");
        Parser.Command actual = Parser.parse("unmark 2");
        assertEquals(expected, actual);
    }

    @Test
    public void parse_markCommand_returnsMarkCommandWithIndex() {
        Parser.Command expected = new Parser.Command(
                Parser.Type.MARK, "3", "", "", "");
        Parser.Command actual = Parser.parse("mark 3");
        assertEquals(expected, actual);
    }


    @Test
    public void parse_deleteCommand_returnsDeleteCommandWithIndex() {
        Parser.Command expected = new Parser.Command(
                Parser.Type.DELETE, "5", "", "", "");
        Parser.Command actual = Parser.parse("delete 5");
        assertEquals(expected, actual);
    }
}


