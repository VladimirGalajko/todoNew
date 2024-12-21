package com.example.model;


import com.fasterxml.jackson.annotation.JsonCreator;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;


@Data
@NoArgsConstructor

public class User {

    private long id;
    private String username;
    private String password;

    @JsonCreator
    public User(@JsonProperty("id") long id,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    public User(String username, String password) {
        this(System.currentTimeMillis(), username, password);
    }


//    public JSONObject toJsonObject() {
//        JSONObject json = new JSONObject();
//        json.put("id", id);
//        json.put("username", username);
//        json.put("password", password);
//        return json;
//    }
//
//    public static User fromJsonObject(JSONObject jsonObject) {
//        long id = (long) jsonObject.get("id");
//        String username = (String) jsonObject.get("username");
//        String password = (String) jsonObject.get("password");
//        return new User(id, username, password);
//    }
}
