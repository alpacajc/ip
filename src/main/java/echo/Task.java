package echo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    /**
     * Returns this task's description.
     *
     * @return the task description
     */
    public String getDesc() {
        return description;
    }
    /**
     * Returns whether this task is complete in storage-friendly form.
     *
     * @return {@code "true"} when complete; otherwise {@code "false"}
     */
    public String getStatus() {
        return String.valueOf(isDone);
    }
    /**
     * Returns the visual marker for this task's completion status.
     *
     * @return {@code "[X]"} when complete, otherwise {@code "[ ]"}
     */
    public String getStatusMarker() {
        return this.isDone ? "[X]" : "[ ]";
    }
    /**
     * Marks this task as complete.
     */
    public void mark() {
        this.isDone = true;
    }
    /**
     * Marks this task as incomplete.
     */
    public void unmark() {
        this.isDone = false;
    }
    /**
     * Converts this task to the format used when saving it to storage.
     *
     * @return this task's storage representation
     */
    public String toStorageFormat() {
        return String.join(" // ",
                new String[]{
                        "T",
                        String.valueOf(isDone),
                        description
                }
        );
    }
    /**
     * Returns the formatted representation shown to the user.
     *
     * @return this task's display representation
     */
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
        return String.format("%s%s %s",
                this.taskMarker,
                super.getStatusMarker(),
                super.getDesc());
    }
}

/**
 * Creates a deadline task with a description and due date.
 */
class Deadline extends Task {
    String taskMarker = "[D]";

    String deadline;
    String time = "";

    LocalDate deadlineDate;

    String formattedDate;

    public Deadline(String... taskArgs) {
        super(taskArgs[0]);
        if (taskArgs.length < 2) {
            throw new InvalidCommandException();
        }
        this.deadline = taskArgs[1];
        try {
            this.deadlineDate = LocalDate.parse(deadline);
        } catch (DateTimeParseException dateTimeParseException) {
            throw new InvalidCommandException();
        }
        if (taskArgs.length > 2) {
            this.time = taskArgs[2];
            int timeInt = Integer.parseInt(time);
            this.formattedDate = this.deadlineDate
                    .atTime(Math.floorDiv(timeInt, 100), timeInt % 100)
                    .format(DateTimeFormatter.ofPattern("dd MMM yyyy hhmma"));
        }
        else {
            this.formattedDate = this.deadlineDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        }
    }

    @Override
    public String toString() {
        return String.format("%s%s %s (By: %s)",
                this.taskMarker,
                this.getStatusMarker(),
                super.getDesc(),
                this.formattedDate);
    }
    @Override
    public String toStorageFormat() {
        return String.join(" // ",
                new String[]{
                    "D",
                    super.getStatus(),
                    super.getDesc(),
                    deadline,
                    time
                }
        );
    }
}

/**
 * Creates an event item with a from time and to time.
 */
class Event extends Task {
    String taskMarker = "[E]";

    String from;
    String to;
    String fromTime;
    String toTime;

    LocalDate fromDate;
    LocalDate toDate;

    String formattedFromDate;
    String formattedToDate;

    public Event(String... taskArgs) {
        super(taskArgs[0]);
        if (taskArgs.length < 3) {
            throw new InvalidCommandException();
        }
        this.from = taskArgs[1];
        this.to = taskArgs[2];
        try {
            this.fromDate = LocalDate.parse(from);
            this.toDate = LocalDate.parse(to);
        } catch (DateTimeParseException dateTimeParseException) {
            throw new InvalidCommandException();
        }
        if (taskArgs.length >= 5) {
            this.fromTime = taskArgs[3];
            this.toTime = taskArgs[4];

            int fromTimeInt = Integer.parseInt(fromTime);
            int toTimeInt = Integer.parseInt(toTime);

            this.formattedFromDate = this.fromDate
                    .atTime(Math.floorDiv(fromTimeInt, 100), fromTimeInt % 100)
                    .format(DateTimeFormatter.ofPattern("dd MMM yyyy hhmma"));
            this.formattedToDate = this.toDate
                    .atTime(Math.floorDiv(toTimeInt, 100), toTimeInt % 100)
                    .format(DateTimeFormatter.ofPattern("dd MMM yyyy hhmma"));
        } else {
            this.formattedFromDate = this.fromDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            this.formattedToDate = this.toDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        }
    }

    @Override
    public String toString() {
        return String.format("%s%s %s (from: %s to: %s)",
                this.taskMarker,
                this.getStatusMarker(),
                super.getDesc(),
                this.formattedFromDate,
                this.formattedToDate);
    }
    @Override
    public String toStorageFormat() {
        return String.join(" // ",
                new String[]{
                    "E",
                    super.getStatus(),
                    super.getDesc(),
                    this.from,
                    this.to,
                    this.fromTime,
                    this.toTime
                }
        );
    }
}
