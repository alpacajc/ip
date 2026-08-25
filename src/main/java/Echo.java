import java.util.Scanner;

/**
 * Reads user input, repeats the input and ends session when 'bye' command is received
 */
public class Echo {
    private static final String NAME = "Echo";
    private static final String LINE = "\n" + "-".repeat(30) + "\n";
    private static final String ENDLINE = "\n" + "-".repeat(30);
    private static boolean endSession = false;
    public static void main(String[] args) {
        String banner = " _____     _           \n"
                + "| ____|___| |__   ___  \n"
                + "|  _| / __| '_ \\ / _ \\ \n"
                + "| |__| (__| | | | (_) |\n"
                + "|_____\\___|_| |_|\\___/ \n";
        String welcomeMessage = LINE + banner + LINE + String.format("Hi, I'm %s.\nWhat can I do for you?\n",
                NAME) + ENDLINE;
        String farewellMessage = "Goodbye" + LINE;
        System.out.println(welcomeMessage);
        Scanner scanner = new Scanner(System.in);
        while (!endSession) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                endSession = true;
                System.out.println(farewellMessage);
                break;
            }
            System.out.println(LINE + input + ENDLINE);
        }
        scanner.close();
    }
}
