package echo;

import java.util.ArrayList;

/**
 * Stores and manages Echo's tasks in their display order.
 */
public class TodoList {
    private ArrayList<Task> listOfTasks = new ArrayList<>();

    private static final String LINE = "\n" + "-".repeat(30) + "\n";
    private static final String ENDLINE = "\n" + "-".repeat(30);

    /**
     * Adds a task to the end of this list.
     *
     * @param item the task to add
     */
    public void addToList(Task item) {
        assert item != null;

        listOfTasks.add(item);
        System.out.println(String.format("\nThere are now %d items in the list",
                listOfTasks.size()));
    }
    /**
     * Marks the specified one-based task number as complete.
     *
     * @param taskNum the one-based number of the task to mark
     * @throws IllegalArgumentException if the task number is outside this list
     */
    public void markList(int taskNum) {
        if (listOfTasks.size() < taskNum || taskNum < 1) {
            throw new IllegalArgumentException();
        }
        listOfTasks.get(taskNum - 1).mark();
    }
    /**
     * Marks the specified one-based task number as incomplete.
     *
     * @param taskNum the one-based number of the task to unmark
     * @throws IllegalArgumentException if the task number is outside this list
     */
    public void unmarkList(int taskNum) {
        if (listOfTasks.size() < taskNum || taskNum < 1) {
            throw new IllegalArgumentException();
        }
        listOfTasks.get(taskNum - 1).unmark();
    }
    /**
     * Removes and returns the task at the specified one-based task number.
     *
     * @param taskNum the one-based number of the task to remove
     * @return the removed task
     * @throws IllegalArgumentException if the task number is outside this list
     */
    public Task deleteTask(int taskNum) {
        if (listOfTasks.size() < taskNum || taskNum < 1) {
            throw new IllegalArgumentException();
        }
        return this.listOfTasks.remove(taskNum - 1);
    }

    /**
     *
     * @param keyword the keyword used to find matching task descriptions
     * @return a TodoList that has all Tasks in the calling TodoList with
     * descriptions containing the keyword
     */
    public TodoList findSearchList(String keyword) {
        TodoList searchList = new TodoList();
        for (Task currentTask : this.listOfTasks) {
            if (currentTask.getDesc().contains(keyword)) {
                searchList.addToList(currentTask);
            }
        }
        return searchList;
    }
    /**
     * Returns the tasks in this list.
     *
     * @return the list that stores the tasks
     */
    public ArrayList<Task> getList() {
        return this.listOfTasks;
    }
    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index the zero-based position of the task
     * @return the task at the given index
     */
    public Task getTask(int index){
        return this.listOfTasks.get(index);
    }
    /**
     * Returns a numbered, formatted representation of this task list.
     *
     * @return the formatted task list
     */
    @Override
    public String toString() {
        int len = listOfTasks.size();

        String output = "";

        for (int i = 0; i < len; i ++) {
            Task currentTask = listOfTasks.get(i);
            output += String.format("%d. %s\n",
                    i + 1,
                    currentTask);
        }
        return LINE + output + ENDLINE;
    }
    /**
     * Returns the number of tasks in this list.
     *
     * @return the task count
     */
    protected int getSize() {
        return listOfTasks.size();
    }
}
