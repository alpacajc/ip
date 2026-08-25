public class Echo {
    private static final String name = "Echo";
    private static final String line = "\n" + "-".repeat(30) + "\n";
    private static final String line2 = "\n" + "-".repeat(30);
    public static void main(String[] args) {
        String banner = " _____     _           \n"
                + "| ____|___| |__   ___  \n"
                + "|  _| / __| '_ \\ / _ \\ \n"
                + "| |__| (__| | | | (_) |\n"
                + "|_____\\___|_| |_|\\___/ \n";
        String welcomeMessage = line + banner + line + String.format("Hi, I'm %s.\nWhat can I do for you?\n",
                name) + line2;
        String farewellMessage = "Goodbye, hope to see you again" + line;
        System.out.println(welcomeMessage);
        System.out.println(farewellMessage);

    }
}
