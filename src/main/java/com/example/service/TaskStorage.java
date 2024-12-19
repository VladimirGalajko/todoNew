package com.example.service;

import com.example.model.Task;
import com.example.model.User;

import java.util.List;

public interface TaskStorage {
    void addTask(Task task);

    List<Task> getTasksForUser(String username);

    boolean removeTask(String id);
}

