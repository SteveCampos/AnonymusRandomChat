package apps.steve.fire.randomchat.model;

import java.util.ArrayList;

/**
 * Created by Steve on 19/05/2016.
 */
public class Message {

    ArrayList<ChatMessage> list =  new ArrayList<>();

    public Message() {
    }

    public ArrayList<ChatMessage> getList() {
        return list;
    }

    public void setList(ArrayList<ChatMessage> list) {
        this.list = list;
    }

    /*
    public static final int SENT = 100;
    public static final int DELIVERED = 200;


    private String androidID;
    private int messageStatus;
    private String messageText;
    private long messageTime;


    public Message() {
    }

    public String getAndroidID() {
        return androidID;
    }

    public void setAndroidID(String androidID) {
        this.androidID = androidID;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }*/
}
