package com.example.service;

import com.example.datasource.DataSource;
import com.example.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UserService {
        private final DataSource dataSource;

    public UserService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addUser(User user) {
        dataSource.saveUser(user);
        log.info("Пользователь добавлен: {}", user.getUsername());
    }

    public User findUserByUsernameAndPassword(String username, String password) {
        User user = dataSource.findUserByUsernameAndPassword(username, password);
        if (user == null) {
            log.warn("Пользователь с именем '{}' не найден или пароль неверный.", username);
        }
        return user;
    }
}
