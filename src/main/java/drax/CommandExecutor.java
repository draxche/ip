package drax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Executes parsed commands and coordinates changes to the task list and storage. */
public class CommandExecutor {
    /** Indicates whether the application should continue accepting commands after execution. */
    public enum Outcome {
        CONTINUE, EXIT
    }

    /**
     * Contains the text produced by a command and whether the application should continue.
     *
     * @param response text to present to the user in a string format
     * @param outcome whether command processing should continue
     */
    public record ExecutionResult(String response, Outcome outcome) {
    }

    private final TaskList tasks;
    private final Storage storage;

    /**
     * Creates an executor that coordinates commands using the given application components.
     *
     * @param tasks task list that commands query and modify
     * @param storage storage used to persist task changes
     */
    public CommandExecutor(TaskList tasks, Storage storage) {
        this.tasks = tasks;
        this.storage = storage;
    }

    /**
     * Executes a parsed command and returns its display text and control-flow outcome.
     *
     * @param command command to execute
     * @return the response to display in string format and whether the application should continue
     */
    public ExecutionResult execute(Parser.Command command) {
        return switch (command.type()) {
            case BYE -> executeBye();
            case LIST -> executeList();
            case MARK -> executeMark(command);
            case UNMARK -> executeUnmark(command);
            case DELETE -> executeDelete(command);
            case TODO -> createTodo(command);
            case DEADLINE -> createDeadline(command);
            case EVENT -> createEvent(command);
            case FIND -> executeFind(command);
            default -> executeUnknown();
        };
    }

    private ExecutionResult executeBye() {
        return exitWith("Goodbye. Hope to see you again soon!");
    }

    private ExecutionResult executeList() {
        List<String> messages = new ArrayList<>();
        if (tasks.isEmpty()) {
            messages.add("Oops! You currently have no tasks.");
        } else {
            messages.add("Here are the tasks in your list!");
        }
        int count = 1;
        for (Task task : tasks) {
            messages.add(formatTask(count, task));
            count++;
        }
        return continueWith(messages);
    }

    private ExecutionResult executeMark(Parser.Command command) {
        try {
            int index = parseTaskIndex(command.argument());
            Task task = tasks.get(index);
            task.markAsDone();

            List<String> messages = new ArrayList<>();
            saveTasks(messages);
            messages.add("I've marked this task as done:");
            messages.add(task.toString());
            return continueWith(messages);
        } catch (DraxException e) {
            return continueWith(e.getMessage());
        } catch (NumberFormatException e) {
            return continueWith("Please enter a valid number!");
        }
    }

    private ExecutionResult executeUnmark(Parser.Command command) {
        try {
            int index = parseTaskIndex(command.argument());
            Task task = tasks.get(index);
            task.unmarkAsDone();

            List<String> messages = new ArrayList<>();
            saveTasks(messages);
            messages.add("I've marked this task as not done:");
            messages.add(task.toString());
            return continueWith(messages);
        } catch (DraxException e) {
            return continueWith(e.getMessage());
        } catch (NumberFormatException e) {
            return continueWith("Please enter a valid number!");
        }
    }

    private ExecutionResult executeDelete(Parser.Command command) {
        try {
            int index = parseTaskIndex(command.argument());
            List<String> messages = new ArrayList<>();
            messages.add("I've deleted this task");
            messages.add(tasks.get(index).toString());

            tasks.remove(index);
            saveTasks(messages);
            messages.add(getTaskCountMessage());
            return continueWith(messages);
        } catch (NumberFormatException e) {
            return continueWith("Please enter a valid number!");
        } catch (DraxException | IllegalArgumentException e) {
            return continueWith(e.getMessage());
        }
    }

    private ExecutionResult executeUnknown() {
        return continueWith("Sorry! But that's not a function I can do :(");
    }

