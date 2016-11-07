package apps.steve.fire.randomchat.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steve on 9/07/2016.
 */
public class

User {
    private String name;
    private Connection connection;
    private long timeCreated;
    private int age;
    private String genero;


    public User() {
    }

    public User(String name, Connection connection, long timeCreated, int age, String genero) {
        this.name = name;
        this.connection = connection;
        this.timeCreated = timeCreated;
        this.age = age;
        this.genero = genero;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("connection", connection.toMap());
        result.put("timeCreated", timeCreated);
        result.put("age", age);
        result.put("genero", genero);
        return result;
    }
}
