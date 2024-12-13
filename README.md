# Task Tracker CLI

A simple command-line application for managing tasks. You can add, update, delete, and mark tasks with various statuses. This project is designed to keep track of tasks, allowing users to easily manage their to-do list through the terminal.

PROJECT URL: https://roadmap.sh/projects/task-tracker
## Features

- **Add a task**: Add a new task with a description.
- **Update a task**: Modify the description of an existing task.
- **Delete a task**: Remove a task by its ID.
- **Mark a task as in-progress or done**: Track the progress of tasks.
- **List tasks**: View all tasks or filter them by their status (`todo`, `in-progress`, `done`).
- **Persistent data**: Tasks are saved in a JSON file and persist across sessions.

## Installation

1. Clone the repository to your local machine:
   ```bash
   git clone <repository-url>
   cd task-tracker-cli
   ```
2. Compile the Java files
   ```bash
   javac -d bin src src/tasktracker/*.java
   ```
3. Run the application
   ```bash
   java -cp bin tasktracker.TaskTracker <command> <arguments>
   ```

## Usage

1. Add a Task
   ```bash
    java -cp bin tasktracker.TaskTracker add "Task description"
   ```
2. Update a Task
   ```bash
   java -cp bin tasktracker.TaskTracker update <task-id> "New task description"
   ```
3. Delete a Task
   ```bash
   java -cp bin tasktracker.TaskTracker delete <task-id>
   ```
4. Mark Task as In-Progress
   ```bash
   java -cp bin tasktracker.TaskTracker mark-in-progress <task-id>
   ```
5. Mark Task as Done
   ```bash
   java -cp bin tasktracker.TaskTracker mark-done <task-id>
   ```
6. List Tasks
   - List all tasks
     ```bash
     java -cp bin tasktracker.TaskTracker list
     ```
   - List tasks by status
     ```bash
     java -cp bin tasktracker.TaskTracker list <status>
     ```
