


package com.example.service;

import com.example.datasource.DataSource;
import com.example.model.Task;
import com.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService implements TaskStorage {

    private final DataSource dataSource;

    public TaskService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


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

    public void createTask(Task task) {
        task.setId(UUID.randomUUID().toString());
        String currentDateTime = LocalDateTime.now().toString();
        task.setCreatedAt(currentDateTime);
        task.setUpdatedAt(currentDateTime);
        dataSource.saveTask(task);
    }

}
