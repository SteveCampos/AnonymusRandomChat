package apps.steve.fire.randomchat.interfaces;

import java.util.List;

import apps.steve.fire.randomchat.model.ChatMessage;
import apps.steve.fire.randomchat.model.Chater;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.model.RandomChat;

/**
 * Created by Steve on 24/08/2016.
 */

public interface OnRoomListener {
    void onMessagedSended(boolean success, ChatMessage message, String error);
    void onReadMessages(List<ChatMessage> messages);

    void onMeReaded(Emisor me);
    void onHimReaded(Emisor him);
    void onRoomStateChanged(String state);
    void onRoomActionChanged(String action);
    void onRoomHot(boolean isHot);

    void onReceptorConnectionChanged(String state, long lastConnection);
}
