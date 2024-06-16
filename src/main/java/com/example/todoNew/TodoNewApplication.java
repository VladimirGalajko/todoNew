package com.example.todoNew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TodoNewApplication {
    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите действие:");
        System.out.println("1. Вход");
        System.out.println("2. Регистрация");
        System.out.println("3. Тестирование");

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
            JsonFileManager.saveUser(currentUser);
            System.out.println("Пользователь успешно зарегистрирован: " + username);
        } else if (action == 3) {
            testFunctions();
            return;
        } else {
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
                    taskManager.displayTasks();
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

    private static void testFunctions() {
     //   testUserRegistration();
        testUserLogin();
        testTaskCreation();
        testTaskViewing();
        testTaskDeletion();
    }

    private static void testUserRegistration() {
        User testUser = new User("testuser", "testpassword");
        JsonFileManager.saveUser(testUser);

    }
    private static void testUserLogin() {
        logger.info("Начало testUserLogin");
        User currentUser = null;
        currentUser = JsonFileManager.findUserByUsernameAndPassword("testuser", "testpassword");
        if (currentUser == null) {
            logger.debug("Пользователь не найден или неверный пароль.");
            return;
        } else {
            logger.debug("Вход выполнен для пользователя: " + currentUser.getUsername());
        }
        logger.info("Завершение testUserLogin");
    }


    private static void testTaskCreation() {
        logger.info("Начало testTaskCreation");

        User existingUser = JsonFileManager.findUserByUsernameAndPassword("testuser", "testpassword");
        if (existingUser == null) {
            User testUser = new User("testuser", "testpassword");
            JsonFileManager.saveUser(testUser);
        }

        User currentUser = JsonFileManager.findUserByUsernameAndPassword("testuser", "testpassword");
        if (currentUser == null) {
            logger.debug("Пользователь не найден или неверный пароль.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager(scanner, currentUser);

        // Добавляем тестовую задачу
        Task testTask = new Task(
                1, "Test Task", "This is a test task.", "pending",
                currentUser.getUsername(), currentUser.getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy HH:mm:ss")),
                ""
        );

        taskManager.addTask(testTask);

        taskManager.saveTasksToFile();

        boolean taskFound = false;
        for (Task task : taskManager.getTasks()) {
            if (task.getName().equals("Test Task") && task.getUsername().equals(currentUser.getUsername())) {
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

    private static void testTaskViewing() {
        logger.info("Начало testTaskViewing");

        User existingUser = JsonFileManager.findUserByUsernameAndPassword("testuser", "testpassword");
        if (existingUser == null) {
            User testUser = new User("testuser", "testpassword");
            JsonFileManager.saveUser(testUser);

        }

        User currentUser = JsonFileManager.findUserByUsernameAndPassword("testuser", "testpassword");
        if (currentUser == null) {
            logger.debug("Пользователь не найден или неверный пароль.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager(scanner, currentUser);

        Task testTask1 = new Task(
                1, "Test Task 1", "This is test task 1.", "pending",
                currentUser.getUsername(), currentUser.getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy HH:mm:ss")),
                ""
        );

        Task testTask2 = new Task(
                2, "Test Task 2", "This is test task 2.", "completed",
                currentUser.getUsername(), currentUser.getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy HH:mm:ss")),
                ""
        );

        taskManager.addTask(testTask1);
        taskManager.addTask(testTask2);
        taskManager.saveTasksToFile();

        logger.debug("Просмотр задач для пользователя: " + currentUser.getUsername());
        taskManager.displayTasks();

        logger.info("Завершение testTaskViewing");
    }
    private static void testTaskDeletion() {
       logger.info("Начало теста удаления задач");

        User currentUser = JsonFileManager.findUserByUsernameAndPassword("testuser", "testpassword");
        if (currentUser == null) {
            User testUser = new User("testuser", "testpassword");
            JsonFileManager.saveUser(testUser);
            currentUser = testUser;
        }

        TaskManager taskManager = new TaskManager(new Scanner(System.in), currentUser);

        Task testTask1 = new Task(
                1, "Task to Delete 1", "This task should be deleted.", "pending",
                currentUser.getUsername(), currentUser.getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy HH:mm:ss")),
                ""
        );

        Task testTask2 = new Task(
                2, "Task to Delete 2", "This task should also be deleted.", "completed",
                currentUser.getUsername(), currentUser.getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy HH:mm:ss")),
                ""
        );

        // Добавление тестовых задач в TaskManager
        taskManager.addTask(testTask1);
        taskManager.addTask(testTask2);

        taskManager.saveTasksToFile();

        logger.info("Задачи до удаления:");
        taskManager.displayTasks();

        // Удаление первой задачи
        long taskIdToDelete = testTask1.getId();
        taskManager.removeTask(taskIdToDelete);

        // Удаление второй задачи
        taskIdToDelete = testTask2.getId();
        taskManager.removeTask(taskIdToDelete);

        taskManager.saveTasksToFile();

        logger.info("Задачи после удаления:");
        taskManager.displayTasks();
        logger.info("Завершение теста удаления задач");
    }


}