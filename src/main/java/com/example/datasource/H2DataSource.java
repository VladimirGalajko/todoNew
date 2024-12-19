package com.example.datasource;


import com.example.model.Task;
import com.example.model.User;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component("h2DataSource")
@Profile("h2")
public class H2DataSource implements DataSource {
    private static final Logger logger = LoggerFactory.getLogger(H2DataSource.class);
    private final JdbcDataSource dataSource;
    public H2DataSource() {
        this.dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        createTables();
    }

    private void createTables() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) UNIQUE, password VARCHAR(255))");
            stmt.execute("CREATE TABLE IF NOT EXISTS tasks (id VARCHAR(36) PRIMARY KEY, name VARCHAR(255), description VARCHAR(255), status VARCHAR(255), createdAt VARCHAR(255), updatedAt VARCHAR(255), username VARCHAR(255), userId BIGINT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (id, username, password) VALUES (?, ?, ?)")) {
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
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
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
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO tasks (id, name, description, status, createdAt, updatedAt, username, userId) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, task.getId());
            stmt.setString(2, task.getName());
            stmt.setString(3, task.getDescription());
            stmt.setString(4, task.getStatus());
            stmt.setString(5, task.getCreatedAt());
            stmt.setString(6, task.getUpdatedAt());
            stmt.setString(7, task.getUsername());
//            stmt.setLong(8, task.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> getTasksForUser(String username) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tasks WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

             var task = Task.builder()
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .status(rs.getString("status"))
                        .username(rs.getString("username"))
                        .build();
                if (task.getUsername() == null) {
                    logger.info("username null -> continue");
                    continue;
                }
                tasks.add(task);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public boolean removeTask(String taskId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
            stmt.setString(1, taskId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
