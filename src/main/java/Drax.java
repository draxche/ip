import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Drax {
    public static void main(String[] args) {
        TaskStorage.LoadResult loadResult = loadTasks();
        ArrayList<Task> tasks = loadResult.tasks();
        String banner = """
                ██████╗ ██████╗  █████╗ ██╗  ██╗
                ██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝
                ██║  ██║██████╔╝███████║ ╚███╔╝
                ██║  ██║██╔══██╗██╔══██║ ██╔██╗
                ██████╔╝██║  ██║██║  ██║██╔╝ ██╗
                ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
                """;
        System.out.println(banner);
        System.out.println("Infinite Salutations! I'm Drax!");
        System.out.println("What's on your mind today?");
        for (String warning : loadResult.warnings()) {
            System.out.println(warning);
        }
        Scanner reader = new Scanner(System.in);
        String n;

        while(reader.hasNextLine()) {
            n = reader.nextLine();
            if (n.equals("bye")) {
                System.out.println("Goodbye. Hope to see you again soon!");
                break;
            }
            else if (n.equals("list")) {
                int count = 1;
                if (tasks.isEmpty()) {
                    System.out.println("Oops! You currently have no tasks.");
                } else {
                    System.out.println("Here are the tasks in your list!");
                }
                for (Task task : tasks) {
                    System.out.printf("%d.%s%n", count, task);
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
                    saveTasks(tasks);
                    System.out.println("I've marked this task as done:");
                    System.out.println(task);
                } catch (DraxException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number!");
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
                    saveTasks(tasks);
                    System.out.println("I've marked this task as not done:");
                    System.out.println(task);
                } catch (DraxException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number!");
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
                    saveTasks(tasks);
                    System.out.println("I've added this task");
                    System.out.println(newTodo);
                    if (tasks.size() == 1) {
                        System.out.println("Now you have 1 task!");
                    } else {
                        System.out.println("Now you have " + tasks.size() + " tasks!");
                    }
                } catch (DraxException e) {
                    System.out.println(e.getMessage());
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
                    saveTasks(tasks);
                    System.out.println("I've added this task");
                    System.out.println(newDeadline);
                    if (tasks.size() == 1) {
                        System.out.println("Now you have 1 task!");
                    } else {
                        System.out.println("Now you have " + tasks.size() + " tasks!");
                    }
                } catch (DraxException | IllegalArgumentException e) {
                    System.out.println(e.getMessage());
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
                    saveTasks(tasks);
                    System.out.println("I've added this task");
                    System.out.println(newEvent);
                    if (tasks.size() == 1) {
                        System.out.println("Now you have 1 task!");
                    } else {
                        System.out.println("Now you have " + tasks.size() + " tasks!");
                    }
                } catch (DraxException | IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            else if (n.startsWith("delete ")) {
                try {
                    String taskNumber = n.substring(7).trim();
                    int index = Integer.parseInt(taskNumber) - 1;
                    if (index >= tasks.size() || index < 0) {
                        throw new DraxException("This task does not exist. You don't have that many tasks!");
                    }
                    System.out.println("I've deleted this task");
                    System.out.println(tasks.get(index));
                    tasks.remove(index);
                    saveTasks(tasks);
                    if (tasks.size() == 1) {
                        System.out.println("Now you have 1 task!");
                    } else {
                        System.out.println("Now you have " + tasks.size() + " tasks!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number!");
                } catch (DraxException e) {
                    System.out.println(e.getMessage());
                }
            }

            else {
                System.out.println("Sorry! But that's not a function I can do :(");
            }
        }

        reader.close();
    }

    private static void saveTasks(ArrayList<Task> tasks) {
        try {
            TaskStorage.save(tasks);
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Sorry! I could not save your tasks. They are available until you exit.");
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
