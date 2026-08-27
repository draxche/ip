import java.io.IOException;
import java.util.ArrayList;

public class Drax {
    public static void main(String[] args) {
        TaskStorage.LoadResult loadResult = loadTasks();
        TaskList tasks = new TaskList(loadResult.tasks());
        Ui ui = new Ui();
        ui.showGreeting();
        ui.showWarnings(loadResult.warnings());
        String n;

        while(ui.hasNextCommand()) {
            n = ui.readCommand();
            if (n.equals("bye")) {
                ui.show("Goodbye. Hope to see you again soon!");
                break;
            }
            else if (n.equals("list")) {
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

            else if (n.startsWith("mark ")) {
                try {
                    String taskNumber = n.substring(5).trim();
                    int index = Integer.parseInt(taskNumber) - 1;
                    if (index >= tasks.size() ||  index < 0) {
                        throw new DraxException("This task doesn't exist. You don't have that many tasks!");
                    }
                    Task task = tasks.get(index);
                    task.markAsDone();
                    saveTasks(tasks, ui);
                    ui.show("I've marked this task as done:");
                    ui.show(task.toString());
                } catch (DraxException e) {
                    ui.show(e.getMessage());
                } catch (NumberFormatException e) {
                    ui.show("Please enter a valid number!");
                }
            }

            else if (n.startsWith("unmark ")) {
                try {
                    String taskNumber = n.substring(7).trim();
                    int index = Integer.parseInt(taskNumber) - 1;
                    if (index >= tasks.size() ||   index < 0) {
                        throw new DraxException("This task does not exist. You don't have that many tasks!");
                    }
                    Task task = tasks.get(index);
                    task.unmarkAsDone();
                    saveTasks(tasks, ui);
                    ui.show("I've marked this task as not done:");
                    ui.show(task.toString());
                } catch (DraxException e) {
                    ui.show(e.getMessage());
                } catch (NumberFormatException e) {
                    ui.show("Please enter a valid number!");
                }
            }

            else if (n.startsWith("todo ")) {
                try {
                    String newTask = n.substring(5).trim();
                    if (newTask.isEmpty()) {
                        throw new DraxException("You didn't provide a task!?");
                    }
                    Todo newTodo = new Todo(newTask);
                    tasks.add(newTodo);
                    saveTasks(tasks, ui);
                    ui.show("I've added this task");
                    ui.show(newTodo.toString());
                    if (tasks.size() == 1) {
                        ui.show("Now you have 1 task!");
                    } else {
                        ui.show("Now you have " + tasks.size() + " tasks!");
                    }
                } catch (DraxException e) {
                    ui.show(e.getMessage());
                }
            }

            else if (n.startsWith("deadline ")) {
                try {
                    int split = n.indexOf(" /by ");
                    if (split == -1) {
                        throw new DraxException("You didn't provide a end date! Use /by [deadline]");
                    }
                    String newTask = n.substring(9, split).trim();
                    if (newTask.isEmpty()) {
                        throw new DraxException("You didn't provide a task!?");
                    }
                    String deadlineText = n.substring(split + 5).trim();
                    Deadline newDeadline = new Deadline(newTask, ScheduleDateTime.parse(deadlineText));
                    tasks.add(newDeadline);
                    saveTasks(tasks, ui);
                    ui.show("I've added this task");
                    ui.show(newDeadline.toString());
                    if (tasks.size() == 1) {
                        ui.show("Now you have 1 task!");
                    } else {
                        ui.show("Now you have " + tasks.size() + " tasks!");
                    }
                } catch (DraxException | IllegalArgumentException e) {
                    ui.show(e.getMessage());
                }
            }

            else if (n.startsWith("event ")) {
                try {
                    int fromIndex = n.indexOf(" /from ");
                    int toIndex = n.indexOf(" /to ");
                    if (fromIndex == -1 || toIndex == -1) {
                        throw new DraxException("You didn't provide when this event is happening! " +
                                "Use /from [date] /to [date]");
                    }
                    String newTask = n.substring(6, fromIndex).trim();
                    if (newTask.isEmpty()) {
                        throw new DraxException("You didn't provide a task!?");
                    }
                    String fromText = n.substring(fromIndex + 7, toIndex).trim();
                    String toText = n.substring(toIndex + 5).trim();
                    Event newEvent = new Event(newTask, ScheduleDateTime.parse(fromText),
                            ScheduleDateTime.parse(toText));
                    tasks.add(newEvent);
                    saveTasks(tasks, ui);
                    ui.show("I've added this task");
                    ui.show(newEvent.toString());
                    if (tasks.size() == 1) {
                        ui.show("Now you have 1 task!");
                    } else {
                        ui.show("Now you have " + tasks.size() + " tasks!");
                    }
                } catch (DraxException | IllegalArgumentException e) {
                    ui.show(e.getMessage());
                }
            }

            else if (n.startsWith("delete ")) {
                try {
                    String taskNumber = n.substring(7).trim();
                    int index = Integer.parseInt(taskNumber) - 1;
                    if (index >= tasks.size() || index < 0) {
                        throw new DraxException("This task does not exist. You don't have that many tasks!");
                    }
                    ui.show("I've deleted this task");
                    ui.show(tasks.get(index).toString());
                    tasks.remove(index);
                    saveTasks(tasks, ui);
                    if (tasks.size() == 1) {
                        ui.show("Now you have 1 task!");
                    } else {
                        ui.show("Now you have " + tasks.size() + " tasks!");
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

    private static void saveTasks(TaskList tasks, Ui ui) {
        try {
            TaskStorage.save(tasks.asList());
        } catch (IOException | IllegalArgumentException e) {
            ui.show("Sorry! I could not save your tasks. They are available until you exit.");
        }
    }

    private static TaskStorage.LoadResult loadTasks() {
        try {
            return TaskStorage.load();
        } catch (IOException e) {
            ArrayList<String> warnings = new ArrayList<>();
            warnings.add("Sorry! I could not read your saved tasks. Starting with an empty list.");
            return new TaskStorage.LoadResult(new ArrayList<>(), warnings);
        }
    }
}
