import java.util.ArrayList;
import java.util.Scanner;

public class Drax {
    public static void main(String[] args) {
        ArrayList<String> tasks = new ArrayList<String>();
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
                for (String task : tasks) {
                    System.out.println(count + ". " + task);
                    count++;
                }
            }
            else {
                tasks.add(n);
                System.out.println("added: " + n);
            }
        }
        reader.close();
    }
}

