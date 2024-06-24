package com.example.todoNew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class TodoNewApplication {
    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);


    public static void main(String[] args) {
        boolean testMode = false;

        if (testMode) {
            TestManager.testUserRegistration("testuser", "testpassword");
            TestManager.testUserLogin("testuser", "testpassword");
            TestManager.testTaskCreation("testuser", "testpassword", 1, "Test Task", "This is a test task.", "pending");
            TestManager.testTaskViewing("testuser", "testpassword");
            TestManager.testTaskDeletion("testuser", "testpassword", 1);
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите действие:");
        System.out.println("1. Вход");
        System.out.println("2. Регистрация");


        int action = Integer.parseInt(scanner.nextLine().trim());

        User currentUser = null;
        if (action == 1) {
            System.out.println("Введите имя пользователя:");
            String username = scanner.nextLine().trim();

            System.out.println("Введите пароль:");
            String password = scanner.nextLine().trim();

            currentUser = JsonFileManager.findUserByUsernameAndPassword(username, password);
            if (currentUser == null) {
                System.out.println("Пользователь не найден или неверный пароль.");
                return;
            } else {
                System.out.println("Вход выполнен для пользователя: " + currentUser.getUsername());
            }
        } else if (action == 2) {
            System.out.println("Введите имя пользователя для регистрации:");
            String username = scanner.nextLine().trim();

            System.out.println("Введите пароль для нового пользователя:");
            String password = scanner.nextLine().trim();

            currentUser = new User(username, password);
            JsonFileManager.saveUser(currentUser,testMode);
            System.out.println("Пользователь успешно зарегистрирован: " + username);
        }  else {
            System.out.println("Неверный выбор действия.");
            return;
        }

        TaskManager taskManager = new TaskManager(scanner, currentUser);
        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Просмотр задач");
            System.out.println("2. Добавление задачи");
            System.out.println("3. Редактирование задачи");
            System.out.println("4. Удаление задачи");
            System.out.println("5. Выход");
            int choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice) {
                case 1:
                    taskManager.displayTasks(testMode);
                    break;
                case 2:
                    taskManager.addTask();
                    break;
                case 3:
                    taskManager.editTask();
                    break;
                case 4:
                    taskManager.removeTask();
                    break;
                case 5:
                    System.out.println("Выход из приложения.");
                    return;
                default:
                    System.out.println("Неверный выбор действия.");
            }
        }
    }

}