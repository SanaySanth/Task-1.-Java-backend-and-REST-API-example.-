https://github.com/SanaySanth/Task-1.-Java-backend-and-REST-API-example.-/blob/main/01.png

# Kaiburr Assessment - Task 1: Java REST API

## Project Description

This is a Java-based REST API application built with **Spring Boot** that manages "task" objects. Each task represents a shell command that can be executed, with all data stored in a **MongoDB** database.

## Author
**Name:** SANAY_SANTH  
**Date:** October 19, 2025

## Technology Stack

- **Java:** 17
- **Framework:** Spring Boot 3.4.10
- **Database:** MongoDB
- **Build Tool:** Maven
- **Dependencies:**
  - Spring Web (REST API)
  - Spring Data MongoDB (Database connectivity)
  - Lombok (Code generation)

## Data Model

### Task Object
- `id` (String) - Unique task identifier
- `name` (String) - Task name
- `owner` (String) - Task owner
- `command` (String) - Shell command to execute
- `taskExecutions` (List) - List of execution records

### TaskExecution Object
- `startTime` (Date) - Execution start time
- `endTime` (Date) - Execution end time
- `output` (String) - Command output

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/tasks` | Get all tasks |
| GET | `/tasks?id={id}` | Get a specific task by ID |
| PUT | `/tasks` | Create a new task |
| DELETE | `/tasks/{id}` | Delete a task by ID |
| GET | `/tasks/findByName?name={searchString}` | Find tasks by name (partial match) |
| PUT | `/tasks/{id}/executions` | Execute a task's command |

## Prerequisites

Before running this application, ensure you have the following installed:

1. **Java Development Kit (JDK) 17 or higher**
   - Verify installation: `java -version`

2. **MongoDB Community Server**
   - MongoDB should be running on `localhost:27017`
   - Verify installation: Open MongoDB Shell or Compass

3. **Maven** (usually included with IDE)
   - Verify installation: `mvn -version`

4. **IDE** (Recommended: VS Code, IntelliJ IDEA, or Eclipse)

## Installation and Setup

### Step 1: Clone the Repository
```bash
git clone <your-repository-url>
cd demo
```

### Step 2: Configure MongoDB
The application is configured to connect to MongoDB at:
```
mongodb://localhost:27017/kaiburr_db
```

If your MongoDB is running on a different host or port, update the connection string in:
```
src/main/resources/application.properties
```

### Step 3: Build the Project
```bash
mvn clean install
```

### Step 4: Run the Application
```bash
mvn spring-boot:run
```

Or run the main class directly from your IDE:
```
src/main/java/com/example/demo/DemoApplication.java
```

The application will start on **http://localhost:8080**

## Security Features

The application includes basic command validation to prevent malicious code execution. The following patterns are blocked:
- `rm` (remove files)
- `shutdown` (system shutdown)
- `reboot` (system reboot)
- `;` (command chaining)
- `&&` (command chaining)
- `|` (piping)
- Backticks and command substitution

## API Testing

Below are the screenshots demonstrating the API functionality using PowerShell's `Invoke-WebRequest` command.

### Test 1: Create a Task (PUT /tasks)
This test creates a new task with the command "echo Hello from my API".

![Create Task]([screenshots/test1_create_task.png](https://github.com/SanaySanth/Task-1.-Java-backend-and-REST-API-example.-/blob/main/01.png
))

**Command:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/tasks" -Method PUT -Headers @{"Content-Type"="application/json"} -Body '{"name": "My First Task", "owner": "SANAY_SANTH", "command": "echo Hello from my API"}'
```

**Expected Result:** Status Code 201 (Created), with the new task ID returned.

---

### Test 2: Get All Tasks (GET /tasks)
This test retrieves all tasks stored in the database.

![Get All Tasks](screenshots/test2_get_all_tasks.png)

**Command:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/tasks" -Method GET
```

**Expected Result:** Status Code 200 (OK), returns a JSON array of all tasks.

---

### Test 3: Execute a Task (PUT /tasks/{id}/executions)
This test executes the shell command associated with a task and stores the execution results.

![Execute Task](screenshots/test3_execute_task.png)

**Command:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/tasks/68f49f3df9e5e861a04918e4/executions" -Method PUT
```

