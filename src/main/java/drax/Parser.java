package drax;

/** Converts a raw console line into a command type and its extracted arguments. */
public class Parser {
    /**
     * The command categories understood by drax.Drax.
     */
    public enum Type {
        BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, UNKNOWN
    }

    /**
     * Parsed command data consumed by drax.Drax's command handlers.
     *
     * @param type      category of command to execute
     * @param argument  general command argument, such as a task number
     * @param task      task description for task-creation commands
     * @param startDate deadline or event start text
     * @param endDate   event end text
     */
    public record Command(Type type, String argument, String task, String startDate, String endDate) {
    }

    /**
     * Parses one console line without performing the command's side effects.
     *
     * @param input raw line entered by the user
     * @return the command type and extracted text fields
     */
    public static Command parse(String input) {
        if (input.equals("bye")) {
            return parseBye();
        }
        if (input.equals("list")) {
            return parseList();
        }
        if (input.startsWith("mark ") || input.equals("mark")) {
            return parseMark(input);
        }
        if (input.startsWith("unmark ") || input.equals("unmark")) {
            return parseUnmark(input);
        }
        if (input.startsWith("todo ") || input.equals("todo")) {
            return parseTodo(input);
        }
        if (input.startsWith("deadline ") || input.equals("deadline")) {
            return parseDeadline(input);
        }
        if (input.startsWith("event ") || input.equals("event")) {
            return parseEvent(input);
        }
        if (input.startsWith("delete ") || input.equals("delete")) {
            return parseDelete(input);
        }
        if (input.startsWith("find ") || input.equals("find")) {
            return parseFind(input);
        }

        return parseUnknown(input);
    }

    /**
     * Parses the bye command.
     */
    private static Command parseBye() {
        return new Command(Type.BYE, "", "", "", "");
    }

    /**
     * Parses the list command.
     */
    private static Command parseList() {
        return new Command(Type.LIST, "", "", "", "");
    }

    /**
     * Parses the mark command and its optional task number.
     */
    private static Command parseMark(String input) {
        String taskNumber = extractArgument(input, 4);
        return new Command(Type.MARK, taskNumber, "", "", "");
    }

    /**
     * Parses the unmark command and its optional task number.
     */
    private static Command parseUnmark(String input) {
        String taskNumber = extractArgument(input, 6);
        return new Command(Type.UNMARK, taskNumber, "", "", "");
    }

    /**
     * Parses the todo command and its task description.
     */
    private static Command parseTodo(String input) {
        String task = extractArgument(input, 4);
        return new Command(Type.TODO, "", task, "", "");
    }

    /**
     * Parses the deadline command and its optional due date.
     */
    private static Command parseDeadline(String input) {
        if (input.equals("deadline")) {
            return new Command(Type.DEADLINE, "", "", "", "");
        }

        int splitStringIndex = input.indexOf(" /by ");
        boolean hasDeadline = splitStringIndex != -1;
        if (!hasDeadline) {
            String task = input.substring(9).trim();
            return new Command(Type.DEADLINE, "", task, "", "");
        }

        String task = input.substring(9, splitStringIndex);
        String endDate = input.substring(splitStringIndex + 5).trim();
        return new Command(Type.DEADLINE, "", task, "", endDate);
    }

    /**
     * Parses the event command and its optional start and end dates.
     */
    private static Command parseEvent(String input) {
        if (input.equals("event")) {
            return new Command(Type.EVENT, "", "", "", "");
        }

        int fromIndex = input.indexOf(" /from ");
        int toIndex = input.indexOf(" /to ");
        boolean hasDates = fromIndex != -1 && toIndex != -1;
        if (!hasDates) {
            String task = input.substring(6).trim();
            return new Command(Type.EVENT, "", task, "", "");
        }
        String task = input.substring(6, fromIndex).trim();
        String startDate = input.substring(fromIndex + 7, toIndex).trim();
        String endDate = input.substring(toIndex + 5).trim();
        return new Command(Type.EVENT, "", task, startDate, endDate);
    }

    /**
     * Parses the delete command and its optional task number.
     */
    private static Command parseDelete(String input) {
        String taskNumber = extractArgument(input, 6);
        return new Command(Type.DELETE, taskNumber, "", "", "");
    }

    /**
     * Parses the find command and its optional keyword.
     */
    private static Command parseFind(String input) {
        String keyword = extractArgument(input, 4);
        return new Command(Type.FIND, keyword, "", "", "");
    }

    /**
     * Parses a command that does not match any known command keyword.
     */
    private static Command parseUnknown(String input) {
        return new Command(Type.UNKNOWN, input, "", "", "");
    }

    /**
     * Extracts the text that follows a command keyword, omitting the separating space when present.
     *
     * @param input         complete command entered by the user
     * @param commandLength length of the command keyword
     * @return the trimmed argument, or an empty string when no argument was supplied
     */
    private static String extractArgument(String input, int commandLength) {
        boolean hasArgument = input.length() > commandLength;
        if (!hasArgument) {
            return "";
        }
        String argument = input.substring(commandLength + 1).trim();
        return argument;
    }

}
