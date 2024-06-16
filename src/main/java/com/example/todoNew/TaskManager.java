package com.example.todoNew;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManager {
    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);
    private Scanner scanner;
    private User currentUser;
    private List<Task> tasks;
    private final String TASKS_FILE = "tasks.json";
    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy HH:mm:ss");
    String formattedDateTime = currentDateTime.format(formatter);

    public TaskManager(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
        this.tasks = loadTasksFromFile();
    }

    public void displayTasks() {
        boolean tasksFound = false;
        System.out.println("Задачи пользователя " + currentUser.getUsername() + ":");
        logger.info("Задачи пользователя {}: {}", currentUser.getUsername(), tasks);
        for (Task task : tasks) {
            if (task.getUsername().equals(currentUser.getUsername())) {
                System.out.println(task);
                logger.info("Задачa:" + task);
                tasksFound = true;
            }
        }
        if (!tasksFound) {
            System.out.println("Задачи отсутствуют.");
            logger.info("Задачи отсутствуют.");
        }
    }

    public void addTask() {
        logger.info("Начало добавления новой задачи");

        System.out.println("Введите название задачи:");
        String name = scanner.nextLine().trim();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine().trim();
        System.out.println("Введите статус задачи:");
        String status = scanner.nextLine().trim();

        long id = tasks.size() + 1;


        Task newTask = new Task(id, name, description, status, currentUser.getUsername(), currentUser.getId(), formattedDateTime, "");
        tasks.add(newTask);
        System.out.println("Новая задача добавлена:");
        System.out.println(newTask);

        saveTasksToFile();

        logger.info("Завершение добавления новой задачи");
    }
    public void addTask(Task task) {
        tasks.add(task);
    }

    public void editTask() {
        logger.info("Начало редактирования новой задачи");

        System.out.println("Введите ID задачи для редактирования:");
        if (scanner.hasNextLong()) {
            long taskId = scanner.nextLong();
            scanner.nextLine();

          Task taskToEdit = null;
            for (Task task : tasks) {
                if (task.getId() == taskId && task.getUsername().equals(currentUser.getUsername())) {
                    taskToEdit = task;
                    break;
                }
            }

            if (taskToEdit != null) {
                System.out.println("Введите новое название задачи:");
                String name = scanner.nextLine().trim();
                System.out.println("Введите новое описание задачи:");
                String description = scanner.nextLine().trim();
                System.out.println("Введите новый статус задачи:");
                String status = scanner.nextLine().trim();

                // Обновляем данные задачи
                taskToEdit.setName(name);
                taskToEdit.setDescription(description);
                taskToEdit.setStatus(status);
                taskToEdit.setUpdatedAt(formattedDateTime);

                System.out.println("Задача успешно отредактирована:");
                System.out.println(taskToEdit);

                saveTasksToFile(); // Сохраняем обновленный список задач в файл
            } else {
                System.out.println("Задача с ID " + taskId + " не найдена или не принадлежит текущему пользователю.");
                logger.debug("Задача с ID " + taskId + " не найдена или не принадлежит текущему пользователю.");
            }
        } else {
            System.out.println("Некорректный формат ID задачи.");
            logger.error("Завершение редактирования  новой задачи");
            scanner.nextLine();
        }

        logger.info("Завершение редактирования  новой задачи");
    }

    public void removeTask() {
        System.out.println("Введите ID задачи для удаления:");
        if (scanner.hasNextLong()) {
            long taskId = scanner.nextLong();
            scanner.nextLine(); // Считываем символ новой строки после считывания числа

            boolean removed = tasks.removeIf(task -> task.getId() == taskId && task.getUsername().equals(currentUser.getUsername()));
            if (removed) {
                System.out.println("Задача с ID " + taskId + " удалена.");
                logger.debug("Задача с ID " + taskId + " удалена.");
                saveTasksToFile();
            } else {
                System.out.println("Задача с ID " + taskId + " не найдена или не принадлежит текущему пользователю.");
                logger.debug("Задача с ID " + taskId + " не найдена или не принадлежит текущему пользователю.");
            }
        } else {
            System.out.println("Некорректный формат ID задачи.");
            logger.debug("Некорректный формат ID задачи.");
            scanner.nextLine();
        }
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
            //System.out.println("Ошибка при чтении файла задач: " + e.getMessage());
            logger.error("Ошибка при чтении файла задач: " + e.getMessage());
            return new ArrayList<>();
        }
    }

        public void saveTasksToFile() {
        try {
            File file = new File(TASKS_FILE);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(file, tasks);
            //System.out.println("Задачи успешно сохранены в файл " + TASKS_FILE);
            logger.info("Задачи успешно сохранены в файл " + TASKS_FILE);
        } catch (IOException e) {
            //System.out.println("Ошибка при сохранении файла задач: " + e.getMessage());
            logger.error("Ошибка при сохранении файла задач: " + e.getMessage());
        }
    }
    public List<Task> getTasks() {
        return loadTasksFromFile();
    }
    public void removeTask(long taskId) {
        boolean removed = tasks.removeIf(task -> task.getId() == taskId && task.getUsername().equals(currentUser.getUsername()));
        if (removed) {
            //System.out.println("Задача с ID " + taskId + " удалена.");
            logger.debug("Задача с ID " + taskId + " удалена.");
            saveTasksToFile();
        } else {
            logger.debug("Задача с ID " + taskId + " не найдена или не принадлежит текущему пользователю.");
        }
    }

}