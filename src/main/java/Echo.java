import java.util.Scanner;
import java.util.ArrayList;
/**
 * Reads user input, repeats the input and ends session when 'bye' command is received
 */
public class Echo {
    private static final String NAME = "Echo";
    private static final String LINE = "\n" + "-".repeat(30) + "\n";
    private static final String ENDLINE = "\n" + "-".repeat(30);
    private static boolean endSession = false;
    private TodoList todoList = new TodoList();
    private Scanner scanner = new Scanner(System.in);

    private class TodoList {
        private ArrayList<String> list = new ArrayList<>();
        public void add_to_list(String item) {
            list.add(item);
        }
        @Override
        public String toString() {
            int len = list.size();
            String output = "";
            for (int i = 0; i < len; i ++) {
                output += String.format("%d. %s\n", i + 1, list.get(i));
            }
            return LINE + output + ENDLINE;
        }
    }

    private void print_welcome() {
        String banner = " _____     _           \n"
                + "| ____|___| |__   ___  \n"
                + "|  _| / __| '_ \\ / _ \\ \n"
                + "| |__| (__| | | | (_) |\n"
                + "|_____\\___|_| |_|\\___/ \n";
        String welcomeMessage = LINE + banner + LINE + String.format("Hi, I'm %s.\nWhat can I do for you?\n",
                NAME) + ENDLINE;
        System.out.println(welcomeMessage);
    }

    private void print_farewell() {
        String farewellMessage = "Goodbye" + LINE;
        System.out.println(farewellMessage);
    }

    private void start() {
        while (!endSession) {
            String input = this.scanner.nextLine();
            if (input.equalsIgnoreCase("bye")) {
                endSession = true;
                this.print_farewell();
                break;
            }
            else if (input.equalsIgnoreCase("list")) {
                System.out.println(this.todoList);
            }
            else {
                this.todoList.add_to_list(input);
                System.out.println(LINE + "Added: " + input + ENDLINE);
            }
        }
    }

    public static void main(String[] args) {
        Echo echo = new Echo();
        echo.print_welcome();
        echo.start();
    }
}
