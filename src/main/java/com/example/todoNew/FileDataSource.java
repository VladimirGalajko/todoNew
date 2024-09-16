package com.example.todoNew;

import java.util.List;

public class FileDataSource implements DataSource {

    @Override
    public void saveUser(User user) {
        JsonFileManager.saveUser(user);
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        return JsonFileManager.findUserByUsernameAndPassword(username, password);
    }

    @Override
    public void saveTask(Task task) {
        TaskManager taskManager = new TaskManager(new User(task.getUserId(), task.getUsername(), ""));
        taskManager.addTask(task);
    }

    @Override
    public void removeTask(long taskId, User currentUser) {
        TaskManager taskManager = new TaskManager(new User(0, "", ""));
        taskManager.removeTask(taskId,currentUser);
    }

    @Override
    public List<Task> getTasksForUser(String username) {
        TaskManager taskManager = new TaskManager(new User(0, username, ""));
        return taskManager.getTasks();
    }
}
