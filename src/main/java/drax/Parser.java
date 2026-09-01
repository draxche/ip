package drax;

/** Converts a raw console line into a command type and its extracted arguments. */
public class Parser {
    /** The command categories understood by drax.Drax. */
    public enum Type {
        BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, UNKNOWN
    }

    /** Parsed command data consumed by drax.Drax's command handlers. */
    public record Command(Type type, String argument, String task, String firstDate, String secondDate) {
    }

    /**
     * Parses one console line without performing the command's side effects.
     *
     * @param input raw line entered by the user
     * @return the command type and extracted text fields
     */
    public static Command parse(String input) {
        if (input.equals("bye")) {
            return new Command(Type.BYE, "", "", "", "");
        }
        if (input.equals("list")) {
            return new Command(Type.LIST, "", "", "", "");
        }
        if (input.startsWith("mark ") || input.equals("mark")) {
            String taskNumber = input.length() == 4 ? "" : input.substring(5).trim();
            return new Command(Type.MARK, taskNumber, "", "", "");
        }
        if (input.startsWith("unmark ") || input.equals("unmark")) {
            String taskNumber = input.length() == 6 ? "" : input.substring(7).trim();
            return new Command(Type.UNMARK, taskNumber, "", "", "");
        }
        if (input.startsWith("todo ") || input.equals("todo")) {
            String task = input.length() == 4 ? "" : input.substring(5).trim();
            return new Command(Type.TODO, "", task, "", "");
        }
        if (input.startsWith("deadline ") || input.equals("deadline")) {
            if (input.equals("deadline")) {
                return new Command(Type.DEADLINE, "", "", "", "");
            }
            int split = input.indexOf(" /by ");
            return split == -1
                    ? new Command(Type.DEADLINE, "", input.substring(9).trim(), "", "")
                    : new Command(Type.DEADLINE, "", input.substring(9, split),
                            input.substring(split + 5).trim(), "");
        }
        if (input.startsWith("event ") || input.equals("event")) {
            if (input.equals("event")) {
                return new Command(Type.EVENT, "", "", "", "");
            }
            int fromIndex = input.indexOf(" /from ");
            int toIndex = input.indexOf(" /to ");
            if (fromIndex == -1 || toIndex == -1) {
                return new Command(Type.EVENT, "", input.substring(6).trim(), "", "");
            }
            return new Command(Type.EVENT, "", input.substring(6, fromIndex).trim(),
                    input.substring(fromIndex + 7, toIndex).trim(), input.substring(toIndex + 5).trim());
        }
        if (input.startsWith("delete ") || input.equals("delete")) {
            String taskNumber = input.length() == 6 ? "" : input.substring(7).trim();
            return new Command(Type.DELETE, taskNumber, "", "", "");
        }
        if (input.startsWith("find ") || input.equals("find")) {
            String keyword = input.length() == 4 ? "" : input.substring(5).trim();
            return new Command(Type.FIND, keyword, "", "", "");
        }
        return new Command(Type.UNKNOWN, input, "", "", "");
    }
}
