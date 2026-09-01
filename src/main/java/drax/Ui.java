package drax;

import java.util.List;
import java.util.Scanner;

/** Handles drax.Drax's console input and output so the command loop stays focused on application logic. */
public class Ui {
    private final Scanner reader;

    public Ui() {
        this.reader = new Scanner(System.in);
    }

    /** Prints out the chatbot's banner and its greetings. */
    public void showGreeting() {
        System.out.println("""
                ██████╗ ██████╗  █████╗ ██╗  ██╗
                ██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
                ██║  ██║██████╔╝███████║ ╚███╔╝
                ██║  ██║██╔══██╗██╔══██║ ██╔██╗
                ██████╔╝██║  ██║██║  ██║██╔╝ ██╗
                ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
                """);
        show("Infinite Salutations! I'm drax.Drax!");
        show("What's on your mind today?");
    }

    public void showWarnings(List<String> warnings) {
        warnings.forEach(this::show);
    }

    public boolean hasNextCommand() {
        return reader.hasNextLine();
    }

    public String readCommand() {
        return reader.nextLine();
    }

    public void show(String message) {
        System.out.println(message);
    }

    public void showTask(int number, Task task) {
        System.out.printf("%d.%s%n", number, task);
    }

    public void close() {
        reader.close();
    }
}
