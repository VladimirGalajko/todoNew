package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.simple.JSONObject;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Task {

    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String name;
    private String description;
    private String status;
    private String username;
    @Builder.Default
    private String createdAt = getCurrentDateTime();
    @Builder.Default
    private String updatedAt = getCurrentDateTime();

    private static final Logger logger = LoggerFactory.getLogger(Task.class);
    private static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("description", description);
        json.put("status", status);
        json.put("username", username);
        json.put("createdAt", createdAt);
        json.put("updatedAt", updatedAt);
        return json;
    }

    public static Task fromJsonObject(JSONObject json) {
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        String username = (String) json.get("username");
        String description = (String) json.get("description");
        String status = (String) json.get("status");

        if (username == null) {
            logger.info("Ошибка: username для задачи с ID " + id + " отсутствует.");
        }

        return Task.builder()
                .name(name)
                .description(description)
                .status(status)
                .username(username)
                .build();
    }
}
