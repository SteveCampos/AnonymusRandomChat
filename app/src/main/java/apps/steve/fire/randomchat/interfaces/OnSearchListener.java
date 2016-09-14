package apps.steve.fire.randomchat.interfaces;

import apps.steve.fire.randomchat.model.Emisor;

/**
 * Created by Steve on 22/08/2016.
 */

public interface OnSearchListener {
    //void onUserCreated(boolean success, String error);
    void onChatPared(String key, String startType, Emisor me, Emisor emisor);
    void onChatCreated(String key, String startType, Emisor me, Emisor receptor);
    void onFailed(String error);
}
