package com.example.todoNew;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2DataSource implements DataSource {
    private JdbcDataSource dataSource;

    public H2DataSource() {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        createTables();
    }

    private void createTables() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) UNIQUE, password VARCHAR(255))");
            stmt.execute("CREATE TABLE IF NOT EXISTS tasks (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), description VARCHAR(255), status VARCHAR(255), createdAt VARCHAR(255), updatedAt VARCHAR(255), username VARCHAR(255), userId BIGINT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(User user) {
        String sql = "MERGE INTO users (id, username, password) KEY (username) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong("id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveTask(Task task) {
        LogManager logManager = LogManager.getInstance();
        String sql = "MERGE INTO tasks (id, name, description, status, createdAt, updatedAt, username, userId) KEY (id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, task.getId());
            stmt.setString(2, task.getName());
            stmt.setString(3, task.getDescription());
            stmt.setString(4, task.getStatus());
            stmt.setString(5, task.getCreatedAt());
            stmt.setString(6, task.getUpdatedAt());
            stmt.setString(7, task.getUsername());
            stmt.setLong(8, task.getUserId());
            stmt.executeUpdate();
            logManager.logInfo("Сохраним задачу в таблицу");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> getTasksForUser(String username) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE username = ?";
        LogManager logManager = LogManager.getInstance();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            logManager.logInfo("Получение задач для пользователя: " + username);

            while (rs.next()) {
                Task task = new Task(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("username"),
                        rs.getLong("userId"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt")
                );
                tasks.add(task);
                logManager.logInfo("задача: " + task);
            }
        } catch (SQLException e) {
            logManager.logError("Ошибка при получении задач для пользователя: " + username, e);
        }

        logManager.logInfo("Завершение получения задач для пользователя: " + username + ". Всего задач: " + tasks.size());
        return tasks;
    }


    @Override
    public void removeTask(long taskId, User currentUser) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, taskId);
            stmt.executeUpdate();
            LogManager.getInstance().logInfo("Задача с ID " + taskId + " удалена.");
        } catch (SQLException e) {
            e.printStackTrace();
            LogManager.getInstance().logInfo("Ошибка при удалении задачи");


        }
    }
}
