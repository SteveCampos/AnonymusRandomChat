package apps.steve.fire.randomchat.interfaces;

import android.view.View;

import apps.steve.fire.randomchat.model.RandomChat;

/**
 * Created by Steve on 30/09/2016.
 */

public interface OnChatItemClickListener {
    void onChatItemClicked(RandomChat item, View view, boolean liked);
    void onLoadMore();
}
