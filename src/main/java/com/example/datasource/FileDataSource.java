package com.example.datasource;


import com.example.model.Task;
import com.example.model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component("fileDataSource")
@Profile("file")
public class FileDataSource implements DataSource {
    private static final Logger logger = LoggerFactory.getLogger(FileDataSource.class);
    private static final String USERS_FILE = "users.json";
    private static final String TASKS_FILE = "task.json";

    private final List<User> users = new ArrayList<>();
    private final List<Task> tasks = new ArrayList<>();


    public FileDataSource() {
        loadUsers();
        loadTasks();
    }

    @Override
    public void saveUser(User user) {
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                logger.info("Пользователь с именем '" + user.getUsername() + "' уже существует.");
                return;
            }
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
        logger.info("Задача сохранена: " + task.getName());
    }



    @Override
    public boolean removeTask(String taskId) {
        tasks.removeIf(task -> Objects.equals(task.getId(), taskId));
        saveTasksToFile();
        logger.info("Задача с ID {} удалена", taskId);
        return true;
    }

    @Override
    public List<Task> getTasksForUser(String username) {
        List<Task> userTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getUsername().equals(username)) {
                userTasks.add(task);
            }
        }
        return userTasks;
    }

    private void loadUsers() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(USERS_FILE)) {
            JSONArray userList = (JSONArray) jsonParser.parse(reader);
            for (Object userObj : userList) {
                JSONObject userJson = (JSONObject) userObj;
                users.add(User.fromJsonObject(userJson));
            }
            logger.info("Пользователи успешно загружены из файла.");
        } catch (IOException | ParseException e) {
            logger.info("Ошибка при загрузке пользователей: ", e);
        }
    }

    private void saveUsersToFile() {
        JSONArray userList = new JSONArray();
        for (User user : users) {
            userList.add(user.toJsonObject());
        }
        try (FileWriter file = new FileWriter(USERS_FILE)) {
            file.write(userList.toJSONString());
            file.flush();
            logger.info("Пользователи успешно сохранены в файл.");
        } catch (IOException e) {
            logger.info("Ошибка при сохранении пользователей: ", e);
        }
    }

    private void loadTasks() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(TASKS_FILE)) {
            JSONArray taskList = (JSONArray) jsonParser.parse(reader);
            for (Object taskObj : taskList) {
                JSONObject taskJson = (JSONObject) taskObj;
                tasks.add(Task.fromJsonObject(taskJson));
            }
            logger.info("Задачи успешно загружены из файла.");
        } catch (IOException | ParseException e) {
            logger.info("Ошибка при загрузке задач: ", e);
        }
    }

    private void saveTasksToFile() {
        JSONArray taskList = new JSONArray();
        for (Task task : tasks) {
            taskList.add(task.toJsonObject());
        }
        try (FileWriter file = new FileWriter(TASKS_FILE)) {
            file.write(taskList.toJSONString());
            file.flush();
            logger.info("Задачи успешно сохранены в файл.");
        } catch (IOException e) {
            logger.info("Ошибка при сохранении задач: ", e);
        }
    }
}




