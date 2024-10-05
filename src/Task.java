// import for handling date and time
import java.time.LocalDateTime;
// import for formatting date and time
import java.time.format.DateTimeFormatter;


// represents a task
public class Task {
    private int id;
    private String description;
    private String status; // "to-do", "in-progress", "done"
    private String createdAt;
    private String updatedAt;

    // constructor
    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.status = "to-do";
        this.createdAt = getCurrentTime();
        this.updatedAt = getCurrentTime();
    }

     // retrieves the current date and time formatted as anISO 8601 string
    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // define the date/time format
        return LocalDateTime.now().format(formatter); // get the current time and format it
    }

    // returns the ID of the task
    public int getId() {
        return id;
    }

    // returns the description of the task
    public String getDescription() {
        return description;
    }

    // returns the current status of the task
    public String getStatus() {
        return status;
    }

    // returns the creation timestamp of the task
    public String getCreatedAt() {
        return createdAt;
    }

    // returns the last updated timestamp of the task
    public String getUpdatedAt() {
        return updatedAt;
    }

    // updates the task's status and updates the timestamp
    public void setStatus(String status) {
        this.status = status; 
        this.updatedAt = getCurrentTime();
    }

    // updates the task's description and updates the timestamp
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = getCurrentTime();

    }

    // convert Task to a JSON-like string
    public String toJson() {
        return "{"
                + "\"id\":" + id + ","
                + "\"description\":\"" + description + "\","
                + "\"status\":\"" + status + "\","
                + "\"createdAt\":\"" + createdAt + "\","
                + "\"updatedAt\":\"" + updatedAt + "\""
                + "}";
    }

    public static Task fromJson(String json) {
        // remove curly braces and split the string by commas
        String[] parts = json.replace("{", "").replace("}", "").split(",");

        // variables to hold extracted values
        int id = 0;
        String description = "";
        String status = "";
        String createdAt = "";
        String updatedAt = "";

        // iterate over the parts to assign values
        for (String part : parts) {
            String[] keyValue = part.split(":");
            String key = keyValue[0].trim().replace("\"", "");
            String value = keyValue[1].trim().replace("\"", "");

            // assign values to appropriate variables based on the key
            switch (key) {
                case "id":
                    id = Integer.parseInt(value);
                    break;
                case "description":
                    description = value;
                    break;
                case "status":
                    status = value;
                    break;
                case "createdAt":
                    createdAt = value;
                    break;
                case "updatedAt":
                    updatedAt = value;
                    break;
            }
        }

        // Create a new Task object using the extracted values
        Task task = new Task(id, description);
        task.status = status;
        task.createdAt = createdAt;
        task.updatedAt = updatedAt;
        return task;
    }
}
