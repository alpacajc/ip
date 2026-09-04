package echo;

import java.time.DateTimeException;

/**
 * Runs the Echo command-line task manager.
 *
 * <p>This class reads commands from the user, updates the task list, saves changes to storage,
 * and displays feedback through the user interface. The session ends when the user enters the
 * {@code bye} command.</p>
 */
public class Echo {
    private static boolean endSession = false;
    private TodoList todoList = new TodoList();
    private Storage store = new Storage("testdata.txt", todoList);
    private Ui ui = new Ui();
    private Parser parser = new Parser();

    /**
     * Represents the command words supported by Echo.
     */
    public enum CommandWord {
        BYE("bye"),
        LIST("list"),
        MARK("mark"),
        UNMARK("unmark"),
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event"),
        DELETE("delete"),
        INVALID("invalid");

        public String cmd;

        CommandWord(String cmd) {
            this.cmd = cmd;
        }

        /**
         * Returns the command word that corresponds to the given input.
         *
         * @param input the command word entered by the user
         * @return the matching command word, or {@link #INVALID} when no command matches
         */
        public static CommandWord fromString(String input) {
            for (CommandWord cmdword : values()) {
                if (cmdword.cmd.equals(input)) {
                    return cmdword;
                }
            }
            return INVALID;
        }
    }

    private void start() {
        try {
            while (!endSession) {
                String input = ui.readCommand();
                String[] commandArgs = parser.parseInput(input);
                String command = commandArgs[0];
                CommandWord cmdword = CommandWord.fromString(command);
                switch (cmdword) {
                    case BYE -> {
                        endSession = true;
                        this.ui.printFarewell();
                        break;
                    }
                    case LIST -> {
                        ui.printList(this.todoList);
                    }
                    case MARK -> {
                        try {
                            int taskNum = parser.parseTaskNum();
                            this.todoList.markList(taskNum);
                            ui.printMark(todoList.getTask(taskNum - 1), true);
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid input for mark. Example usage: mark 2");
                        }
                    }
                    case UNMARK -> {
                        try {
                            int taskNum = parser.parseTaskNum();
                            this.todoList.unmarkList(taskNum);
                            ui.printMark(todoList.getTask(taskNum - 1), false);
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid input for unmark. Example usage: unmark 2");
                        }
                    }
                    case TODO -> {
                        try {
                            String desc = parser.parseTask(input, command)[0];
                            Todo newTask = new Todo(desc);
                            this.todoList.addToList(newTask);
                            ui.printAddedTask(newTask, cmdword.toString());
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
                            System.out.println("Invalid input for todo. Example usage: todo Example");
                        }
                    }
                    case DEADLINE -> {
                        try {
                            String[] desc = parser.parseTask(input, command);
                            Deadline newTask;
                            if (desc.length > 2) {
                                newTask = new Deadline(desc[0], desc[1], desc[2]);
                            } else {
                                newTask = new Deadline(desc[0], desc[1]);
                            }
                            this.todoList.addToList(newTask);
                            ui.printAddedTask(newTask, cmdword.toString());
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException | StringIndexOutOfBoundsException | DateTimeException e) {
                            System.out.println("Invalid input for deadline. Example usage: deadline Example /2023-12-13");
                        }
                    }
                    case EVENT -> {
                        try {
                            String[] desc = parser.parseTask(input, command);
                            Task newTask;
                            if (desc.length > 4) {
                                newTask = new Event(desc[0], desc[1], desc[2], desc[3], desc[4]);
                            } else {
                                newTask = new Event(desc[0], desc[1], desc[2]);
                            }
                            this.todoList.addToList(newTask);
                            ui.printAddedTask(newTask, cmdword.toString());
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException | StringIndexOutOfBoundsException | DateTimeException e) {
                            System.out.println("Invalid input for event. Example usage: event Example /2023-12-13 /2023-12-14");
                        }
                    }
                    case DELETE -> {
                        try {
                            int taskNum = parser.parseTaskNum();
                            Task deletedTask = todoList.deleteTask(taskNum);
                            int listSize = todoList.getSize();
                            ui.printDeleteTask(deletedTask, listSize);
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
                            System.out.println("Invalid input for delete. Example usage: delete 2");
                        }
                    }
                    case INVALID -> {
                        throw new InvalidCommandException();
                    }
                }
            }
        }
        catch (InvalidCommandException e) {
            ui.printInvalidCommandMessage();
            this.start();
        }
    }

    /**
     * Starts Echo by loading saved tasks, displaying a welcome message, and processing commands.
     *
     * @param args command-line arguments; Echo does not use them
     */
    public static void main(String[] args) {
        Echo echo = new Echo();
        echo.store.readData();
        echo.ui.printWelcome();
        echo.start();
    }
}

/**
 * Parses user input into commands and their associated arguments.
 */
class Parser {

    /** Stores the arguments from the most recently parsed command. */
    String[] commandArgs;

    /**
     * Splits an input line into arguments and converts the command to lowercase.
     *
     * @param input the user input to parse
     * @return the command and its arguments
     */
    public String[] parseInput(String input) {
        String[] commandArgs = input.trim().split(" ");
        commandArgs[0] = commandArgs[0].toLowerCase();
        this.commandArgs = commandArgs;
        return commandArgs;
    }

    /**
     * Returns the task number from the most recently parsed command.
     *
     * @return the task number specified in the command
     * @throws InvalidCommandException if the command does not include a task number
     * @throws NumberFormatException if the task number is not a valid integer
     */
    public int parseTaskNum() throws InvalidCommandException {
        if (commandArgs.length > 1) {
            return Integer.parseInt(commandArgs[1]);
        } else {
            throw new InvalidCommandException();
        }
    }

    /**
     * Extracts a task description and any slash-prefixed details from a command.
     *
     * @param input the complete user input
     * @param command the command prefix to remove from the input
     * @return the description and optional details, split at {@code " /"}
     * @throws InvalidCommandException if the command has no task description
     */
    public String[] parseTask(String input, String command) throws InvalidCommandException {
        if (input.equals(command)) {
            throw new InvalidCommandException();
        }
        String[] commandArgs = input.trim()
                .substring(command.length() + 1).split(" /");
        if (commandArgs.length >= 1) {
            return commandArgs;
        } else {
            throw new InvalidCommandException();
        }
    }
}

class InvalidCommandException extends IllegalArgumentException {
    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException() {
    }
}

