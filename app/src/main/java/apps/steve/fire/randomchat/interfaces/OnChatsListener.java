package apps.steve.fire.randomchat.interfaces;

import java.util.List;

import apps.steve.fire.randomchat.model.HistorialChat;

/**
 * Created by Steve on 29/08/2016.
 */

public interface OnChatsListener {
    void onChatChangedListener(boolean success, List<HistorialChat> chatList);
}
