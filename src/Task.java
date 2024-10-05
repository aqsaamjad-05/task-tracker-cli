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

}
