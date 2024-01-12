package com.example.todoNew;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


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
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public void loadTasks() {
        try {
            FileReader reader = new FileReader(FILE_PATH);
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            tasks.clear();
            for (Object obj : jsonArray) {
                if (obj instanceof org.json.simple.JSONObject) {
                    tasks.add(Task.fromJsonObject((org.json.simple.JSONObject) obj));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void saveTasks() {
        try (FileWriter writer = new FileWriter(FILE_PATH);){
            JSONArray jsonArray = new JSONArray();
            for (Task task : tasks) {
                jsonArray.add(task.toJsonObject());
            }
            writer.write(jsonArray.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewTasks() {
        try {
            loadTasks();
            System.out.println("Список задач:");
            if (tasks.isEmpty()) {
                System.out.println("Задачи отсутствуют.");
            } else {
                for (Task task : tasks) {
                    System.out.println(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTask() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Введите имя задачи: ");
            String name = scanner.nextLine();

            System.out.print("Введите описание задачи: ");
            String description = scanner.nextLine();

            System.out.print("Введите статус задачи: ");
            String status = scanner.nextLine();

            long id = System.currentTimeMillis();
            Task newTask = new Task(id, name, description, status, getCurrentDate(), "");

            tasks.add(newTask);
            saveTasks();

            System.out.println("Задача успешно создана!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTask() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Введите ID задачи для удаления: ");
            long idToDelete = scanner.nextLong();

            Iterator<Task> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (task.getId() == idToDelete) {
                    iterator.remove();
                    saveTasks();
                    System.out.println("Задача успешно удалена!");
                    return;
                }
            }

            System.out.println("Задача с указанным ID не найдена.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editTask() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Введите ID задачи для редактирования: ");
            long idToEdit = scanner.nextLong();
            scanner.nextLine();
            for (Task task : tasks) {
                if (task.getId() == idToEdit) {
                    System.out.print("Введите новое имя задачи: ");
                    String newName = scanner.nextLine();
                    task.setName(newName);

                    System.out.print("Введите новое описание задачи: ");
                    String newDescription = scanner.nextLine();
                    task.setDescription(newDescription);

                    saveTasks();
                    System.out.println("Задача успешно отредактирована!");
                    return;
                }
            }

            System.out.println("Задача с указанным ID не найдена.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeTaskStatus() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Введите ID задачи для изменения статуса: ");
            long idToChangeStatus = scanner.nextLong();

            for (Task task : tasks) {
                if (task.getId() == idToChangeStatus) {
                    System.out.print("Введите новый статус задачи: ");
                    String newStatus = scanner.next();
                    task.setStatus(newStatus);

                    if (newStatus.equalsIgnoreCase("Done")) {
                        task.setDateEnd(getCurrentDate());
                    }
                    saveTasks();
                    System.out.println("Статус задачи успешно изменен!");
                    return;
                }
            }

            System.out.println("Задача с указанным ID не найдена.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
