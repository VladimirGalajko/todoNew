package com.example.todoNew;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
public class Task {
    private long id;
    private String name;
    private String description;
    private String status;
    private String dateStart;
    private String dateEnd;
    private String username;

    public Task(long id, String name, String description, String status, String dateStart, String dateEnd, String username) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.username = username;
    }

    public static Task fromJsonObject(JSONObject jsonObject) {

        long id = (long) jsonObject.get("id");
        String name = (String) jsonObject.get("name");
        String description = (String) jsonObject.get("description");
        String status = (String) jsonObject.get("status");
        String dateStart = (String) jsonObject.get("dateStart");
        String dateEnd = (String) jsonObject.get("dateEnd");
        String username = (String) jsonObject.get("username");

        return new Task(id, name, description, status, dateStart, dateEnd, username);

    }

    public JSONObject toJsonObject() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", getId());
        jsonObject.put("name", getName());
        jsonObject.put("description", getDescription());
        jsonObject.put("status", getStatus());
        jsonObject.put("dateStart", getDateStart());
        jsonObject.put("dateEnd", getDateEnd());
        jsonObject.put("username", getUsername());

        return jsonObject;
    }

    @Override
    public String toString() {
        return "Task {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
