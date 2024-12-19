package com.example.model;


import lombok.Getter;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Getter
public class User {
    private final long id;
    private final String username;
    private final String password;

    private static final Logger logger = LoggerFactory.getLogger(User.class);
    public User(String username, String password) {
        this.id = System.currentTimeMillis();
        this.username = username;
        this.password = password;
    }


    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }


    public static User fromJsonObject(JSONObject obj) {
        long id = (long) obj.get("id");
        String username = (String) obj.get("username");
        String password = (String) obj.get("password");
        return new User(id, username, password);
    }


    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("username", this.username);
        obj.put("password", this.password);
        return obj;
    }
}