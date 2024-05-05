package com.example.todoNew;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import static org.apache.tomcat.util.http.FastHttpDateFormat.getCurrentDate;


public class TaskManager {
    private static final String FILE_PATH = "todo.json";
    private static final String USER_FILE_PATH = "users.json";
    private List<Task> tasks;

    private List<User> users;
    private Scanner scanner;

    public TaskManager(Scanner scanner) {
        this.tasks = new ArrayList<>();
        this.users = new ArrayList<>();
        this.scanner = scanner;
        loadUsers();
    }
    Logger logger = Logger.getInstance();

    private void loadUsers() {
        try (FileReader reader = new FileReader(USER_FILE_PATH)) {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            users.clear();
            for (Object obj : jsonArray) {
                if (obj instanceof org.json.simple.JSONObject) {
                    users.add(User.fromJsonObject((org.json.simple.JSONObject) obj));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (FileWriter writer = new FileWriter(USER_FILE_PATH)) {
            JSONArray jsonArray = new JSONArray();
            for (User user : users) {
                jsonArray.add(user.toJsonObject());
            }
            writer.write(jsonArray.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                logger.log("Пользователь с таким именем уже существует.");
                System.out.println("Пользователь с таким именем уже существует.");
                return;
            }
        }
        User newUser = new User(username, password);
        users.add(newUser);
        saveUsers();
        logger.log("Пользователь успешно зарегистрирован!");
        System.out.println("Пользователь успешно зарегистрирован!");
    }

public String loginUser(String username, String password) {
    for (User user : users) {
        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
            logger.log("Пользователь: " + username + " успешно авторизирован");
            System.out.println("Пользователь: " + username + " успешно авторизирован");
            return username; // Возвращаем имя пользователя при успешной авторизации
        }
    }
    logger.log("Ошибка авторизации для пользователя: " + username);
    System.out.println("Ошибка авторизации для пользователя: " + username);
    return null; // Возвращаем null при неудачной авторизации
}

    public void loadTasks(String username) throws FileNotFoundException {

        try {

            FileReader reader = new FileReader(username + "_" + FILE_PATH);
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            tasks.clear();
            for (Object obj : jsonArray) {
                if (obj instanceof org.json.simple.JSONObject) {
                    Task task = Task.fromJsonObject((org.json.simple.JSONObject) obj);
                    tasks.add(task);
                }

            }
        } catch (IOException | ParseException e) {
            if (e instanceof FileNotFoundException) {
                logger.log("Ошибка загрузки задач: файл не найден для пользователя: " + username);
                throw (FileNotFoundException) e;
            }
            logger.log("Ошибка загрузки задач для пользователя: " + username + ". Подробности: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveTasks(String username) {
        try (FileWriter writer = new FileWriter(username + "_" + FILE_PATH)) {
            JSONArray jsonArray = new JSONArray();
            for (Task task : tasks) {

                task.setUsername(username);
                jsonArray.add(task.toJsonObject());
            }
            writer.write(jsonArray.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewTasks(String username) {
        try {
            loadTasks(username);
            System.out.println("Список задач:");
            logger.log("Список задач:");
            if (tasks.isEmpty()) {
                System.out.println("Задачи отсутствуют.");
                logger.log("Задачи отсутствуют.");
            } else {
                for (Task task : tasks) {
                    System.out.println(task);
                    logger.log("Задачи :" + task.toString());
                }
            }
        } catch (FileNotFoundException e) {
            logger.log("Задачи отсутствуют.");
            System.out.println("Задачи отсутствуют.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long createTask(String name, String description, String status, String username) {
        try {
            long id = System.currentTimeMillis();
            Task newTask = new Task(id, name, description, status, getCurrentDate(), "", username);
            tasks.add(newTask);
            saveTasks(username);
            logger.log("Задача успешно создана!");
            System.out.println("Задача успешно создана!");
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void deleteTask(long idToDelete, String username) {
        try {
            Iterator<Task> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (task.getId() == idToDelete && task.getUsername().equals(username)) {
                    iterator.remove();
                    saveTasks(username);
                    logger.log("Задача успешно удалена!");
                    System.out.println("Задача успешно удалена!");
                    return;
                }
            }
            logger.log("Задача с указанным ID не найдена или не принадлежит текущему пользователю.");
            System.out.println("Задача с указанным ID не найдена или не принадлежит текущему пользователю.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editTask(long taskIdToEdit, String newName, String newDescription, String newStatus, String username) {
        for (Task task : tasks) {
            if (task.getId() == taskIdToEdit && task.getUsername().equals(username)) {
                task.setName(newName);
                task.setDescription(newDescription);
                task.setStatus(newStatus);
                saveTasks(username);
                logger.log("Задача успешно отредактирована!");
                System.out.println("Задача успешно отредактирована!");
                return;
            }
        }
        logger.log("Задача с указанным ID не найдена или не принадлежит пользователю.");
        System.out.println("Задача с указанным ID не найдена или не принадлежит пользователю.");
    }

    public Task getTaskById(long taskId, String username) {
        for (Task task : tasks) {
            if (task.getId() == taskId && task.getUsername().equals(username)) {
                return task;
            }
        }
        return null;
    }

    public void changeTaskStatus(long idToChangeStatus, String username) {
        try {
            for (Task task : tasks) {
                if (task.getId() == idToChangeStatus && task.getUsername().equals(username)) {

                    task.setStatus("Выполнено");

                    task.setDateEnd(getCurrentDate());

                    saveTasks(username);
                    logger.log("Статус задачи успешно изменен на 'Выполнено'!");
                    System.out.println("Статус задачи успешно изменен на 'Выполнено'!");
                    return;
                }
            }
            logger.log("Задача с указанным ID не найдена или не принадлежит пользователю.");
            System.out.println("Задача с указанным ID не найдена или не принадлежит пользователю.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
