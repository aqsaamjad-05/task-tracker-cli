package tasktracker;

public class TaskTracker {
    public class TaskTrackerCLI {

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
    
        // placeholder methods for future implementation
        private static void addTask(String description) {
            System.out.println("Adding task: " + description);
            // will be implemented in future steps
        }
    
        private static void listTasks() {
            System.out.println("Listing tasks");
            // will be implemented in future steps
        }
    }
}
