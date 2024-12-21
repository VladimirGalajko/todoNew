package com.example.controllers;

import com.example.dto.TaskDTO;
import com.example.dto.UserDTO;
import com.example.model.Task;
import com.example.model.User;
import com.example.service.AuthService;
import com.example.service.TaskService;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private AuthService authService;
    private final UserService userService;

    private final TaskService taskService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO) {
        log.info("Регистрация пользователя: {}", userDTO);
        User user = new User(userDTO.getUsername(), userDTO.getPassword());
        userService.addUser(user);
        log.info("Пользователь зарегистрирован: {}", user);
        return "User registered: " + userDTO.getUsername();
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserDTO userDTO) {
        User user = userService.findUserByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
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
        if (taskDTO.getUsername() == null || taskDTO.getUsername().isEmpty()) {
            return ResponseEntity.status(400).body("Task username is required");
        }

        try {
            log.info("Получен запрос на создание задачи: {}", taskDTO);
            Task task = new Task();
            task.setId(UUID.randomUUID().toString());
            task.setName(taskDTO.getName());
            task.setDescription(taskDTO.getDescription());
            task.setStatus(taskDTO.getStatus());
            task.setUsername(taskDTO.getUsername());
            task.setCreatedAt(LocalDateTime.now().toString());
            task.setUpdatedAt(LocalDateTime.now().toString());

            taskService.createTask(task);

            return ResponseEntity.ok("Задача успешно создана. id: " + task.getId());
        } catch (IllegalArgumentException e) {
            log.error("Ошибка: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Внутренняя ошибка сервера: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Внутренняя ошибка сервера.");
        }
    }
    @PostMapping("/deleteTask")
    public ResponseEntity<String> deleteTask(@RequestBody TaskDTO taskDTO) {
        if (!authService.isLoggedIn()) {
            return ResponseEntity.status(403).body("Please log in first");
        }

        if (taskDTO.getId() == null || taskDTO.getId().isEmpty()) {
            return ResponseEntity.status(400).body("Task ID is required for deletion");
        }


        boolean isDeleted = taskService.removeTask(taskDTO.getId());
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

        List<Task> tasks = taskService.getTasksForUser(userDTO.getUsername());

        if (tasks == null || tasks.isEmpty()) {
            return ResponseEntity.status(404).body("No tasks found for user: " + userDTO.getUsername());
        }

        return ResponseEntity.ok(tasks);
    }

}
