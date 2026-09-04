package echo;

import java.util.ArrayList;

/**
 * Stores and manages Echo's tasks in their display order.
 */
public class TodoList {
    private ArrayList<Task> list = new ArrayList<>();
    private static final String LINE = "\n" + "-".repeat(30) + "\n";
    private static final String ENDLINE = "\n" + "-".repeat(30);

    /**
     * Creates an empty task list.
     */
    public TodoList() {
    };

    /**
     * Adds a task to the end of this list.
     *
     * @param item the task to add
     */
    public void addToList(Task item) {
        list.add(item);
        System.out.println(String.format("\nThere are now %d items in the list",
                list.size()));
    }
    /**
     * Marks the specified one-based task number as complete.
     *
     * @param taskNum the one-based number of the task to mark
     * @throws IllegalArgumentException if the task number is outside this list
     */
    public void markList(int taskNum) {
        if (list.size() < taskNum || taskNum < 1) {
            throw new IllegalArgumentException();
        }
        list.get(taskNum - 1).mark();
    }
    /**
     * Marks the specified one-based task number as incomplete.
     *
     * @param taskNum the one-based number of the task to unmark
     * @throws IllegalArgumentException if the task number is outside this list
     */
    public void unmarkList(int taskNum) {
        if (list.size() < taskNum || taskNum < 1) {
            throw new IllegalArgumentException();
        }
        list.get(taskNum - 1).unmark();
    }
    /**
     * Removes and returns the task at the specified one-based task number.
     *
     * @param taskNum the one-based number of the task to remove
     * @return the removed task
     * @throws IllegalArgumentException if the task number is outside this list
     */
    public Task deleteTask(int taskNum) {
        if (list.size() < taskNum || taskNum < 1) {
            throw new IllegalArgumentException();
        }
        return this.list.remove(taskNum - 1);
    }
    /**
     * Returns the tasks in this list.
     *
     * @return the list that stores the tasks
     */
    public ArrayList<Task> getList() {
        return this.list;
    }
    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index the zero-based position of the task
     * @return the task at the given index
     */
    public Task getTask(int index){
        return this.list.get(index);
    }
    /**
     * Returns a numbered, formatted representation of this task list.
     *
     * @return the formatted task list
     */
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
    /**
     * Returns the number of tasks in this list.
     *
     * @return the task count
     */
    public int getSize() {
        return list.size();
    }
}
