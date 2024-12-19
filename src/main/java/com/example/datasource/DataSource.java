package com.example.datasource;

import com.example.model.Task;
import com.example.model.User;

import java.util.List;
public interface DataSource {
    void saveTask(Task task);
    boolean removeTask(String taskId);
    List<Task> getTasksForUser(String username);

    // Новый метод для сохранения пользователя
    void saveUser(User user);

    // Новый метод для поиска пользователя
    User findUserByUsernameAndPassword(String username, String password);
}

//public interface DataSource {
//
//    void saveUser(User user);
//    User findUserByUsernameAndPassword(String username, String password);
//    void saveTask(Task task);
//    void removeTask(long taskId, User user);
//    List<Task> getTasksForUser(String username);
//}
