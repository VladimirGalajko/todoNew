package com.example.todoNew;

import java.util.List;

public interface DataSource {
    void saveUser(User user);
    User findUserByUsernameAndPassword(String username, String password);
    void saveTask(Task task);
    void removeTask(long taskId, User user);
    List<Task> getTasksForUser(String username);
}
