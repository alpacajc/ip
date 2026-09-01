package echo;

import java.util.ArrayList;

public class TodoList {
    private ArrayList<Task> list = new ArrayList<>();
    private static final String LINE = "\n" + "-".repeat(30) + "\n";
    private static final String ENDLINE = "\n" + "-".repeat(30);

    public TodoList() {};
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
