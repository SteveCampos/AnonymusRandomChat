package apps.steve.fire.randomchat.interfaces;

import android.view.View;

import java.util.List;

import apps.steve.fire.randomchat.model.RandomChat;

/**
 * Created by Steve on 19/12/2016.
 */

public interface OnPostListener {
    void onPostItemClicked(RandomChat item, View v, boolean bool);
    void onLoadMore();
    void onPostCreated();
    void onFailed(String error);
    void onChatLaunched(int countryId, String key);
}
