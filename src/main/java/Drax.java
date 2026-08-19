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
                for (Task task : tasks) {
                    System.out.println(count + "." + "[" + task.getStatusIcon() + "] "+ task.getTask());
                    count++;
                }
            }

            else if (n.startsWith("mark ")) {
                String taskNumber = n.substring(5).trim();
                int index = Integer.parseInt(taskNumber) - 1;
                Task task = tasks.get(index);
                task.markAsDone();
                System.out.println("I've marked this task as done:");
                System.out.println("[X] " + task.getTask());
            }

            else if (n.startsWith("unmark ")) {
                String taskNumber = n.substring(7).trim();
                int index = Integer.parseInt(taskNumber) - 1;
                Task task = tasks.get(index);
                task.unmarkAsDone();
                System.out.println("I've marked this task as not done:");
                System.out.println("[ ] " + task.getTask());
            }
            else {
                tasks.add(new Task(n));
                System.out.println("added: " + n);
            }
        }
        reader.close();
    }
}
