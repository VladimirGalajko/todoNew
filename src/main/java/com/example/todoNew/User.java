package com.example.todoNew;

import org.json.simple.JSONObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private long id;
    private String username;
    private String password;

    // Конструктор для создания нового пользователя с автоматическим генерацией ID
    public User(String username, String password) {
        this.id = System.currentTimeMillis(); // Пример генерации уникального ID на основе текущего времени
        this.username = username;
        this.password = password;
    }

    // Конструктор для создания пользователя с указанным ID (например, при чтении из файла)
    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Метод для создания пользователя из объекта JSON
    public static User fromJsonObject(JSONObject obj) {
        long id = (long) obj.get("id");
        String username = (String) obj.get("username");
        String password = (String) obj.get("password");
        return new User(id, username, password);
    }

    // Метод для преобразования пользователя в объект JSON
    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("username", this.username);
        obj.put("password", this.password);
        return obj;
    }
}
