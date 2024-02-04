package com.example.todoNew;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static User fromJsonObject(JSONObject jsonObject) {
        String username = (String) jsonObject.get("username");
        String password = (String) jsonObject.get("password");


        return new User(username, password);
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", getUsername());
        jsonObject.put("password", getPassword());

        return jsonObject;
    }
}
