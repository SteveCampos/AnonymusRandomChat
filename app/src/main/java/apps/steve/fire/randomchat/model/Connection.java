package apps.steve.fire.randomchat.model;

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
}
