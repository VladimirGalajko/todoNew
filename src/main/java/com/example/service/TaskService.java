package com.example.service;

import com.example.datasource.DataSource;
import com.example.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Slf4j
@Service
public class TaskService implements TaskStorage {

    private final DataSource dataSource;

    public TaskService(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public boolean removeTask(String taskId) {
        return dataSource.removeTask(taskId);
    }
    @Override
    public void createTask(Task task) {
        task.setId(UUID.randomUUID().toString());
        String currentDateTime = LocalDateTime.now().toString();
        task.setCreatedAt(currentDateTime);
        task.setUpdatedAt(currentDateTime);
        dataSource.saveTask(task);
    }

    @Override
    public List<Task> getTasksForUser(String username) {
        return dataSource.getTasksForUser(username);
    }



}
