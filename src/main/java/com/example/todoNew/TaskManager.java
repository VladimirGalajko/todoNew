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
        User newUser = new User(username, password);
        users.add(newUser);
        saveUsers();
        System.out.println("Пользователь успешно зарегистрирован!");
    }

    public boolean loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Пользователь: " + username);
                return true;
            }
        }

        System.out.println("Неверные учетные данные. Попробуйте снова");
        return false;
    }

    public void loadTasks(String username) throws FileNotFoundException {
        try {
            FileReader reader = new FileReader(username + "_" + FILE_PATH);
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            tasks.clear();
            for (Object obj : jsonArray) {
                if (obj instanceof org.json.simple.JSONObject) {
                    tasks.add(Task.fromJsonObject((org.json.simple.JSONObject) obj));
                }
            }
        } catch (IOException | ParseException e) {
            if (e instanceof FileNotFoundException) {
                throw (FileNotFoundException) e;
            }
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
            if (tasks.isEmpty()) {
                System.out.println("Задачи отсутствуют.");
            } else {
                for (Task task : tasks) {
                    System.out.println(task);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Задачи отсутствуют.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTask(String name, String description, String status, String username) {
        try {
            long id = System.currentTimeMillis();
            Task newTask = new Task(id, name, description, status, getCurrentDate(), "", username);
            tasks.add(newTask);
            saveTasks(username);
            System.out.println("Задача успешно создана!");
        } catch (Exception e) {
            e.printStackTrace();
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
                    System.out.println("Задача успешно удалена!");
                    return;
                }
            }

            System.out.println("Задача с указанным ID не найдена или не принадлежит текущему пользователю.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editTask(long idToEdit, String newName, String newDescription, String username) {
        try {
            for (Task task : tasks) {
                if (task.getId() == idToEdit) {
                    task.setName(newName);
                    task.setDescription(newDescription);
                    saveTasks(username);
                    System.out.println("Задача успешно отредактирована!");
                    return;
                }
            }

            System.out.println("Задача с указанным ID не найдена.");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    System.out.println("Статус задачи успешно изменен на 'Выполнено'!");
                    return;
                }
            }

            System.out.println("Задача с указанным ID не найдена или не принадлежит пользователю.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
