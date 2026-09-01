package echo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
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
