package echo;

import java.util.Scanner;

/**
 * Handles command-line input and output for Echo.
 */
public class Ui {
    private Scanner scanner = new Scanner(System.in);
    private static final String NAME = "Echo";
    private static final String LINE = "\n" + "-".repeat(30) + "\n";
    private static final String ENDLINE = "\n" + "-".repeat(30);

    /**
     * Creates a user interface that reads commands from standard input.
     */
    public Ui() {
    };

    /**
     * Reads the next command entered by the user.
     *
     * @return the entered command
     */
    public String readCommand() {
        return this.scanner.nextLine();
    }

    /**
     * Displays Echo's welcome message.
     */
    public static void printWelcome() {
        String banner = " _____     _           \n"
                + "| ____|___| |__   ___  \n"
                + "|  _| / __| '_ \\ / _ \\ \n"
                + "| |__| (__| | | | (_) |\n"
                + "|_____\\___|_| |_|\\___/ \n";
        String welcomeMessage = LINE + banner + LINE + String.format("Hi, I'm %s.\nWhat can I do for you?\n",
                NAME) + ENDLINE;
        System.out.println(welcomeMessage);
        // return welcomeMessage;
    }

    /**
     * Returns Echo's welcome message.
     */
    public static String getWelcome() {
        String banner =   " _____     _           \n"
                        + "| ____|___| |__   ___  \n"
                        + "|  _| / __| '_ \\ / _ \\ \n"
                        + "| |__| (__| | | | (_) |\n"
                        + "|_____\\__|_| |_|\\___/ \n";
        String welcomeMessage = LINE + banner + LINE + String.format("Hi, I'm %s.\nWhat can I do for you?\n",
                NAME) + ENDLINE;
        return welcomeMessage;
    }

    /**
     * Displays Echo's farewell message.
     */
    public static void printFarewell() {
        String farewellMessage = "Goodbye" + LINE;
        System.out.println(farewellMessage);
    }

    /**
     * @return Echo's farewell message
     */
    public static String getFarewell() {
        String farewellMessage = "Goodbye" + LINE;
        return farewellMessage;
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task the task that was added
     * @param type the type of task that was added
     */
    public void printAddedTask(Task task, String type) {
        System.out.println(String.format("Added this %s task:\n  %s",
                type, task));
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task the task that was added
     * @param type the type of task that was added
     */
    public String getAddedTaskString(Task task, String type) {
        return String.format("Added this %s task:\n  %s",
                type, task);
    }

    /**
     * Displays confirmation that a task was deleted and the number of tasks remaining.
     *
     * @param deletedTask the task that was deleted
     * @param size the number of tasks remaining
     */
    public void printDeleteTask(Task deletedTask, int size) {
        System.out.println(String.format("Deleted this task\n  %s\nNow you have %d tasks left",
                deletedTask, size));
    }

    /**
     * Return string representing confirmation that a task was deleted and the number of tasks remaining.
     *
     * @param deletedTask the task that was deleted
     * @param size the number of tasks remaining
     */
    public String getDeleteTask(Task deletedTask, int size) {
        return String.format("Deleted this task\n  %s\nNow you have %d tasks left",
                deletedTask, size);
    }

    /**
     * Displays confirmation that a task's completion status changed.
     *
     * @param task the task whose status changed
     * @param isMarked whether the task is now marked as complete
     */
    public void printMark(Task task, boolean isMarked) {
        if (isMarked) {
            System.out.println(String.format("Marked this task as done:\n  %s",
                    task));
        } else {
            System.out.println(String.format("Marked this task as not done:\n  %s",
                    task));
        }
    }

    /**
     * Returns string of confirmation that a task's completion status changed.
     *
     * @param task the task whose status changed
     * @param isMarked whether the task is now marked as complete
     */
    public String getMarkString(Task task, boolean isMarked) {
        if (isMarked) {
            return String.format("Marked this task as done:\n  %s", task);
        } else {
            return String.format("Marked this task as not done:\n  %s", task);
        }
    }

    /**
     * Displays every task in the given task list.
     *
     * @param list the task list to display
     */
    public void printList(TodoList list) {
        System.out.println(list);
    }

    /**
     * Returns a string of every task in the given task list.
     *
     * @param list the task list to return as a string
     */
    public String getListString(TodoList list) {
        return list.toString();
    }

    /**
     * Displays every task in the given search result list.
     *
     * @param list the task list to display
     */
    public void printSearchList(TodoList list) {
        System.out.println("\nHere are the matching tasks:");
        System.out.println(list);
    }

    /**
     * Returns a string of every task in the given search result list.
     *
     * @param list the task list to return as string
     */
    public String getSearchListString(TodoList list) {
        return "\nHere are the matching tasks:" + "\n" + list.toString();
    }

    /**
     * Displays the message used for an unrecognised command.
     */
    public void printInvalidCommandMessage() {
        System.out.println("Sorry, I don't understand that.");
    }

    /**
     * Displays the message used for an unrecognised command.
     */
    public String getInvalidCommandMessage() {
        return "Sorry, I don't understand that.";
    }
}