**Expected Result:** Status Code 200 (OK), returns the task with populated `taskExecutions` list containing start time, end time, and output.

---

### Test 4: Get Specific Task (GET /tasks?id={id})
This test retrieves a specific task by ID to verify the execution details.

![Get Specific Task](screenshots/test4_get_specific_task.png)

**Command:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/tasks?id=68f49f3df9e5e861a04918e4" -Method GET
```

**Expected Result:** Status Code 200 (OK), returns the task object with execution history.

---

### Test 5: Find Tasks by Name (GET /tasks/findByName?name={searchString})
This test searches for tasks whose name contains the specified string.

![Find by Name](screenshots/test5_find_by_name.png)

**Command:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/tasks/findByName?name=First" -Method GET
```

**Expected Result:** Status Code 200 (OK), returns all tasks with "First" in the name. Returns 404 if no matches found.

---

### Test 6: Delete a Task (DELETE /tasks/{id})
This test deletes a task from the database.

![Delete Task](screenshots/test6_delete_task.png)

**Command:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/tasks/68f49f3df9e5e861a04918e4" -Method DELETE
```

**Expected Result:** Status Code 204 (No Content), task successfully deleted.

---

## Project Structure

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/
│   │   │   │   └── TaskController.java
│   │   │   ├── model/
│   │   │   │   ├── Task.java
│   │   │   │   └── TaskExecution.java
│   │   │   ├── repository/
│   │   │   │   └── TaskRepository.java
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── screenshots/
│   ├── test1_create_task.png
│   ├── test2_get_all_tasks.png
│   ├── test3_execute_task.png
│   ├── test4_get_specific_task.png
│   ├── test5_find_by_name.png
│   └── test6_delete_task.png
├── pom.xml
└── Readme.md
```

## Code Highlights

### TaskController.java
- Implements all required REST endpoints
- Includes command validation for security
- Uses `ProcessBuilder` to execute shell commands safely

### Task.java
- MongoDB document model with `@Document` annotation
- Contains all required fields as per specifications
- Uses Lombok `@Data` annotation for boilerplate reduction

### TaskRepository.java
- Extends `MongoRepository` for database operations
- Custom query method: `findByNameContaining()` for name search

## Testing with Other Tools

### Using cURL (if installed)
```bash
# Create a task
curl -X PUT http://localhost:8080/tasks -H "Content-Type: application/json" -d '{"name": "Test Task", "owner": "John", "command": "echo Test"}'

# Get all tasks
curl http://localhost:8080/tasks

# Execute a task
curl -X PUT http://localhost:8080/tasks/{id}/executions

# Delete a task
curl -X DELETE http://localhost:8080/tasks/{id}
```

### Using Postman
1. Import the endpoints into Postman
2. Set the appropriate HTTP methods
3. For PUT requests, set Content-Type header to `application/json`
4. Add request body in JSON format

## Troubleshooting

### MongoDB Connection Issues
- Ensure MongoDB service is running: Check services or run `mongosh` to verify
- Verify connection string in `application.properties`
- Check firewall settings for port 27017

### Application Won't Start
- Verify Java version: `java -version` (should be 17+)
- Check if port 8080 is available
- Review console logs for specific error messages

### Command Execution Errors
- Ensure the command is valid for your operating system
- Windows uses `cmd.exe /c`, Linux/Mac uses `bash -c`
- Check command validation logic for blocked patterns

## Future Enhancements

- Add authentication and authorization
- Implement pagination for task listing
- Add more sophisticated command validation
- Implement async task execution
- Add task scheduling functionality
- Create a web UI frontend

## License

This project is developed as part of the Kaiburr Assessment 2025.

## Contact

For questions or issues, please contact: SANAY_SANTH

---

**Note:** This README contains all required information as per Kaiburr Assessment guidelines. All screenshots include timestamp and author identification as required.
