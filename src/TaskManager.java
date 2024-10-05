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

    // saves tasks to the JSON file
    private void saveTasks() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++) {
            json.append(tasks.get(i).toJson());
            if (i < tasks.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        try {
            Files.write(Paths.get(TASK_FILE_PATH), json.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Error writing to tasks file.");
        }
    }
}