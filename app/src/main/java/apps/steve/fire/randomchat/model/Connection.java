package apps.steve.fire.randomchat.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steve on 02/09/2016.
 */

public class Connection {
    private String state;
    private long lastConnection;

    public Connection(String state, long lastConnection) {
        this.state = state;
        this.lastConnection = lastConnection;
    }

    public Connection() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("state", state);
        result.put("lastConnection", lastConnection);
        return result;
    }
}
