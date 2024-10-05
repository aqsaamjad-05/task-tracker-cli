import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks; // stores all tasks
    private static final String TASK_FILE_PATH = "data/tasks.json";

    // constructor - loads tasks from the file when TaskManager is created
    public TaskManager() {
        tasks = loadTasks();
    }   

    // adds a new task
    public void addTask(String description) {
        // sets the id of the task
        int id = tasks.size() + 1;
        // creates a new task with the ID and description
        Task newTask = new Task(id, description);
        // adds it to the list of tasks
        tasks.add(newTask);
        // saves the tasks to the JSON file
        saveTasks(); 
        // outputs a success message
        System.out.println("Task added successfully (ID: " + id + ")");
    }

    // updates a task 
    public void updateTask(int id, String newDescription) {
        // loop through the list of tasks
        for (Task task : tasks) {
            // if the task id matches the id being searched for
            if (task.getId() == id) {
                // update the description
                task.setDescription(newDescription);
                // save the updated list to the JSON file 
                saveTasks(); 
                // output a success message
                System.out.println("Task updated successfully (ID: " + id + ")");
                return;
            }
        }
        System.out.println("Task with ID " + id + " not found.");
    }

    // loads tasks from the JSON file
    private List<Task> loadTasks() {
        List<Task> taskList = new ArrayList<>();
        try {
            // read the entire content of the tasks.json file into a string
            String content = new String(Files.readAllBytes(Paths.get(TASK_FILE_PATH)));
           
            // check if the content is not empty
            if (!content.isEmpty()) {
                // remove "[" and "]" and split the content by "}," which separates each JSON object (task)
                String[] jsonTasks = content.replace("[", "").replace("]", "").split("},");
               
                // loop through eah task JSON string
                for (String jsonTask : jsonTasks) {
                    // if the task doesn't end with "}", append it back
                    if (!jsonTask.endsWith("}")) {
                        jsonTask += "}";
                    }
                    // convert the JSON string to a Task object and add it to the taskList
                    taskList.add(Task.fromJson(jsonTask));
                }
            }
        } catch (IOException e) {
            // if the file doesn't exist or there was an error reading it, start with an empty list
            System.out.println("No tasks file found, starting fresh.");
        }
        return taskList; // return the list of tasks (empty if no tasks were loaded)
    }

    // saves tasks to the JSON file with each attribute on its own line
    private void saveTasks() {
        StringBuilder json = new StringBuilder("[\n");

        // loop through each task in the list
        for (int i = 0; i < tasks.size(); i++) {
            // open the task object with indentation
            json.append("  {\n");
            json.append("    \"id\": ").append(tasks.get(i).getId()).append(",\n");
            json.append("    \"description\": \"").append(tasks.get(i).getDescription()).append("\",\n");
            json.append("    \"status\": \"").append(tasks.get(i).getStatus()).append("\",\n");
            json.append("    \"createdAt\": \"").append(tasks.get(i).getCreatedAt()).append("\",\n");
            json.append("    \"updatedAt\": \"").append(tasks.get(i).getUpdatedAt()).append("\"\n");

            // close the task object, and add a comma if it's not the last task
            json.append("  }");
            if (i < tasks.size() - 1) {
                json.append(",");
            }
            // move to the next line for the next task
            json.append("\n");
        }

        // end the array with a closing bracket
        json.append("]");

        try {
            // write the formatted JSON string to the tasks.json file
            Files.write(Paths.get(TASK_FILE_PATH), json.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Error writing to tasks file.");
        }
    }


}