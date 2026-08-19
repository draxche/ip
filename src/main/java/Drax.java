import java.util.Scanner;

public class Drax {
    public static void main(String[] args) {
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
            } else {
                System.out.println(n);
            }
        }
        reader.close();
    }
}

