import java.util.Scanner;
import java.util.ArrayList;
/**
 * Reads user input, and performs a task based on whether the input matches certain commands,
 * such as adding various tasks to a to do list. Exits when the bye command is entered
 */
public class Echo {
    private static final String NAME = "Echo";
    private static final String LINE = "\n" + "-".repeat(30) + "\n";
    private static final String ENDLINE = "\n" + "-".repeat(30);
    private static boolean endSession = false;
    private TodoList todoList = new TodoList();
    private Scanner scanner = new Scanner(System.in);

    public class TodoList {
        private ArrayList<Task> list = new ArrayList<>();
        public void addToList(Task item) {
            list.add(item);
            System.out.println(String.format("\nThere are now %d items in the list\n",
                    list.size()));
        }
        public void markList(int taskNum) {
            if (list.size() < taskNum || taskNum < 1) {
                throw new IllegalArgumentException();
            }
            list.get(taskNum - 1).mark();
        }
        public void unmarkList(int taskNum) {
            if (list.size() < taskNum || taskNum < 1) {
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
                Task currentTask = list.get(i);
                output += String.format("%d. %s\n", i + 1,
                        currentTask);
            }
            return LINE + output + ENDLINE;
        }
    }

    private void printWelcome() {
        String banner = " _____     _           \n"
                + "| ____|___| |__   ___  \n"
                + "|  _| / __| '_ \\ / _ \\ \n"
                + "| |__| (__| | | | (_) |\n"
                + "|_____\\___|_| |_|\\___/ \n";
        String welcomeMessage = LINE + banner + LINE + String.format("Hi, I'm %s.\nWhat can I do for you?\n",
                NAME) + ENDLINE;
        System.out.println(welcomeMessage);
    }

    private void printFarewell() {
        String farewellMessage = "Goodbye" + LINE;
        System.out.println(farewellMessage);
    }

    private void start() {
        while (!endSession) {
            String input = this.scanner.nextLine();
            String command = input.toLowerCase();
            if (command.equals("bye")) {
                endSession = true;
                this.printFarewell();
                break;
            }
            else if (command.equals("list")) {
                System.out.println(this.todoList);
            }
            else if (command.startsWith("mark ")) {
                try {
                    int taskNum = Integer.parseInt(command.substring(5).trim());
                    this.todoList.markList(taskNum);
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
                    this.todoList.unmarkList(taskNum);
                    System.out.println(String.format("Marked this task as not done:\n  %s",
                            todoList.getTask(taskNum - 1)));
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Invalid input for unmark. Example usage: unmark 2");
                }
            }
            else if (command.startsWith("todo ")) {
                try {
                    String desc = input.substring(5).trim();
                    Todo newTask = new Todo(desc);
                    this.todoList.addToList(newTask);
                    System.out.println(String.format("Added this todo task:\n  %s",
                            newTask));
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Invalid input for todo.");
                }
            }
            else if (command.startsWith("deadline ")) {
                try {
                    String[] desc = input.substring(9).trim().split(" /");
                    if (desc.length < 2) {
                        throw new IllegalArgumentException();
                    }
                    Deadline newTask = new Deadline(desc[0], desc[1]);
                    this.todoList.addToList(newTask);
                    System.out.println(String.format("Added this deadline task:\n  %s",
                            newTask));
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Invalid input for deadline.");
                }
            }
            else if (command.startsWith("event ")) {
                try {
                    String[] desc = input.substring(6).trim().split(" /");
                    if (desc.length < 3) {
                        throw new IllegalArgumentException();
                    }
                    Event newTask = new Event(desc[0], desc[1], desc[2]);
                    this.todoList.addToList(newTask);
                    System.out.println(String.format("Added this event task:\n  %s",
                            newTask));
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Invalid input for event.");
                }
            }
            else {
                this.todoList.addToList(new Task(input));
                System.out.println(LINE + "Added: " + input + ENDLINE);
            }
        }
    }

    public static void main(String[] args) {
        Echo echo = new Echo();
        echo.printWelcome();
        echo.start();
    }
}

/**
 * Represents a task with a description and completion status.
 */
class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    public String getDesc() {
        return description;
    }
    public String getStatusMarker() {
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
        return this.getStatusMarker() + " " + this.description;
    }
}

/**
 * Creates a task item with no time or date associated with it.
 */
class Todo extends Task {
    String taskMarker = "[T]";
    public Todo(String desc) {
        super(desc);
    }
    @Override
    public String toString() {
        return String.format("%s%s %s", this.taskMarker, this.getStatusMarker(),
                super.getDesc());
    }
}

/**
 * Creates a deadline task with a description and due date.
 */
class Deadline extends Task {
    String taskMarker = "[D]";
    String deadline = "";
    public Deadline(String desc, String deadline) {
        super(desc);
        this.deadline = deadline;
    }
    @Override
    public String toString() {
        return String.format("%s%s %s (By: %s)", this.taskMarker, this.getStatusMarker(),
                super.getDesc(), this.deadline);
    }
}

/**
 * Creates an event item with a from time and to time.
 */
class Event extends Task {
    String taskMarker = "[E]";
    String from = "";
    String to = "";
    public Event(String desc, String from, String to) {
        super(desc);
        this.from = from;
        this.to = to;
    }
    @Override
    public String toString() {
        return String.format("%s%s %s (from: %s to: %s)", this.taskMarker, this.getStatusMarker(),
                super.getDesc(), this.from, this.to);
    }
}

