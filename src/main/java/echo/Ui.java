package echo;

import java.util.Scanner;

public class Ui {
    private Scanner scanner = new Scanner(System.in);
    private static final String NAME = "Echo";
    private static final String LINE = "\n" + "-".repeat(30) + "\n";
    private static final String ENDLINE = "\n" + "-".repeat(30);

    public Ui() {};
    public String readCommand() {
        return this.scanner.nextLine();
    }

    public void printWelcome() {
        String banner = " _____     _           \n"
                + "| ____|___| |__   ___  \n"
                + "|  _| / __| '_ \\ / _ \\ \n"
                + "| |__| (__| | | | (_) |\n"
                + "|_____\\___|_| |_|\\___/ \n";
        String welcomeMessage = LINE + banner + LINE + String.format("Hi, I'm %s.\nWhat can I do for you?\n",
                NAME) + ENDLINE;
        System.out.println(welcomeMessage);
    }

    public void printFarewell() {
        String farewellMessage = "Goodbye" + LINE;
        System.out.println(farewellMessage);
    }

    public void printAddedTask(Task task, String type) {
        System.out.println(String.format("Added this %s task:\n  %s",
                type, task));
    }

    public void printDeleteTask(Task deletedTask, int size) {
        System.out.println(String.format("Deleted this task\n  %s\nNow you have %d tasks left",
                deletedTask, size));
    }

    public void printMark(Task task, boolean isMarked) {
        if (isMarked) {
            System.out.println(String.format("Marked this task as done:\n  %s",
                    task));
        }
        else {
            System.out.println(String.format("Marked this task as not done:\n  %s",
                    task));
        }
    }

    public void printList(TodoList list) {
        System.out.println(list);
    }

    public void printInvalidCommandMessage() {
        System.out.println("Sorry, I don't understand that.");
    }
}
