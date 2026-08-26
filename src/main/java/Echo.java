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

    public class Task {
        private String description;
        private boolean isDone;

        public Task(String description) {
            this.description = description;
            this.isDone = false;
        }
        public String get_desc() {
            return description;
        }
        public String get_status_marker() {
            return this.isDone ? "[X]" : "[ ]";
        }
        public void mark() {
            this.isDone = true;
        }
        public void unmark() {
            this.isDone = false;
        }
        @Override
        public String toString() {
            return this.get_status_marker() + this.description;
        }
    }

    public class TodoList {
        private ArrayList<Task> list = new ArrayList<>();
        public void add_to_list(String item) {
            list.add(new Task(item));
        }
        public void mark_list(int taskNum) {
            if (list.size() < taskNum) {
                throw new IllegalArgumentException();
            }
            list.get(taskNum - 1).mark();
        }
        public void unmark_list(int taskNum) {
            if (list.size() < taskNum) {
                throw new IllegalArgumentException();
            }
            list.get(taskNum - 1).unmark();
        }
        public Task getTask(int index){
            return this.list.get(index);
        }
        @Override
        public String toString() {
            int len = list.size();
            String output = "";
            for (int i = 0; i < len; i ++) {
                Task currentElem = list.get(i);
                output += String.format("%d. %s %s\n", i + 1,
                        currentElem.get_status_marker(),currentElem.get_desc());
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
            String command = input.toLowerCase();
            if (command.equals("bye")) {
                endSession = true;
                this.print_farewell();
                break;
            }
            else if (command.equals("list")) {
                System.out.println(this.todoList);
            }
            else if (command.startsWith("mark ")) {
                try {
                    int taskNum = Integer.parseInt(command.substring(5).trim());
                    this.todoList.mark_list(taskNum);
                    System.out.println(String.format("Marked this task as done:\n  %s",
                            todoList.getTask(taskNum - 1)));
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Invalid input for mark. Example usage: mark 2");
                }
            }
            else if (command.startsWith("unmark ")) {
                try {
                    int taskNum = Integer.parseInt(command.substring(7).trim());
                    this.todoList.unmark_list(taskNum);
                    System.out.println(String.format("Marked this task as not done:\n  %s",
                            todoList.getTask(taskNum - 1)));
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Invalid input for unmark. Example usage: unmark 2");
                }
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
