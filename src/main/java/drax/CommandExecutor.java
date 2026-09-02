package drax;

import java.io.IOException;

/**
 * Executes parsed commands and coordinates changes to the task list,
 * storage, and user interface.
 */
public class CommandExecutor {
    /** Indicates whether the application should continue accepting commands after execution. */
    public enum Outcome {
        CONTINUE, EXIT
    }

    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;

    /**
     * Creates an executor that coordinates commands using the given application components.
     *
     * @param tasks task list that commands query and modify
     * @param storage storage used to persist task changes
     * @param ui user interface used to display command results
     */
    public CommandExecutor(TaskList tasks, Storage storage, Ui ui) {
        this.tasks = tasks;
        this.storage = storage;
        this.ui = ui;
    }

    /**
     * Executes a parsed command and reports whether command processing should continue.
     *
     * @param command command to execute
     * @return Outcome.EXIT when the application should stop, or
     *         Outcome.CONTINUE otherwise
     */
    public Outcome execute(Parser.Command command) {
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

    private Outcome executeBye() {
        ui.show("Goodbye. Hope to see you again soon!");
        return Outcome.EXIT;
    }

    private Outcome executeList() {
        int count = 1;
        if (tasks.isEmpty()) {
            ui.show("Oops! You currently have no tasks.");
        } else {
            ui.show("Here are the tasks in your list!");
        }
        for (Task task : tasks) {
            ui.showTask(count, task);
            count++;
        }
        return Outcome.CONTINUE;
    }

    private Outcome executeMark(Parser.Command command) {
        try {
            String taskNumber = command.argument();
            int index = Integer.parseInt(taskNumber) - 1;
            if (index >= tasks.getSize() || index < 0) {
                throw new DraxException("This task doesn't exist. You don't have that many tasks!");
            }
            Task task = tasks.get(index);
            task.markAsDone();
            saveTasks(storage, tasks, ui);
            ui.show("I've marked this task as done:");
            ui.show(task.toString());
            return Outcome.CONTINUE;
        } catch (DraxException e) {
            ui.show(e.getMessage());
            return Outcome.CONTINUE;
        } catch (NumberFormatException e) {
            ui.show("Please enter a valid number!");
            return Outcome.CONTINUE;
        }
    }

    private Outcome executeUnmark(Parser.Command command) {
        try {
            String taskNumber = command.argument();
            int index = Integer.parseInt(taskNumber) - 1;
            if (index >= tasks.getSize() || index < 0) {
                throw new DraxException("This task does not exist. You don't have that many tasks!");
            }
            Task task = tasks.get(index);
            task.unmarkAsDone();
            saveTasks(storage, tasks, ui);
            ui.show("I've marked this task as not done:");
            ui.show(task.toString());
            return Outcome.CONTINUE;
        } catch (DraxException e) {
            ui.show(e.getMessage());
            return Outcome.CONTINUE;
        } catch (NumberFormatException e) {
            ui.show("Please enter a valid number!");
            return Outcome.CONTINUE;
        }
    }

    private Outcome executeDelete(Parser.Command command) {
        try {
            String taskNumber = command.argument();
            int index = Integer.parseInt(taskNumber) - 1;
            if (index >= tasks.getSize() || index < 0) {
                throw new DraxException("This task does not exist. You don't have that many tasks!");
            }
            ui.show("I've deleted this task");
            ui.show(tasks.get(index).toString());
            tasks.remove(index);
            saveTasks(storage, tasks, ui);
            if (tasks.getSize() == 1) {
                ui.show("Now you have 1 task!");
            } else {
                ui.show("Now you have " + tasks.getSize() + " tasks!");
            }
            return Outcome.CONTINUE;
        } catch (NumberFormatException e) {
            ui.show("Please enter a valid number!");
            return Outcome.CONTINUE;
        } catch (DraxException | IllegalArgumentException e) {
            ui.show(e.getMessage());
            return Outcome.CONTINUE;
        }
    }

    private Outcome executeUnknown() {
        ui.show("Sorry! But that's not a function I can do :(");
        return Outcome.CONTINUE;
    }

    private Outcome createTodo(Parser.Command command) {
        try {
            String newTask = command.task();
            if (newTask.isEmpty()) {
                throw new DraxException("You didn't provide a task!?");
            }
            Todo newTodo = new Todo(newTask);
            tasks.add(newTodo);
            saveTasks(storage, tasks, ui);
            ui.show("I've added this task");
            ui.show(newTodo.toString());
            if (tasks.getSize() == 1) {
                ui.show("Now you have 1 task!");
            } else {
                ui.show("Now you have " + tasks.getSize() + " tasks!");
            }
            return Outcome.CONTINUE;
        } catch (DraxException e) {
            ui.show(e.getMessage());
            return Outcome.CONTINUE;
        }
    }

    private Outcome createDeadline(Parser.Command command) {
        try {
            if (command.firstDate().isEmpty()) {
                throw new DraxException("You didn't provide a end date! Use /by [deadline]");
            }
            String newTask = command.task();
            if (newTask.isEmpty()) {
                throw new DraxException("You didn't provide a task!?");
            }
            String deadlineText = command.firstDate();
            Deadline newDeadline = new Deadline(newTask, ScheduleDateTime.parse(deadlineText));
            tasks.add(newDeadline);
            saveTasks(storage, tasks, ui);
            ui.show("I've added this task");
            ui.show(newDeadline.toString());
            if (tasks.getSize() == 1) {
                ui.show("Now you have 1 task!");
            } else {
                ui.show("Now you have " + tasks.getSize() + " tasks!");
            }
            return Outcome.CONTINUE;
        } catch (DraxException | IllegalArgumentException e) {
            ui.show(e.getMessage());
            return Outcome.CONTINUE;
        }
    }

    private Outcome createEvent(Parser.Command command) {
        try {
            if (command.firstDate().isEmpty() || command.secondDate().isEmpty()) {
                throw new DraxException("You didn't provide when this event is happening! "
                        + "Use /from [date] /to [date]");
            }
            String newTask = command.task();
            if (newTask.isEmpty()) {
                throw new DraxException("You didn't provide a task!?");
            }
            String fromText = command.firstDate();
            String toText = command.secondDate();
            Event newEvent = new Event(newTask, ScheduleDateTime.parse(fromText),
                    ScheduleDateTime.parse(toText));
            tasks.add(newEvent);
            saveTasks(storage, tasks, ui);
            ui.show("I've added this task");
            ui.show(newEvent.toString());
            if (tasks.getSize() == 1) {
                ui.show("Now you have 1 task!");
            } else {
                ui.show("Now you have " + tasks.getSize() + " tasks!");
            }
            return Outcome.CONTINUE;
        } catch (DraxException | IllegalArgumentException e) {
            ui.show(e.getMessage());
            return Outcome.CONTINUE;
        }
    }

    private Outcome executeFind(Parser.Command command) {
        try {
            String keyword = command.argument();
            int count = 1;
            if (keyword.isEmpty()) {
                throw new DraxException("You didn't provide a keyword!");
            }
            for (Task task : tasks) {
                if (task.getTask().contains(keyword)) {
                    if (count == 1) {
                        ui.show("Here are the matching tasks in your list:");
                    }
                    ui.showTask(count, task);
                    count++;
                }
            }
            if (count == 1) {
                throw new DraxException("Oops! No matching tasks found!");
            }
            return Outcome.CONTINUE;
        } catch (DraxException e) {
            ui.show(e.getMessage());
            return Outcome.CONTINUE;
        }
    }

    private static void saveTasks(Storage storage, TaskList tasks, Ui ui) {
        try {
            storage.save(tasks);
        } catch (IOException | IllegalArgumentException e) {
            ui.show("Sorry! I could not save your tasks. They are available until you exit the program.");
        }
    }
}
