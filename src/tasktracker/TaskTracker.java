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
                // call addTask() method
                addTask(args[1]);
                break;
            
            case "update":
                if (args.length < 3) {
                    System.out.println("Error: Please provide a task ID and a new task description");
                    printUsage();
                    return;
                }
                int taskId = Integer.parseInt(args[1]);  // task ID to update
                String newDescription = args[2]; // new description for the task
                updateTask(taskId, newDescription);
                break;

                case "delete":
                // Check if an ID is provided
                if (args.length < 2) {
                    System.out.println("Error: Please provide the task ID to delete.");
                    printUsage();
                    return;
                }
                try {
                    int id = Integer.parseInt(args[1]);
                    deleteTask(id);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Task ID must be a number.");
                }
                break;
                
            case "mark-in-progress":
            case "mark-done":
                if (args.length < 2) {
                    System.out.println("Error: Please provide the task ID to mark.");
                    printUsage();
                    return;
                }
                try {
                    int id = Integer.parseInt(args[1]);
                    TaskStatus status = command.equals("mark-in-progress") ? TaskStatus.IN_PROGRESS : TaskStatus.DONE;
                    markTaskStatus(id, status);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Task ID must be a number.");
                }
                break;

            case "list":
            if (args.length < 2) {
                listTasks(); // List all tasks if no specific status is provided
            } else {
                try {
                    TaskStatus status = TaskStatus.valueOf(args[1].toUpperCase().replace("-", "_"));
                    listTasksByStatus(status);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: Invalid status. Use 'todo', 'in-progress', or 'done'.");
                }
            }
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
        System.out.println("  java TaskTrackerCLI add \"Task description\"             - Add a new task with a description.");
        System.out.println("  java TaskTrackerCLI update <task-id> \"New task description\" - Update the description of an existing task.");
        System.out.println("  java TaskTrackerCLI delete <task-id>                     - Delete a task by its ID.");
        System.out.println("  java TaskTrackerCLI mark-in-progress <task-id>            - Mark a task as 'in-progress'.");
        System.out.println("  java TaskTrackerCLI mark-done <task-id>                   - Mark a task as 'done'.");
        System.out.println("  java TaskTrackerCLI list                                  - List all tasks.");
        System.out.println("  java TaskTrackerCLI list <status>                         - List tasks by status (todo, in-progress, done).");
        System.out.println("  Valid statuses are: 'todo', 'in-progress', 'done'.");
        System.out.println("  Example usage:");
        System.out.println("    java TaskTrackerCLI add \"Buy groceries\"");
        System.out.println("    java TaskTrackerCLI list");
        System.out.println("    java TaskTrackerCLI list done");
        System.out.println("    java TaskTrackerCLI update 1 \"Buy groceries and cook dinner\"");
        System.out.println("    java TaskTrackerCLI mark-in-progress 1");
        System.out.println("    java TaskTrackerCLI delete 1");
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
                jsonBuilder.append("," + System.lineSeparator());
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
            int newId = tasks.size() + 1;

            // create new task 
            Task newTask = new Task(newId, description);

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

    private static void updateTask(int taskId, String newDescription) {
        try {
            // load existing tasks
            List<Task> tasks = loadTasks();

            // find the task by ID
            Task taskToUpdate = null;
            for (Task task  : tasks) {
                if (task.getId() == taskId) {
                    taskToUpdate = task;
                    break;
                }
            }

            // check if task was found
            if (taskToUpdate == null) {
                System.out.println("Error: Task with ID " + taskId + " not found.");
                return;
            }

            // update the task description and timestamp
            taskToUpdate.setDescription(newDescription);

            // save the updated tasks back to the file
            saveTasks(tasks);

            System.out.println("Task updated successfully: " + taskToUpdate);
        } catch (IOException e) {
            System.err.println("Error updating task: " + e.getMessage());
        }
    }

    private static void deleteTask(int id) {
        try {
            // load existing tasks
            List<Task> tasks = loadTasks();
    
            // find the task to delete
            Task taskToDelete = null;
            for (Task task : tasks) {
                if (task.getId() == id) {
                    taskToDelete = task;
                    break;
                }
            }
    
            // if task not found, display error message
            if (taskToDelete == null) {
                System.out.println("Error: Task with ID " + id + " not found.");
                return;
            }
    
            // remove the task and save the updated list
            tasks.remove(taskToDelete);
            saveTasks(tasks);
    
            // print success message
            System.out.println("Task with ID " + id + " deleted successfully.");
        } catch (IOException e) {
            System.err.println("Error deleting task: " + e.getMessage());
        }
    }
    
    private static void markTaskStatus(int id, TaskStatus newStatus) {
        try {
            // load existing tasks
            List<Task> tasks = loadTasks();
    
            // find the task
            Task taskToMark = null;
            for (Task task : tasks) {
                if (task.getId() == id) {
                    taskToMark = task;
                    break;
                }
            }
    
            // if task is not found, display error
            if (taskToMark == null) {
                System.out.println("Error: Task with ID " + id + " not found.");
                return;
            }
    
            // update the task status
            taskToMark.setStatus(newStatus);
            saveTasks(tasks);
    
            // print success message
            System.out.println("Task with ID " + id + " marked as " + newStatus + ".");
        } catch (IOException e) {
            System.err.println("Error updating task status: " + e.getMessage());
        }
    }

    private static void listTasksByStatus(TaskStatus status) {
        try {
            // load existing tasks
            List<Task> tasks = loadTasks();
    
            // filter tasks by status
            boolean found = false;
            for (Task task : tasks) {
                if (task.getStatus() == status) {
                    System.out.println(task);
                    found = true;
                }
            }
    
            // if no tasks found for the status
            if (!found) {
                System.out.println("No tasks found with status: " + status);
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }
    
    
}
