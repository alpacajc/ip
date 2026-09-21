package echo;

/**
 * Creates a task item with no time or date associated with it.
 */
public class Todo extends Task {
    private final String taskMarker = "[T]";

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
