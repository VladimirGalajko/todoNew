package com.example.datasource;

import com.example.model.Task;
import com.example.model.User;

import java.util.List;
public interface DataSource {
    void saveTask(Task task);
    boolean removeTask(String taskId);
    List<Task> getTasksForUser(String username);

    void saveUser(User user);

    User findUserByUsernameAndPassword(String username, String password);
}

