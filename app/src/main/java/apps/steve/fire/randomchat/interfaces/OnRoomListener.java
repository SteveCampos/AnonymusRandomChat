package apps.steve.fire.randomchat.interfaces;

import java.util.List;

import apps.steve.fire.randomchat.model.ChatMessage;
import apps.steve.fire.randomchat.model.Chater;

/**
 * Created by Steve on 24/08/2016.
 */

public interface OnRoomListener {
    void onChatHistoryCreated(boolean success, String error);
    void onMessagedSended(boolean success, ChatMessage message, String error);
    void onReadMessages(List<ChatMessage> messages);
    void onReceptorConnectionChanged(String state, long lastConnection);
    void onChatStateChanged(String state);

}
