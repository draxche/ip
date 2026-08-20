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
                System.out.println("Here are the tasks in your list!");
                for (Task task : tasks) {
                    System.out.printf("%d.%s%n", count, task);
                    count++;
                }
            }

            else if (n.startsWith("mark ")) {
                String taskNumber = n.substring(5).trim();
                int index = Integer.parseInt(taskNumber) - 1;
                Task task = tasks.get(index);
                task.markAsDone();
                System.out.println("I've marked this task as done:");
                System.out.println(task);
            }

            else if (n.startsWith("unmark ")) {
                String taskNumber = n.substring(7).trim();
                int index = Integer.parseInt(taskNumber) - 1;
                Task task = tasks.get(index);
                task.unmarkAsDone();
                System.out.println("I've marked this task as not done:");
                System.out.println(task);
            }

            else if (n.startsWith("todo ")) {
                String newTask = n.substring(5).trim();
                Todo newTodo = new Todo(newTask);
                tasks.add(newTodo);
                System.out.println("I've added this task");
                System.out.println(newTodo);
                if (tasks.size() == 1) {
                    System.out.println("Now you have 1 task!");
                } else {
                    System.out.println("Now you have " + tasks.size() + " tasks!");
                }
            }

            else if (n.startsWith("deadline ")) {
                int split = n.indexOf(" /by ");
                String newTask = n.substring(9, split).trim();
                String deadline = n.substring(split + 5).trim();
                Deadline newDeadline= new Deadline(newTask, deadline);
                tasks.add(newDeadline);
                System.out.println("I've added this task");
                System.out.println(newDeadline);
                if (tasks.size() == 1) {
                    System.out.println("Now you have 1 task!");
                } else {
                    System.out.println("Now you have " + tasks.size() + " tasks!");
                }
            }

            else if (n.startsWith("event ")) {
                int fromIndex = n.indexOf(" /from ");
                int toIndex = n.indexOf(" /to ");
                String newTask = n.substring(6, fromIndex).trim();
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
            }

            else {
                System.out.println("Sorry! But that's not a function I can do :(");
            }
        }
        reader.close();
    }
}
