package com.example.todoNew;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFileManager {
    private static final String USERS_FILE = "users.json";
    private static List<User> users = new ArrayList<>();

    static {
        loadUsers();
    }

    public static void saveUser(User user) {
        // Проверяем, что пользователь с таким именем уже не существует
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                System.out.println("Пользователь с именем '" + user.getUsername() + "' уже существует.");
                return;
            }
        }

        users.add(user);
        saveUsersToFile();
    }

    private static void loadUsers() {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(USERS_FILE)) {
            Object obj = jsonParser.parse(reader);
            JSONArray userList = (JSONArray) obj;

            for (Object userObj : userList) {
                JSONObject userJson = (JSONObject) userObj;
                User user = User.fromJsonObject(userJson);
                users.add(user);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void saveUsersToFile() {
        JSONArray userList = new JSONArray();
        for (User user : users) {
            userList.add(user.toJsonObject());
        }

        try (FileWriter file = new FileWriter(USERS_FILE)) {
            file.write(userList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User findUserByUsernameAndPassword(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
