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
        String n;

        while(ui.hasNextCommand()) {
            n = ui.readCommand();
            Parser.Command command = Parser.parse(n);
            if (command.type() == Parser.Type.BYE) {
                ui.show("Goodbye. Hope to see you again soon!");
                break;
            }
            else if (command.type() == Parser.Type.LIST) {
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
            }

            else if (command.type() == Parser.Type.MARK) {
                try {
                    String taskNumber = command.argument();
                    int index = Integer.parseInt(taskNumber) - 1;
                    if (index >= tasks.getSize() ||  index < 0) {
                        throw new DraxException("This task doesn't exist. You don't have that many tasks!");
                    }
                    Task task = tasks.get(index);
                    task.markAsDone();
                    saveTasks(storage, tasks, ui);
                    ui.show("I've marked this task as done:");
                    ui.show(task.toString());
                } catch (DraxException e) {
                    ui.show(e.getMessage());
                } catch (NumberFormatException e) {
                    ui.show("Please enter a valid number!");
                }
            }

            else if (command.type() == Parser.Type.UNMARK) {
                try {
                    String taskNumber = command.argument();
                    int index = Integer.parseInt(taskNumber) - 1;
                    if (index >= tasks.getSize() ||   index < 0) {
                        throw new DraxException("This task does not exist. You don't have that many tasks!");
                    }
                    Task task = tasks.get(index);
                    task.unmarkAsDone();
                    saveTasks(storage, tasks, ui);
                    ui.show("I've marked this task as not done:");
                    ui.show(task.toString());
                } catch (DraxException e) {
                    ui.show(e.getMessage());
                } catch (NumberFormatException e) {
                    ui.show("Please enter a valid number!");
                }
            }

            else if (command.type() == Parser.Type.TODO) {
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
                } catch (DraxException e) {
                    ui.show(e.getMessage());
                }
            }

            else if (command.type() == Parser.Type.DEADLINE) {
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
                } catch (DraxException | IllegalArgumentException e) {
                    ui.show(e.getMessage());
                }
            }

            else if (command.type() == Parser.Type.EVENT) {
                try {
                    if (command.firstDate().isEmpty() || command.secondDate().isEmpty()) {
                        throw new DraxException("You didn't provide when this event is happening! " +
                                "Use /from [date] /to [date]");
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
                } catch (DraxException | IllegalArgumentException e) {
                    ui.show(e.getMessage());
                }
            }

            else if (command.type() == Parser.Type.DELETE) {
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
                } catch (NumberFormatException e) {
                    ui.show("Please enter a valid number!");
                } catch (DraxException e) {
                    ui.show(e.getMessage());
                }
            }

            else {
                ui.show("Sorry! But that's not a function I can do :(");
            }
        }

        ui.close();
    }

    private static void saveTasks(Storage storage, TaskList tasks, Ui ui) {
        try {
            storage.save(tasks);
        } catch (IOException | IllegalArgumentException e) {
            ui.show("Sorry! I could not save your tasks. They are available until you exit.");
        }
    }
}
