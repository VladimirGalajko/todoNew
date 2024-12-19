package com.example.controllers;

import com.example.dto.TaskDTO;
import com.example.dto.UserDTO;
import com.example.model.Task;
import com.example.model.User;
import com.example.service.AuthService;
import com.example.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private AuthService authService;

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO) {
        logger.info("Регистрация пользователя: {}", userDTO);
        User user = new User(userDTO.getUsername(), userDTO.getPassword());
        taskService.addUser(user);
        logger.info("Пользователь зарегистрирован: {}", user);
        return "User registered: " + userDTO.getUsername();
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserDTO userDTO) {
        User user = taskService.findUserByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
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
            logger.info("Получен запрос на создание задачи: {}", taskDTO);

            // Преобразуем TaskDTO в Task
            Task task = new Task();
            task.setId(UUID.randomUUID().toString()); // Генерируем уникальный ID
            task.setName(taskDTO.getName());
            task.setDescription(taskDTO.getDescription());
            task.setStatus(taskDTO.getStatus());
            task.setUsername(taskDTO.getUsername());
            task.setCreatedAt(LocalDateTime.now().toString());
            task.setUpdatedAt(LocalDateTime.now().toString());

            // Сохраняем задачу через сервис
            taskService.createTask(task);

            return ResponseEntity.ok("Задача успешно создана. id: " + task.getId());
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Внутренняя ошибка сервера: {}", e.getMessage(), e);
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
