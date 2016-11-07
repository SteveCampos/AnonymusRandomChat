package apps.steve.fire.randomchat.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Steve on 8/07/2016.
 */

public class StartFirebaseAtBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //context.startService(new Intent(context, NotificationFireListener.class));
    }
}
