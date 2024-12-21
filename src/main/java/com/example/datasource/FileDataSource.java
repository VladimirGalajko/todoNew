package com.example.datasource;


import com.example.model.Task;
import com.example.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;

@Slf4j
@Component("fileDataSource")
@Profile("file")
public class FileDataSource implements DataSource {

    private static final String USERS_FILE = "users.json";
    private static final String TASKS_FILE = "tasks.json";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<User> users = new ArrayList<>();
    private final List<Task> tasks = new ArrayList<>();

    public FileDataSource() {
        loadUsers();
        loadTasks();
    }

    @Override
    public void saveUser(User user) {
        if (users.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
            log.info("Пользователь с именем '{}' уже существует.", user.getUsername());
            return;
        }
        users.add(user);
        saveUsersToFile();
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveTask(Task task) {
        tasks.add(task);
        saveTasksToFile();
        log.info("Задача '{}' сохранена.", task.getName());
    }

    @Override
    public boolean removeTask(String taskId) {
        boolean removed = tasks.removeIf(task -> Objects.equals(task.getId(), taskId));
        if (removed) {
            saveTasksToFile();
            log.info("Задача с ID '{}' удалена.", taskId);
        }
        return removed;
    }

    @Override
    public List<Task> getTasksForUser(String username) {
        return tasks.stream()
                .filter(task -> task.getUsername().equals(username))
                .toList();
    }

    private void loadUsers() {
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                users.addAll(objectMapper.readValue(file, new TypeReference<List<User>>() {}));
                log.info("Пользователи успешно загружены из файла.");
            }
        } catch (IOException e) {
            log.warn("Ошибка при загрузке пользователей: {}", e.getMessage());
        }
    }

    private void saveUsersToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(USERS_FILE), users);
            log.info("Пользователи успешно сохранены в файл.");
        } catch (IOException e) {
            log.error("Ошибка при сохранении пользователей: {}", e.getMessage());
        }
    }

    private void loadTasks() {
        try {
            File file = new File(TASKS_FILE);
            if (file.exists()) {
                tasks.addAll(objectMapper.readValue(file, new TypeReference<List<Task>>() {}));
                log.info("Задачи успешно загружены из файла.");
            }
        } catch (IOException e) {
            log.warn("Ошибка при загрузке задач: {}", e.getMessage());
        }
    }

    private void saveTasksToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(TASKS_FILE), tasks);
            log.info("Задачи успешно сохранены в файл.");
        } catch (IOException e) {
            log.error("Ошибка при сохранении задач: {}", e.getMessage());
        }
    }
}
