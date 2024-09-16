package com.example.todoNew;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final User currentUser;
    @Getter
    private List<Task> tasks;
    private final String TASKS_FILE = "tasks.json";
    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy HH:mm:ss");
    String formattedDateTime = currentDateTime.format(formatter);

    public TaskManager(User currentUser) {
        this.currentUser = currentUser;
        this.tasks = loadTasksFromFile();
    }



    public void addTask(Task task) {
        tasks.add(task);
        saveTasksToFile();
    }

    private List<Task> loadTasksFromFile() {
        try {
            File file = new File(TASKS_FILE);
            ObjectMapper mapper = new ObjectMapper();
            if (file.exists()) {
                return mapper.readValue(file, new TypeReference<List<Task>>() {});
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            LogManager.getInstance().logError("Ошибка при чтении файла задач: ", e);
            return new ArrayList<>();
        }
    }

    public void saveTasksToFile() {
        try {
            File file = new File(TASKS_FILE);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(file, tasks);
            LogManager.getInstance().logInfo("Задачи успешно сохранены в файл " + TASKS_FILE);
        } catch (IOException e) {
            LogManager.getInstance().logError("Ошибка при сохранении файла задач: ", e);
        }
    }



    public static void UserRegistration(String username, String password,boolean testMode) {
        User testUser = new User(username, password);
        DataSource dataSource = DataSourceFactory.createDataSource(testMode); // true для H2, замените в зависимости от конфигурации
        dataSource.saveUser(testUser);
        LogManager.getInstance().logInfo("User registered: " + username);
    }

    public static void UserLogin(String username, String password,boolean testMode) {
        LogManager.getInstance().logInfo("Начало UserLogin");
        DataSource dataSource = DataSourceFactory.createDataSource(testMode); // true для H2, замените в зависимости от конфигурации
        User currentUser = dataSource.findUserByUsernameAndPassword(username, password);
        if (currentUser == null) {
            LogManager.getInstance().logInfo("Пользователь не найден или неверный пароль.");
        } else {
            LogManager.getInstance().logInfo("Вход выполнен для пользователя: " + currentUser.getUsername());
        }
        LogManager.getInstance().logInfo("Завершение UserLogin");
    }

    public static void TaskCreation(String username, String password, long id, String taskName, String description, String status,boolean testMode) {
        LogManager.getInstance().logInfo("Начало TaskCreation сохраним " + taskName);

        DataSource dataSource = DataSourceFactory.createDataSource(testMode);
        User currentUser = dataSource.findUserByUsernameAndPassword(username, password);
        if (currentUser == null) {
            LogManager.getInstance().logInfo("Пользователь не найден или неверный пароль.");
            return;
        }

        Task testTask = new Task(
                id, taskName, description, status,
                currentUser.getUsername(), currentUser.getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy HH:mm:ss")),
                ""
        );
        LogManager.getInstance().logInfo("Вызов saveTask ");
        dataSource.saveTask(testTask);

        boolean taskFound = false;
        for (Task task : dataSource.getTasksForUser(username)) {
            if (task.getName().equals(taskName) && task.getUsername().equals(currentUser.getUsername())) {
                taskFound = true;
                LogManager.getInstance().logInfo("Тестовая задача успешно добавлена: " + task);
                break;
            }
        }

        if (!taskFound) {
            LogManager.getInstance().logInfo("Не удалось добавить тестовую задачу.");
        }

        LogManager.getInstance().logInfo("Завершение TaskCreation");
    }

    public static void TaskViewing(String username, String password,boolean testMode) {
        LogManager.getInstance().logInfo("Начало TaskViewing");

        DataSource dataSource = DataSourceFactory.createDataSource(testMode);
        User currentUser = dataSource.findUserByUsernameAndPassword(username, password);
        if (currentUser == null) {
            LogManager.getInstance().logInfo("Пользователь не найден или неверный пароль.");
            return;
        }

        List<Task> tasks = dataSource.getTasksForUser(username);
        for (Task task : tasks) {
            LogManager.getInstance().logInfo("Задачa: " + task);
        }

        LogManager.getInstance().logInfo("Завершение TaskViewing");
    }

    public static void TaskDeletion(String username, String password, long taskId, boolean testMode) {
        LogManager.getInstance().logInfo("Начало теста удаления задач");

        DataSource dataSource = DataSourceFactory.createDataSource(testMode);
        User currentUser = dataSource.findUserByUsernameAndPassword(username, password);
        if (currentUser == null) {
            LogManager.getInstance().logInfo("Пользователь не найден или неверный пароль.");
            return;
        }

        dataSource.removeTask(taskId, currentUser);

        LogManager.getInstance().logInfo("Завершение теста удаления задач");
    }


    public void removeTask(long taskId,User currentUser) {
        boolean removed = tasks.removeIf(task -> task.getId() == taskId && task.getUsername().equals(currentUser.getUsername()));

        if (removed) {
            LogManager.getInstance().logInfo("Задача с ID " + taskId + " удалена.");
            saveTasksToFile();
        } else {
            LogManager.getInstance().logInfo("Задача с ID " + taskId + " не найдена или не принадлежит текущему пользователю.");
        }
    }

}
