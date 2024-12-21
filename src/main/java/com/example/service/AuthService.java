package com.example.service;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class AuthService {

    @Getter
    private final String username;
    private final String password;
    @Getter
    private boolean loggedIn = false;

public AuthService(SecurityProperties securityProperties) {
    this.username = securityProperties.getUsername();
    this.password = securityProperties.getPassword();
}

    public boolean login(String inputUsername, String inputPassword) {
        if (username.equals(inputUsername) && password.equals(inputPassword)) {
            loggedIn = true;
            log.info("User '{}' successfully logged in.", inputUsername);
            return true;
        }
        log.warn("Failed login attempt for username '{}'.", inputUsername);
        return false;
    }

    public void logout() {
        loggedIn = false;
        log.info("User logged out.");
    }


}
