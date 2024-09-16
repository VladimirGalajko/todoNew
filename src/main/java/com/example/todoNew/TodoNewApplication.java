package com.example.todoNew;

public class TodoNewApplication {
    public static void main(String[] args) {
        boolean useH2 = true;
        DataSource dataSource = DataSourceFactory.createDataSource(useH2);

        LogManager.getInstance().setLogDestination(LogManager.LogDestination.LOGGER_ONLY); // LOGGER_ONLY  SYSTEM_OUT_ONLY ALL


        User user = new User("testuser", "testpassword");
        dataSource.saveUser(user);

//        User foundUser = dataSource.findUserByUsernameAndPassword("testuser", "testpassword");
//        System.out.println("Found user: " + foundUser);



        TaskManager.UserRegistration("testuser", "testpassword",useH2);
        TaskManager.UserLogin("testuser", "testpassword",useH2);

        TaskManager.TaskCreation("testuser", "testpassword", 5, "пятая задача", "This is a test task 5.", "pending",useH2);
        TaskManager.TaskCreation("testuser", "testpassword", 4, "четвертая задача", "This is a test task 4.", "pending",useH2);
        TaskManager.TaskCreation("testuser", "testpassword", 8, "восьмая задача", "This is a test task 8", "pending",useH2);
        TaskManager.TaskDeletion("testuser", "testpassword", 4,useH2);
        TaskManager.TaskDeletion("testuser", "testpassword", 5,useH2);
        TaskManager.TaskViewing("testuser", "testpassword",useH2);
    }
}
