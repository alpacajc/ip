package echo;

import java.util.ArrayList;
import java.util.ArrayDeque;

/**
 * Runs the Echo command-line task manager.
 *
 * <p>This class reads commands from the user, updates the task list, saves changes to storage,
 * and displays feedback through the user interface. The session ends when the user enters the
 * {@code bye} command.</p>
 */
public class Echo {
    private TodoList todoList = new TodoList(this);
    protected Storage store = new Storage("testdata.txt", todoList);
    private Ui ui = new Ui();

    private final int undoLimit = 1;
    private final int numSavedStates = undoLimit + 1;
    protected ArrayDeque<Runnable> previousActions = new ArrayDeque<Runnable>(numSavedStates);

    /**
     * Represents the command words supported by Echo.
     */
    protected enum CommandWord {
        BYE("bye"),
        LIST("list"),
        MARK("mark"),
        UNMARK("unmark"),
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event"),
        DELETE("delete"),
        FIND("find"),
        UNDO("undo"),
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
            assert input != null;
            for (CommandWord cmdword : values()) {
                if (cmdword.cmd.equals(input)) {
                    return cmdword;
                }
            }
            return INVALID;
        }
    }

    /**
     * Parses and interprets user input, then handles the primary logic of what the user input does.
     *
     * @param input the input entered by the user
     * @return Echo's text response to input commands in the form of a String
     */
    protected String getResponse(String input) {
        assert input != null;

        String[] commandArgs = Parser.convertInputToArgs(input);
        String command = commandArgs[0];

        int todoListSize = todoList.getSize();

        if (previousActions.size() > numSavedStates) {
            previousActions.pop();
        }

        System.out.println(previousActions);

        try {
            CommandWord cmdword = CommandWord.fromString(command);
            switch (cmdword) {
                case BYE -> {
                    return Ui.getFarewell();
                }
                case LIST -> {
                    return ui.getListString(this.todoList);
                }
                case MARK -> {
                    int taskNum = Parser.getTaskNum(todoListSize, commandArgs);
                    this.todoList.markList(taskNum);
                    this.store.writeData(this.todoList.getList());
                    return ui.getMarkString(todoList.getTask(taskNum - 1), true);
                }
                case UNMARK -> {
                    int taskNum = Parser.getTaskNum(todoListSize, commandArgs);
                    this.todoList.unmarkList(taskNum);
                    this.store.writeData(this.todoList.getList());
                    return ui.getMarkString(todoList.getTask(taskNum - 1), false);
                }
                case TODO -> {
                    String desc = Parser.getTaskArgs(input, command)[0];
                    Todo newTask = new Todo(desc);
                    this.todoList.addToList(newTask);
                    this.store.writeData(this.todoList.getList());
                    return ui.getAddedTaskString(newTask, cmdword.toString());
                }
                case DEADLINE -> {
                    String[] taskArgs = Parser.getTaskArgs(input, command);
                    assert taskArgs.length > 1;
                    Deadline newTask = new Deadline(taskArgs);
                    this.todoList.addToList(newTask);
                    this.store.writeData(this.todoList.getList());
                    return ui.getAddedTaskString(newTask, cmdword.toString());
                }
                case EVENT -> {
                    String[] taskArgs = Parser.getTaskArgs(input, command);
                    assert taskArgs.length > 1;
                    Task newTask = new Event(taskArgs);
                    this.todoList.addToList(newTask);
                    this.store.writeData(this.todoList.getList());
                    return ui.getAddedTaskString(newTask, cmdword.toString());
                }
                case DELETE -> {
                    int taskNum = Parser.getTaskNum(todoListSize, commandArgs);
                    Task deletedTask = this.todoList.deleteTask(taskNum);
                    int listSize = this.todoList.getSize();
                    assert this.todoList.getSize() < todoListSize;
                    this.store.writeData(this.todoList.getList());
                    return ui.getDeleteTask(deletedTask, listSize);
                }
                case FIND -> {
                    String keyword = Parser.getKeyword(input, command);
                    TodoList searchList = this.todoList.findSearchList(keyword);
                    assert searchList.getSize() <= this.todoList.getSize();
                    return ui.getSearchListString(searchList);
                }
                case UNDO -> {
                    previousActions.pop().run();
                    return "undid the previous command.";
                }
                default -> {
                    throw new InvalidCommandException();
                }
            }
        } catch (InvalidCommandException e) {
            return ui.getInvalidCommandMessage(command);
        }
    }
}

/**
 * Parses user input into commands and their associated arguments.
 */
class Parser {

    /**
     * Splits an input line into arguments and converts the command to lowercase.
     *
     * @param input the user input to parse
     * @return the command and its arguments
     */
    public static String[] convertInputToArgs(String input) {
        String[] commandArgs = input.trim().split(" ");
        commandArgs[0] = commandArgs[0].toLowerCase();
        return commandArgs;
    }

    /**
     * Returns the task number from the most recently parsed command.
     *
     * @return the task number specified in the command
     * @throws InvalidCommandException if the command does not include a task number
     * @throws NumberFormatException if the task number is not a valid integer
     */
    public static int getTaskNum(int todoListSize, String[] commandArgs) throws InvalidCommandException {
        try {
            int taskNum = Integer.parseInt(commandArgs[1]);
            if (taskNum > todoListSize) {
                throw new InvalidCommandException();
            }
            return taskNum;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new InvalidCommandException();
        }
    }

    public static String getKeyword(String input, String command) throws InvalidCommandException {
        if (input.equals(command)) {
            throw new InvalidCommandException();
        }
        String[] commandArgs = input.trim()
                .substring(command.length() + 1).split(" ");
        if (commandArgs.length >= 1) {
            return String.join(" ", commandArgs);
        } else {
            throw new InvalidCommandException();
        }
    }

    /**
     * Extracts the task arguments without any slash-prefixed details from a task-creating command.
     *
     * @param input the complete user input
     * @param command the command prefix to remove from the input
     * @return the description and optional details, split at {@code " /"}
     * @throws InvalidCommandException if the command has no task description
     */
    public static String[] getTaskArgs(String input, String command) throws InvalidCommandException {
        if (input.trim().equals(command)) {
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

    /**
     * Extracts the task arguments without any double slash-prefixed details from the storage text file.
     *
     * @param input the task in storage format
     * @param command the command prefix to remove from the input
     * @return the description and optional details, split at {@code " // "}
     * @throws InvalidCommandException if the stored task is invalid
     */
    public static String[] getStoredTaskArgs(String input, String command) throws InvalidCommandException {
        if (input.trim().equals(command)) {
            throw new InvalidCommandException();
        }
        String[] commandArgs = input.trim()
                .substring(command.length() + 1).split(" /");
        if (commandArgs.length >= 2) {
            ArrayList<String> taskArgs= new ArrayList<String>();
            for (int i = 1; i < commandArgs.length; i++) {
                if (!commandArgs[i].equals("null")) {
                    taskArgs.add(commandArgs[i]);
                }
            }
            return taskArgs.toArray(String[]::new);
        } else {
            throw new InvalidCommandException();
        }
    }
}

class InvalidCommandException extends IllegalArgumentException {
}

