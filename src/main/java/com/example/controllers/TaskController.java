package com.example.controllers;

import com.example.dto.TaskDTO;
import com.example.dto.UserDTO;
import com.example.model.Task;
import com.example.model.User;
import com.example.service.AuthService;
import com.example.service.TaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private AuthService authService;

    private final TaskManager taskManager;

    public TaskController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO.getUsername(), userDTO.getPassword());
        taskManager.addUser(user);
        return "User registered: " + userDTO.getUsername();
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserDTO userDTO) {
        User user = taskManager.findUserByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
        if (user == null) {
            return "Invalid username or password.";
        }
        return "User logged in: " + userDTO.getUsername();
    }

    @PostMapping("/createTask")
    public ResponseEntity<String> createTask(@RequestBody TaskDTO taskDTO) {
        if (!authService.isLoggedIn()) {
            return ResponseEntity.status(403).body("Please log in first");
        }
        ;
        Task newTask = Task.builder()
                .name(taskDTO.getName())
                .description(taskDTO.getDescription())
                .status(taskDTO.getStatus())
                .username(taskDTO.getUsername())
                .build();
        taskManager.addTask(newTask);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task created id:" + newTask.getId());

    }

    @PostMapping("/deleteTask")
    public ResponseEntity<String> deleteTask(@RequestBody TaskDTO taskDTO) {
        if (!authService.isLoggedIn()) {
            return ResponseEntity.status(403).body("Please log in first");
        }

        if (taskDTO.getId() == null || taskDTO.getId().isEmpty()) {
            return ResponseEntity.status(400).body("Task ID is required for deletion");
        }


        boolean isDeleted = taskManager.removeTask(taskDTO.getId());
        if (isDeleted) {
            return ResponseEntity.ok("Task deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Task not found");
        }
    }

    @PostMapping("/viewTasks")
    public ResponseEntity<?> viewTasks(@RequestBody UserDTO userDTO) {

        if (!authService.isLoggedIn()) {
            return ResponseEntity.status(403).body("Please log in first");
        }
        ;

        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            return ResponseEntity.status(400).body("Username is required to view tasks");
        }

        List<Task> tasks = taskManager.getTasksForUser(userDTO.getUsername());

        if (tasks == null || tasks.isEmpty()) {
            return ResponseEntity.status(404).body("No tasks found for user: " + userDTO.getUsername());
        }

        return ResponseEntity.ok(tasks);
    }

}
