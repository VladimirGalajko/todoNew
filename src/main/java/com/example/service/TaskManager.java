


package com.example.service;

import com.example.datasource.DataSource;
import com.example.model.Task;
import com.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskManager implements TaskStorage {

    private final DataSource dataSource;

    public TaskManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Override
    public void addTask(Task task) {
        dataSource.saveTask(task);
    }

    @Override
    public boolean removeTask(String taskId) {
       return dataSource.removeTask(taskId);
    }

    @Override
    public List<Task> getTasksForUser(String username) {
        return dataSource.getTasksForUser(username);
    }

    public void addUser(User user) {
        dataSource.saveUser(user);
    }

     public User findUserByUsernameAndPassword(String username, String password) {
        return dataSource.findUserByUsernameAndPassword(username, password);
    }
}
