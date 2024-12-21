package com.example.service;

import com.example.model.Task;
import java.util.List;

public interface TaskStorage {
    void createTask(Task task);
    List<Task> getTasksForUser(String username);
    boolean removeTask(String id);
}

