import java.util.ArrayList;
import java.util.Scanner;

public class Drax {
    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();
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
                    String deadline = n.substring(split + 5).trim();
                    Deadline newDeadline = new Deadline(newTask, deadline);
                    tasks.add(newDeadline);
                    System.out.println("I've added this task");
                    System.out.println(newDeadline);
                    if (tasks.size() == 1) {
                        System.out.println("Now you have 1 task!");
                    } else {
                        System.out.println("Now you have " + tasks.size() + " tasks!");
                    }
                } catch (DraxException e) {
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
                    String from = n.substring(fromIndex + 7, toIndex).trim();
                    String to = n.substring(toIndex + 5).trim();
                    Event newEvent = new Event(newTask, from, to);
                    tasks.add(newEvent);
                    System.out.println("I've added this task");
                    System.out.println(newEvent);
                    if (tasks.size() == 1) {
                        System.out.println("Now you have 1 task!");
                    } else {
                        System.out.println("Now you have " + tasks.size() + " tasks!");
                    }
                } catch (DraxException e) {
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
}
