package com.example.todoNew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class TodoNewApplication {
    private static String loggedInUser;
    private static boolean isTesting = false;

    public static void main(String[] args) {
        SpringApplication.run(TodoNewApplication.class, args);

        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager(scanner);

        while (true) {


            System.out.println("Выберите действие:");
            System.out.println("1. Авторизация пользователя");
            System.out.println("2. Регистрация пользователя");
            System.out.println("3. Тестирование действий");
            System.out.println("0. Выход");

            int loginOrRegister = scanner.nextInt();
            scanner.nextLine();

            switch (loginOrRegister) {
                case 1:
                    loggedInUser = loginUser(taskManager, scanner);
                    break;
                case 2:
                    registerUser(taskManager, scanner);
                    break;
                case 3:
                    performTesting(taskManager, scanner);
                    break;
                case 0:
                    if (loggedInUser != null) {
                        taskManager.saveTasks(loggedInUser);
                    }
                    System.exit(0);
                    break;
                default:
                    System.out.println();
                    continue;
            }

            performActions(scanner, taskManager, loggedInUser);
        }
    }

    private static String loginUser(TaskManager taskManager, Scanner scanner) {
        String user = null;
        do {
            System.out.print("Введите имя пользователя(loginUser): ");
            String username = scanner.nextLine();
            System.out.print("Введите пароль: ");
            String password = scanner.nextLine();

            if (taskManager.loginUser(username, password) != null) {
                System.out.println("Авторизация успешна!");
                user = username;
                break;
            } else {
                System.out.println();
            }
        } while (user == null);

        return user;
    }

    private static void registerUser(TaskManager taskManager, Scanner scanner) {
        System.out.print("Для регистрации введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        taskManager.registerUser(username, password);
    }

    private static void performActions(Scanner scanner, TaskManager taskManager, String loggedInUser) {
        while (true) {
            if (loggedInUser == null && !isTesting) {
                System.out.println("Требуется авторизация.");
                loggedInUser = loginUser(taskManager, scanner);

                if (loggedInUser != null) {
                    viewTasks(taskManager, loggedInUser);
                }
            } else {
                System.out.println("Выберите действие:");
                System.out.println("3. Просмотр задач");
                System.out.println("4. Создание задачи");
                System.out.println("5. Удаление задачи");
                System.out.println("6. Редактирование задачи");
                System.out.println("7. Измененить статус задачи на [Выполнено]");
                System.out.println("8. Тестирование действий");
                System.out.println("0. Выход");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 3:
                        taskManager.viewTasks(loggedInUser);
                        break;
                    case 4:
                        createTaskFromConsole(taskManager, scanner, loggedInUser);
                        break;
                    case 5:
                        deleteTaskFromConsole(taskManager, scanner, loggedInUser);
                        break;
                    case 6:
                        editTaskFromConsole(taskManager, scanner, loggedInUser);
                        break;
                    case 7:
                        changeTaskStatusFromConsole(taskManager, scanner, loggedInUser);
                        break;
                    case 8:
                        performActionsTesting(taskManager, scanner);
                        break;
                    case 0:
                        taskManager.saveTasks(loggedInUser);
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Неверный ввод. Пожалуйста, выберите корректное действие.");
                }
            }
        }
    }

    private static void viewTasks(TaskManager taskManager, String loggedInUser) {
        taskManager.viewTasks(loggedInUser);
    }

    private static void createTaskFromConsole(TaskManager taskManager, Scanner scanner, String username) {
        String name = promptForName(scanner);
        String description = promptForDescription(scanner);
        String status = promptForStatus(scanner);

        taskManager.createTask(name, description, status, username);
    }

    private static String promptForName(Scanner scanner) {
        System.out.print("Введите имя задачи: ");
        return scanner.nextLine();
    }

    private static String promptForDescription(Scanner scanner) {
        System.out.print("Введите описание задачи: ");
        return scanner.nextLine();
    }

    private static String promptForStatus(Scanner scanner) {
        System.out.print("Введите статус задачи: ");
        return scanner.nextLine();
    }

    private static void deleteTaskFromConsole(TaskManager taskManager, Scanner scanner, String username) {
        long idToDelete = promptForTaskId(scanner);
        taskManager.deleteTask(idToDelete, username);
    }

    private static void editTaskFromConsole(TaskManager taskManager, Scanner scanner, String username) {
        long idToEdit = promptForTaskId(scanner);
        Task taskToEdit = taskManager.getTaskById(idToEdit, username);

        if (taskToEdit != null) {
            System.out.println("Текущее имя задачи: " + taskToEdit.getName());
            System.out.println("Текущее описание задачи: " + taskToEdit.getDescription());
            System.out.println("Текущий статус задачи: " + taskToEdit.getStatus());

            String newName = promptForName(scanner);
            String newDescription = promptForDescription(scanner);
            String newStatus = promptForStatus(scanner);

            if (!newName.isEmpty()) {
                taskToEdit.setName(newName);
                taskToEdit.setDescription(newDescription);
                taskToEdit.setStatus(newStatus);

                taskManager.saveTasks(username);
                System.out.println("Задача успешно отредактирована!");
            } else {
                System.out.println("Имя задачи не может быть пустым. Редактирование отменено.");
            }
        } else {
            System.out.println("Задача с указанным ID не найдена или не принадлежит пользователю.");
        }
    }

    private static void changeTaskStatusFromConsole(TaskManager taskManager, Scanner scanner, String username) {
        long idToChangeStatus = promptForTaskId(scanner);
        taskManager.changeTaskStatus(idToChangeStatus, username);
    }

    private static long promptForTaskId(Scanner scanner) {
        System.out.print("Введите ID задачи: ");
        long taskId = scanner.nextLong();
        scanner.nextLine();
        return taskId;
    }

    private static void performTesting(TaskManager taskManager, Scanner scanner) {

        isTesting = true;
        Logger logger = Logger.getInstance();

        System.out.println("Старт тестирования (регистрация/авторизация)...");
        logger.log("Старт тестирования (регистрация/авторизация)...");

        User testUser = new User("testUser", "testPassword");

        logger.log("Тестирование функции регистрации...");
        taskManager.registerUser(testUser.getUsername(), testUser.getPassword());

        logger.log("Тестирование функции авторизации...");

        loggedInUser = taskManager.loginUser(testUser.getUsername(), testUser.getPassword());
        if (loggedInUser != null) {
            logger.log("Авторизация успешна для пользователя: " + loggedInUser);
            // performActionsTesting(taskManager, scanner, loggedInUser);
        } else {
            logger.log("Ошибка авторизации для пользователя: " + testUser.getUsername());
        }
        System.out.println("Tестирование (регистрация/авторизация) завершено...детали в log.txt");
        logger.log("Tестирование (регистрация/авторизация) завершено");
        // logger.close();
    }

    private static void performActionsTesting(TaskManager taskManager, Scanner scanner) {
        Logger logger = Logger.getInstance();

        User testUser = new User("testUser", "testPassword");
        loggedInUser = taskManager.loginUser(testUser.getUsername(), testUser.getPassword());

        System.out.println("Старт тестирования других действи...");
        logger.log("Тестируем (Просмотр задач)...");
        taskManager.viewTasks(loggedInUser);

        logger.log("Тестирование (Создание задачи) ...");
        String taskName = "Test Task";
        String taskDescription = "This is a test task.";
        String taskStatus = "Pending";
        long taskId = taskManager.createTask(taskName, taskDescription, taskStatus, loggedInUser);
        logger.log("Запускаем (Просмотр задач) для проверки ...");
        taskManager.viewTasks(loggedInUser);
//      long taskId = 1714909480061L;
//
        if (taskId != -1) {
//        if (taskId == 1714909480061L) {
            logger.log("Тестируем (Редактирование задачи)...");
            String newName = "Test Task editing";
            String newTaskDescription = "This is a test task. editing";
            String newTaskStatus = "Pending editing";
            taskManager.editTask(taskId, newName, newTaskDescription, newTaskStatus, loggedInUser);
            logger.log("Запускаем (Просмотр задач) для проверки ....");
            taskManager.viewTasks(loggedInUser);

            logger.log("Тестируем (Измененить статус задачи на [Выполнено])...");
            taskManager.changeTaskStatus(taskId, loggedInUser);
            logger.log("Запускаем (Просмотр задач) для проверки ....");
            taskManager.viewTasks(loggedInUser);

            logger.log("Тестируем (Удаление задачи)...");
            taskManager.deleteTask(taskId, loggedInUser);
            logger.log("Запускаем (Просмотр задач) для проверки ...");
            taskManager.viewTasks(loggedInUser);

        } else {
            logger.log("Ошибка при создании задачи.");
        }
        // logger.close();
    }

}


