package com.example.service;

import com.example.model.User;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Getter
    private  String username;
    private  String password;
    @Getter
    private boolean loggedIn = false;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
//    public AuthService(
//            @Value("${app.security.username}") String username,
//            @Value("${app.security.password}") String password
//    ) {
//        this.username = username;
//        this.password = password;
//    }
@Value("${app.security.username}")
public void setUsername(String username) {
    this.username = username;
}

    @Value("${app.security.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login(String inputUsername, String inputPassword) {
        if (username.equals(inputUsername) && password.equals(inputPassword)) {
            loggedIn = true;
            return true;
        }
        return false;
    }

    public void logout() {
        loggedIn = false;
    }

}
