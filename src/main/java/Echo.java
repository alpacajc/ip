import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private Storage store = new Storage("testdata.txt", todoList);

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

        public static CommandWord fromString(String input) {
            input = input.toLowerCase().trim().split(" ")[0];
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
                String input = this.scanner.nextLine();
                String command = input.toLowerCase();
                CommandWord cmdword = CommandWord.fromString(command);
                switch (cmdword) {
                    case BYE -> {
                        endSession = true;
                        this.printFarewell();
                        break;
                    }
                    case LIST -> {
                        System.out.println(this.todoList);
                    }
                    case MARK -> {
                        try {
                            int taskNum = Integer.parseInt(command.substring(5).trim());
                            this.todoList.markList(taskNum);
                            System.out.println(String.format("Marked this task as done:\n  %s",
                                    todoList.getTask(taskNum - 1)));
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid input for mark. Example usage: mark 2");
                        }
                    }
                    case UNMARK -> {
                        try {
                            int taskNum = Integer.parseInt(command.substring(7).trim());
                            this.todoList.unmarkList(taskNum);
                            System.out.println(String.format("Marked this task as not done:\n  %s",
                                    todoList.getTask(taskNum - 1)));
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid input for unmark. Example usage: unmark 2");
                        }
                    }
                    case TODO -> {
                        try {
                            String desc = input.substring(5).trim();
                            if (desc.isEmpty()) {
                                throw new IllegalArgumentException();
                            }
                            Todo newTask = new Todo(desc);
                            this.todoList.addToList(newTask);
                            System.out.println(String.format("Added this todo task:\n  %s",
                                    newTask));
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid input for todo.");
                        }
                    }
                    case DEADLINE -> {
                        try {
                            String[] desc = input.substring(9).trim().split(" ");
                            if (desc.length < 2 || desc[0].isEmpty()) {
                                throw new IllegalArgumentException();
                            }
                            Deadline newTask;
                            if (desc.length > 2) {
                                newTask = new Deadline(desc[0], desc[1], desc[2]);
                            }
                            else {
                                newTask = new Deadline(desc[0], desc[1]);
                            }
                            this.todoList.addToList(newTask);
                            System.out.println(String.format("Added this deadline task:\n  %s",
                                    newTask));
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException | DateTimeException e) {
                            System.out.println("Invalid input for deadline.");
                        }
                    }
                    case EVENT -> {
                        try {
                            String[] desc = input.substring(6).trim().split(" ");
                            if (desc.length < 3 || desc[0].isEmpty()) {
                                throw new IllegalArgumentException();
                            }
                            Task newTask;
                            if (desc.length >= 5) {
                                newTask = new Event(desc[0], desc[1], desc[2], desc[3], desc[4]);
                            }
                            else {
                                System.out.println("test");
                                newTask = new Event(desc[0], desc[1], desc[2]);
                            }
                            this.todoList.addToList(newTask);
                            System.out.println(String.format("Added this event task:\n  %s",
                                    newTask));
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid input for event.");
                        }
                    }
                    case DELETE -> {
                        try {
                            int taskNum = Integer.parseInt(command.substring(7).trim());
                            System.out.println(String.format("Deleted this task\n  %s\nNow you have %d tasks left",
                                    todoList.deleteTask(taskNum), todoList.getSize()));
                            store.writeData(todoList.getList());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid input for delete. Example usage: delete 2");
                        }
                    }
                    case INVALID -> {
                        throw new InvalidCommandException("Invalid command");
                    }
                }
            }
        }
        catch (InvalidCommandException e) {
            System.out.println("Sorry, I don't understand that.");
            this.start();
        }
    }

    public static void main(String[] args) {
        Echo echo = new Echo();
        echo.store.readData();
        echo.printWelcome();
        echo.start();
    }
}

