package com.example.todoNew;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TestManager {
    private static final Logger logger = LoggerFactory.getLogger(TestManager.class);

    public static void testUserRegistration(String username, String password) {
        User testUser = new User(username, password);
        JsonFileManager.saveUser(testUser,true);
        logger.info("User registered: " + username);
    }

    public static void testUserLogin(String username, String password) {
        logger.info("Начало testUserLogin");
        User currentUser = JsonFileManager.findUserByUsernameAndPassword(username, password);
        if (currentUser == null) {
            logger.debug("Пользователь не найден или неверный пароль.");
        } else {
            logger.debug("Вход выполнен для пользователя: " + currentUser.getUsername());
        }
        logger.info("Завершение testUserLogin");
    }

    public static void testTaskCreation(String username, String password, long id, String taskName, String description, String status) {
        logger.info("Начало testTaskCreation");

        User currentUser = JsonFileManager.findUserByUsernameAndPassword(username, password);
        if (currentUser == null) {
            logger.debug("Пользователь не найден или неверный пароль.");
            return;
        }

        Task testTask = new Task(
                id, taskName, description, status,
                currentUser.getUsername(), currentUser.getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy HH:mm:ss")),
                ""
        );

        TaskManager taskManager = new TaskManager(new Scanner(System.in), currentUser);
        taskManager.addTask(testTask);
        taskManager.saveTasksToFile();

        boolean taskFound = false;
        for (Task task : taskManager.getTasks()) {
            if (task.getName().equals(taskName) && task.getUsername().equals(currentUser.getUsername())) {
                taskFound = true;
                logger.debug("Тестовая задача успешно добавлена: " + task);
                break;
            }
        }

        if (!taskFound) {
            logger.error("Не удалось добавить тестовую задачу.");
        }

        logger.info("Завершение testTaskCreation");
    }

    public static void testTaskViewing(String username, String password) {
        logger.info("Начало testTaskViewing");

        User currentUser = JsonFileManager.findUserByUsernameAndPassword(username, password);
        if (currentUser == null) {
            logger.debug("Пользователь не найден или неверный пароль.");
            return;
        }

        TaskManager taskManager = new TaskManager(new Scanner(System.in), currentUser);
        taskManager.displayTasks(true);

        logger.info("Завершение testTaskViewing");
    }

    public static void testTaskDeletion(String username, String password, long taskId) {
        logger.info("Начало теста удаления задач");

        User currentUser = JsonFileManager.findUserByUsernameAndPassword(username, password);
        if (currentUser == null) {
            logger.debug("Пользователь не найден или неверный пароль.");
            return;
        }

        TaskManager taskManager = new TaskManager(new Scanner(System.in), currentUser);
        taskManager.removeTask(taskId);

        logger.info("Завершение теста удаления задач");
    }
}
