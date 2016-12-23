package apps.steve.fire.randomchat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.Utils;

/**
 * Created by madhur on 17/01/15.
 */
@IgnoreExtraProperties
public class ChatMessage {


    public final static String AUTOMATIC= "4650";
    public final static String PARED = "PARED";
    public final static String PARED_BY_POST = "PARED_BY_POST";
    public final static String BLOCKED = "BLOCKED";
    public final static String UNBLOCKED = "UNBLOCKED";
    public final static String WAITING = "WAITING";


    public final static String KEY_DEFAULT = "-KVk11235813213455";
    private String messageText;
    private String androidID;
    private int messageStatus;
    private String messageType;
    private long messageTime;

    @JsonIgnore
    private String keyMessage;

    public ChatMessage() {
    }

    public ChatMessage(String messageText, String androidID, int messageStatus, String messageType, long messageTime, String keyMessage) {
        this.messageText = messageText;
        this.androidID = androidID;
        this.messageStatus = messageStatus;
        this.messageType = messageType;
        this.messageTime = messageTime;
        this.keyMessage = keyMessage;
    }

    public long getMessageTime() {
        return Utils.abs(messageTime);
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }


    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
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

    public String getKeyMessage() {
        return keyMessage;
    }

    public void setKeyMessage(String keyMessage) {
        this.keyMessage = keyMessage;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("messageText", messageText);
        result.put("androidID", androidID);
        result.put("messageStatus", messageStatus);
        result.put("messageType", messageType);
        result.put("messageTime", messageTime);
        return result;
    }

    public static ChatMessage getWaitingMessage(){
        return new ChatMessage(
                WAITING,
                AUTOMATIC,
                Constants.SENT,
                Constants.MESSAGE_TEXT,
                -new Date().getTime(),
                ""
        );
    }
    public static ChatMessage getParedMessage(){
        return new ChatMessage(
                PARED,
                AUTOMATIC,
                Constants.SENT,
                Constants.MESSAGE_TEXT,
                -new Date().getTime(),
                ""
        );
    }
    public static ChatMessage getParedByPostMessage(){
        return new ChatMessage(
                PARED_BY_POST,
                AUTOMATIC,
                Constants.SENT,
                Constants.MESSAGE_TEXT,
                -new Date().getTime(),
                ""
        );
    }


    public static ChatMessage getBlockedMessage(){
        return new ChatMessage(
                BLOCKED,
                AUTOMATIC,
                Constants.SENT,
                Constants.MESSAGE_TEXT,
                -new Date().getTime(),
                ""
        );
    }
    public static ChatMessage getUnblockedMessage(){
        return new ChatMessage(
                UNBLOCKED,
                AUTOMATIC,
                Constants.SENT,
                Constants.MESSAGE_TEXT,
                -new Date().getTime(),
                ""
        );
    }

}

