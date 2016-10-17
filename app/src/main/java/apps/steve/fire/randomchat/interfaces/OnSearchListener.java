package apps.steve.fire.randomchat.interfaces;

import apps.steve.fire.randomchat.model.Emisor;

/**
 * Created by Steve on 22/08/2016.
 */

public interface OnSearchListener {
    void onChatLaunched(int countryId, String key);
    void onFailed(String error);
    void onNotCountrySelected();
}
