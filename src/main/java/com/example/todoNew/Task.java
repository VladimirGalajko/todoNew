package com.example.todoNew;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Task {
    private long id;
    private String name;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String username;
    private long userId;

    public Task() {
    }
    public Task(long id, String name, String description, String status, String username, long userId, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.username = username;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}