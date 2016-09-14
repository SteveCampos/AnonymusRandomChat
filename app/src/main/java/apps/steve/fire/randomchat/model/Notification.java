package apps.steve.fire.randomchat.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steve on 8/07/2016.
 */

public class Notification {

    private String body;
    private String sender;
    private String keyRoom;
    private String senderGender;
    private int status;

    public Notification(String body, String sender, String keyRoom, String senderGender, int status) {
        this.body = body;
        this.sender = sender;
        this.keyRoom = keyRoom;
        this.senderGender = senderGender;
        this.status = status;
    }

    public Notification() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getKeyRoom() {
        return keyRoom;
    }

    public void setKeyRoom(String keyRoom) {
        this.keyRoom = keyRoom;
    }

    public String getSenderGender() {
        return senderGender;
    }

    public void setSenderGender(String senderGender) {
        this.senderGender = senderGender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("body", body);
        result.put("sender", sender);
        result.put("keyRoom", keyRoom);
        result.put("senderGender", senderGender);
        result.put("status", status);
        return result;
    }
}
