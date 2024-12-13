package tasktracker;

import java.io.*;
import java.util.*;

public class TaskTracker {
    private static final String TASKS_FILE = "data\\tasks.json";


    public static void main(String[] args) {
        // check if no arguments are provided
        if (args.length < 1) {
            printUsage();
            return;
        }

        // get the first argument (the command)
        String command = args[0];

        // basic command routing using switch statement
        switch (command) {
            case "add":
                // check if a description is provided
                if (args.length < 2) {
                    System.out.println("Error: Please provide a task description.");
                    printUsage();
                    return;
                }
                // call addTask() method (to be implemented later)
                addTask(args[1]);
                break;
            
            case "list":
                // call listTasks() method (to be implemented later)
                listTasks();
                break;
            
            default:
                // for any unknown command, show usage
                System.out.println("Unknown command: " + command);
                printUsage();
        }
    }

    // method to print usage instructions
    private static void printUsage() {
        System.out.println("Task Tracker CLI Usage:");
        System.out.println("  java TaskTrackerCLI add \"Task description\"");
        System.out.println("  java TaskTrackerCLI list");
        // more commands will be added later
    }

    private static List<Task> loadTasks() throws IOException {
        File file = new File(TASKS_FILE);
        // create an empty file if it doesn't exist
        if (!file.exists()) {
            file.createNewFile(); 
            return new ArrayList<>();
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();

        // parse JSON manually
        String json = jsonBuilder.toString().trim();
        if (json.isEmpty() || json.equals("[]")) {
            return new ArrayList<>();
        }
        
        List<Task> tasks = new ArrayList<>();
        String[] taskEntries = json.substring(1, json.length() - 1).split("\\},\\{");

        for (String taskEntry : taskEntries) {
            taskEntry = taskEntry.replace("{", "").replace("}", "");
            String[] fields = taskEntry.split(",");
            int id = Integer.parseInt(fields[0].split(":")[1].trim());
            String description = fields[1].split(":")[1].trim().replace("\"", "");
            String status = fields[2].split(":")[1].trim().replace("\"", "");
            tasks.add(new Task(id, description, TaskStatus.valueOf(status)));   
        }

        return tasks;
    }

    private static void saveTasks(List<Task> tasks) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            jsonBuilder.append(task.toJson());
            if (i < tasks.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");

        BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE));
        writer.write(jsonBuilder.toString());
        writer.close();
    }

    private static void listTasks() {
        try {
            List<Task> tasks = loadTasks();
            if (tasks.isEmpty()) {
                System.out.println("No tasks found.");
                return;
            }
            for (Task task : tasks) {
                System.out.println(task);
            }
        } catch (IOException e) {
            System.err.println("Error reading tasks: " + e.getMessage());
        }
    }

    private static void addTask(String description) {
        try {
            // load existing tasks
            List<Task> tasks = loadTasks();

            // generate unique ID
            int newID = tasks.size() + 1;

            // create new task 
            Task newTask = new Task(newID, description);

            // add to the task list
            tasks.add(newTask);

            // save updated task list to file
            saveTasks(tasks);
            
            // print success message
            System.out.println("Task added successfuly: " + newTask);
        } catch (IOException e) {
            System.err.println("Error saving task: " + e.getMessage());
        }
    }
}
