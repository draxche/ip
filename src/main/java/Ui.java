import java.util.List;
import java.util.Scanner;

/** Handles Drax's console input and output so the command loop stays focused on application logic. */
public class Ui {
    private final Scanner reader;

    /** Creates a console UI connected to standard input. */
    public Ui() {
        this.reader = new Scanner(System.in);
    }

    /** Displays Drax's greeting banner. */
    public void showGreeting() {
        System.out.println("""
                ██████╗ ██████╗  █████╗ ██╗  ██╗
                ██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
                ██║  ██║██████╔╝███████║ ╚███╔╝
                ██║  ██║██╔══██╗██╔══██║ ██╔██╗
                ██████╔╝██║  ██║██║  ██║██╔╝ ██╗
                ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
                """);
        show("Infinite Salutations! I'm Drax!");
        show("What's on your mind today?");
    }

    /** Displays warnings produced while loading saved tasks. */
    public void showWarnings(List<String> warnings) {
        warnings.forEach(this::show);
    }

    /** Returns whether another command is available on standard input. */
    public boolean hasNextCommand() {
        return reader.hasNextLine();
    }

    /** Reads one command from standard input. */
    public String readCommand() {
        return reader.nextLine();
    }

    /** Displays one line of text. */
    public void show(String message) {
        System.out.println(message);
    }

    /** Displays a numbered task in the same format used by the list command. */
    public void showTask(int number, Task task) {
        System.out.printf("%d.%s%n", number, task);
    }

    /** Releases the console input resource. */
    public void close() {
        reader.close();
    }
}
