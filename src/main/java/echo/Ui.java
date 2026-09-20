package echo;

/**
 * Handles command-line input and output for Echo.
 */
public class Ui {
    private static final String NAME = "Echo";
    private static final String LINE = "\n" + "-".repeat(30) + "\n";
    private static final String ENDLINE = "\n" + "-".repeat(30);

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
    }

    /**
     * Returns Echo's welcome message.
     */
    public static String getWelcome() {
        return LINE + String.format("Hi, I'm %s.\nWhat can I do for you?\n",
                NAME) + ENDLINE;
    }

    /**
     * @return Echo's farewell message
     */
    public static String getFarewell() {
        return "Goodbye" + LINE;
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
     * Returns a string of every task in the given task list.
     *
     * @param list the task list to return as a string
     */
    public String getListString(TodoList list) {
        return list.toString();
    }

    /**
     * Returns a string of every task in the given search result list.
     *
     * @param list the task list to return as string
     */
    public String getSearchListString(TodoList list) {
        return "\nHere are the matching tasks:" + "\n" + list.toString();
    }
}