class Storage {
    File listFile;
    TodoList todoList;
    public Storage(String filePath, TodoList todoList) {
        this.listFile = new File(filePath);
        this.todoList = todoList;
    }
    public void fileExists() {
        System.out.println("File exists: " + listFile.exists());
    }
    // taskArgs: [type, marked, desc, others...]
    public void readData() {
        if (listFile.exists()) {
            System.out.println(listFile.getName() + " already exists");
            try {
                Scanner fileReader = new Scanner(listFile);
                while (fileReader.hasNext()) {
                    String nextLine = fileReader.nextLine();
                    String[] taskArgs = nextLine.split(" // ");
                    if (taskArgs.length < 3) {
                        continue;
                    }
                    String taskType = taskArgs[0];
                    String markedString = taskArgs[1];
                    boolean marked = Boolean.parseBoolean(markedString);
                    String taskDesc = taskArgs[2];
                    if (taskType.equals("T")) {
                        Task currentTask = new Todo(taskDesc);
                        if (marked) {
                            currentTask.mark();
                        }
                        todoList.addToList(currentTask);
                    }
                    else if (taskType.equals("D")) {
                        String deadline = taskArgs[3];
                        Task currentTask = new Deadline(taskDesc, deadline);
                        if (taskArgs.length > 4) {
                            String time = taskArgs[4];
                            currentTask = new Deadline(taskDesc, deadline, time);
                        }
                        if (marked) {
                            currentTask.mark();
                        }
                        todoList.addToList(currentTask);
                    }
                    else if (taskType.equals("E")) {
                        Task currentTask;
                        if (taskArgs.length > 6) {
                            String from = taskArgs[3];
                            String to = taskArgs[5];
                            String fromTime = taskArgs[4];
                            String toTime = taskArgs[6];
                            System.out.println("test");
                            currentTask = new Event(taskDesc, from, fromTime, to, toTime);
                        }
                        else {
                            String from = taskArgs[3];
                            String to = taskArgs[4];
                            currentTask = new Event(taskDesc, from, to);
                        }
                        if (marked) {
                            currentTask.mark();
                        }
                        todoList.addToList(currentTask);
                    }
                }
            }
            catch (FileNotFoundException e) {
                System.out.println("File not found");
            }
        }
        else {
            try {
                listFile.createNewFile();
                System.out.println("File created: " + listFile.getName());
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void writeData(ArrayList<Task> taskList) {
        try {
            FileWriter fw = new FileWriter(listFile);
            for (Task task : taskList) {
                fw.write(task.toStorageFormat() + "\n");
            }
            fw.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

class TodoList {
    private ArrayList<Task> list = new ArrayList<>();
    private static final String LINE = "\n" + "-".repeat(30) + "\n";
    private static final String ENDLINE = "\n" + "-".repeat(30);

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
    public Task deleteTask(int taskNum) {
        if (list.size() < taskNum || taskNum < 1) {
            throw new IllegalArgumentException();
        }
        return this.list.remove(taskNum - 1);
    }
    public ArrayList<Task> getList() {
        return this.list;
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
    public int getSize() {
        return list.size();
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
    public String getStatus() {return String.valueOf(isDone);}
    public String getStatusMarker() {
        return this.isDone ? "[X]" : "[ ]";
    }
    public void mark() {
        this.isDone = true;
    }
    public void unmark() {
        this.isDone = false;
    }
    public String toStorageFormat() {
        return String.join(" // ", new String[]{"T", String.valueOf(isDone),
        description});
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
    LocalDate deadlineDate;
    String formattedDate;
    String deadline;
    String time = "";

    public Deadline(String desc, String deadline) {
        super(desc);
        this.deadline = deadline;
        this.deadlineDate = LocalDate.parse(deadline);
        this.formattedDate = this.deadlineDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }
    public Deadline(String desc, String deadline, String time) {
        super(desc);
        this.deadline = deadline;
        this.time = time;
        int timeInt = Integer.parseInt(time);
        this.deadlineDate = LocalDate.parse(deadline);
        this.formattedDate = this.deadlineDate
                .atTime(Math.floorDiv(timeInt, 100), timeInt % 100)
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy hhmma"));
    }
    @Override
    public String toString() {
        return String.format("%s%s %s (By: %s)", this.taskMarker, this.getStatusMarker(),
                super.getDesc(), this.formattedDate);
    }
    @Override
    public String toStorageFormat() {
        return String.join(" // ", new String[]{"D", super.getStatus(),
                super.getDesc(), deadline, time});
    }
}

/**
 * Creates an event item with a from time and to time.
 */
class Event extends Task {
    String taskMarker = "[E]";
    String formattedFromDate;
    String formattedToDate;
    String from;
    String to;
    LocalDate fromDate;
    LocalDate toDate;

    public Event(String desc, String from, String to) {
        super(desc);
        this.from = from;
        this.to = to;
        this.fromDate = LocalDate.parse(from);
        this.toDate = LocalDate.parse(to);
        this.formattedFromDate = this.fromDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        this.formattedToDate = this.toDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }
    public Event(String desc, String from, String fromTime, String to, String toTime) {
        super(desc);
        this.from = from;
        this.to = to;
        this.fromDate = LocalDate.parse(from);
        this.toDate = LocalDate.parse(to);
        int fromTimeInt = Integer.parseInt(fromTime);
        int toTimeInt = Integer.parseInt(toTime);
        this.formattedFromDate = this.fromDate
                .atTime(Math.floorDiv(fromTimeInt, 100), fromTimeInt % 100)
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy hhmma"));
        this.formattedToDate = this.toDate
                .atTime(Math.floorDiv(toTimeInt, 100), toTimeInt % 100)
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy hhmma"));
    }
    @Override
    public String toString() {
        return String.format("%s%s %s (from: %s to: %s)", this.taskMarker, this.getStatusMarker(),
                super.getDesc(), this.formattedFromDate, this.formattedToDate);
    }
    @Override
    public String toStorageFormat() {
        return String.join(" // ", new String[]{"E", super.getStatus(),
                super.getDesc(), this.from, this.to});
    }
}

class InvalidCommandException extends IllegalArgumentException {
    public InvalidCommandException(String message) {
        super(message);
    }
}

