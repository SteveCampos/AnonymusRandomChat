package apps.steve.fire.randomchat.interfaces;

import java.util.List;

import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.RandomChat;

/**
 * Created by Steve on 29/08/2016.
 */

public interface OnChatsListener {
    void onChatChangedListener(boolean success, List<RandomChat> chatList);
    void onChatsHotListener(boolean success, List<RandomChat> chatList);
    void onLoadMore();
    void onChatItemClicked(RandomChat item);
}
