package drax;

import java.util.Scanner;

/** Handles terminal input and presents response strings produced by Drax. */
public class Ui {
    private static final String BANNER = """
            ██████╗ ██████╗  █████╗ ██╗  ██╗
            ██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
            ██║  ██║██████╔╝███████║ ╚███╔╝
            ██║  ██║██╔══██╗██╔══██║ ██╔██╗
            ██████╔╝██║  ██║██║  ██║██╔╝ ██╗
            ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝""";

    private final Scanner reader;

    /** Creates a console UI that reads commands from standard input. */
    public Ui() {
        this.reader = new Scanner(System.in);
    }

    /**
     * Adds console-only branding to the interface-neutral greeting.
     *
     * @param greeting greeting supplied by Drax
     * @return the banner and greeting formatted for the terminal
     */
    public String getConsoleGreeting(String greeting) {
        return BANNER + "\n\n" + greeting;
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

    public void close() {
        reader.close();
    }
}