    private ExecutionResult createTodo(Parser.Command command) {
        try {
            String newTask = command.task();
            if (newTask.isEmpty()) {
                throw new DraxException("You didn't provide a task!?");
            }
            Todo newTodo = new Todo(newTask);
            tasks.add(newTodo);
            return getTaskCreatedResult(newTodo);
        } catch (DraxException e) {
            return continueWith(e.getMessage());
        }
    }

    private ExecutionResult createDeadline(Parser.Command command) {
        try {
            if (command.firstDate().isEmpty()) {
                throw new DraxException("You didn't provide a end date! Use /by [deadline]");
            }
            String newTask = command.task();
            if (newTask.isEmpty()) {
                throw new DraxException("You didn't provide a task!?");
            }
            Deadline newDeadline = new Deadline(newTask, ScheduleDateTime.parse(command.firstDate()));
            tasks.add(newDeadline);
            return getTaskCreatedResult(newDeadline);
        } catch (DraxException | IllegalArgumentException e) {
            return continueWith(e.getMessage());
        }
    }

    private ExecutionResult createEvent(Parser.Command command) {
        try {
            if (command.firstDate().isEmpty() || command.secondDate().isEmpty()) {
                throw new DraxException("You didn't provide when this event is happening! "
                        + "Use /from [date] /to [date]");
            }
            String newTask = command.task();
            if (newTask.isEmpty()) {
                throw new DraxException("You didn't provide a task!?");
            }
            Event newEvent = new Event(newTask, ScheduleDateTime.parse(
                    command.firstDate()), ScheduleDateTime.parse(command.secondDate()));
            tasks.add(newEvent);
            return getTaskCreatedResult(newEvent);
        } catch (DraxException | IllegalArgumentException e) {
            return continueWith(e.getMessage());
        }
    }

    private ExecutionResult executeFind(Parser.Command command) {
        try {
            String keyword = command.argument();
            if (keyword.isEmpty()) {
                throw new DraxException("You didn't provide a keyword!");
            }

            List<String> messages = new ArrayList<>();
            int count = 1;
            for (Task task : tasks) {
                if (task.getTask().contains(keyword)) {
                    if (count == 1) {
                        messages.add("Here are the matching tasks in your list:");
                    }
                    messages.add(formatTask(count, task));
                    count++;
                }
            }
            if (count == 1) {
                throw new DraxException("Oops! No matching tasks found!");
            }
            return continueWith(messages);
        } catch (DraxException e) {
            return continueWith(e.getMessage());
        }
    }

    private ExecutionResult getTaskCreatedResult(Task task) {
        List<String> messages = new ArrayList<>();
        saveTasks(messages);
        messages.add("I've added this task");
        messages.add(task.toString());
        messages.add(getTaskCountMessage());
        return continueWith(messages);
    }

    private int parseTaskIndex(String taskNumber) throws DraxException {
        int index = Integer.parseInt(taskNumber) - 1;
        if (index >= tasks.getSize() || index < 0) {
            throw new DraxException("This task does not exist. You don't have that many tasks!");
        }
        return index;
    }

    private String getTaskCountMessage() {
        if (tasks.getSize() == 1) {
            return "Now you have 1 task!";
        }
        return "Now you have " + tasks.getSize() + " tasks!";
    }

    private String formatTask(int number, Task task) {
        return number + "." + task;
    }

    private void saveTasks(List<String> messages) {
        try {
            storage.save(tasks);
        } catch (IOException | IllegalArgumentException e) {
            messages.add("Sorry! I could not save your tasks. They are available until you exit the program.");
        }
    }

    private static ExecutionResult continueWith(String... messages) {
        return continueWith(List.of(messages));
    }

    private static ExecutionResult continueWith(List<String> messages) {
        return new ExecutionResult(String.join("\n", messages), Outcome.CONTINUE);
    }

    private static ExecutionResult exitWith(String message) {
        return new ExecutionResult(message, Outcome.EXIT);
    }
}
