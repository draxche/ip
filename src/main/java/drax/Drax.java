package drax;

import java.io.IOException;
import java.util.ArrayList;

/** Entry point and command-processing loop for the drax task manager. */
public class Drax {
    /**
     * Starts drax, loads saved tasks, and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Storage storage = new Storage();
        Storage.LoadResult loadResult;
        try {
            loadResult = storage.load();
        } catch (IOException e) {
            ArrayList<String> warnings = new ArrayList<>();
            warnings.add("Sorry! I could not read your saved tasks. Starting with an empty list.");
            loadResult = new Storage.LoadResult(new ArrayList<>(), warnings);
        }

        TaskList tasks = new TaskList(loadResult.tasks());
        Ui ui = new Ui();
        ui.showGreeting();
        ui.showWarnings(loadResult.warnings());

        CommandExecutor executor = new CommandExecutor(tasks, storage, ui);

        while (ui.hasNextCommand()) {
            Parser.Command command = Parser.parse(ui.readCommand());
            CommandExecutor.Outcome outcome = executor.execute(command);
            if (outcome == CommandExecutor.Outcome.EXIT) {
                break;
            }
        }

        ui.close();
    }
}
