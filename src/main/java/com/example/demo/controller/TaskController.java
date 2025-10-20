package com.example.demo.controller; // Make sure this package name is correct

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Task;
import com.example.demo.model.TaskExecution;
import com.example.demo.repository.TaskRepository;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    
    @GetMapping
    public ResponseEntity<?> getTasks(@RequestParam(required = false) String id) {
        if (id != null) {
            Optional<Task> task = taskRepository.findById(id);
            return task.map(ResponseEntity::ok)
                       .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return ResponseEntity.ok(taskRepository.findAll());
    }

   
    @PutMapping
    public ResponseEntity<String> createTask(@RequestBody Task task) {
        if (!isCommandValid(task.getCommand())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or potentially malicious command.");
        }
        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task created with ID: " + task.getId());
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        if (!taskRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        taskRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
    @GetMapping("/findByName")
    public ResponseEntity<List<Task>> findTasksByName(@RequestParam String name) {
        List<Task> tasks = taskRepository.findByNameContaining(name);
        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tasks);
    }

    
    @PutMapping("/{id}/executions")
    public ResponseEntity<Task> executeTask(@PathVariable String id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Task task = taskOptional.get();
        Date startTime = new Date();
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
          
            processBuilder.command("cmd.exe", "/c", task.getCommand());

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                 while ((line = errorReader.readLine()) != null) {
                    output.append("ERROR: ").append(line).append("\n");
                }
            }

        } catch (Exception e) {
            output.append("Error executing command: ").append(e.getMessage());
        }

        Date endTime = new Date();
        TaskExecution execution = new TaskExecution(startTime, endTime, output.toString());
        task.getTaskExecutions().add(execution);
        taskRepository.save(task);

        return ResponseEntity.ok(task);
    }
    
     
    private boolean isCommandValid(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }
       
        String[] blocked = {"rm", git"shutdown", "reboot", ";", "&&", "|", "`", "$("};
        String lowerCaseCommand = command.toLowerCase();
        for (String block : blocked) {
            if (lowerCaseCommand.contains(block)) {
                return false;
            }
        }
        return true;
    }
}
