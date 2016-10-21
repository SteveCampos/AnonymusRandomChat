package apps.steve.fire.randomchat.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steve on 8/07/2016.
 */

public class Notification {

    private String from;
    private String to;
    private String body;

    private int countryId;
    private String keyRoom;
    private String senderGender;
    private int status;

    public Notification() {
    }

    public Notification(String from, String to, String body, int countryId, String keyRoom, String senderGender, int status) {
        this.from = from;
        this.to = to;
        this.body = body;
        this.countryId = countryId;
        this.keyRoom = keyRoom;
        this.senderGender = senderGender;
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
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
        result.put("from", from);
        result.put("to", to);
        result.put("body", body);

        result.put("countryId", countryId);
        result.put("keyRoom", keyRoom);
        result.put("senderGender", senderGender);
        result.put("status", status);
        return result;
    }
}
