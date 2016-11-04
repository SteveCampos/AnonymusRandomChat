package apps.steve.fire.randomchat.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.activities.MainActivity;
import apps.steve.fire.randomchat.model.Country;
import apps.steve.fire.randomchat.model.Notification;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Steve on 7/07/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";
    public static final String ACTION_NOTIFY_NEW_NOTIFICATION = "action_notify_new_notification";

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.d(TAG, "onMessageSent s: "+ s);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "ON CREATE");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ON DESTROY");
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived");
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "remoteMessage.getData(): " + remoteMessage.getData());

        String body = remoteMessage.getData().get("body");
        String senderGender = remoteMessage.getData().get("senderGender");
        String keyRoom = remoteMessage.getData().get("keyRoom");
        String from = remoteMessage.getData().get("deviceFrom");
        int countryId = 1;

        try{
            countryId = Integer.parseInt(remoteMessage.getData().get("countryId"));
        }catch (Exception e){
            Log.d(TAG, "Exception: " + e);
            Toast.makeText(getApplicationContext(), "onMessageReceived Exception: " + e, Toast.LENGTH_SHORT).show();
        }

        // Handle data payload of FCM messages.
        /*Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " +
                remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
        Log.d(TAG, "FCM " + remoteMessage.getNotification().getBody());
*/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        //int noReaded = preferences.getInt(Constants.PREF_NO_READED, 0);
        int countNotification = preferences.getInt(Constants.PREF_NOTI_COUNT, 0);
        int notificationFromCount = preferences.getInt(Constants.PREF_NOTI_COUNT_FROM, 0);
        String userIdLastNoti = preferences.getString(Constants.PREF_NOTI_USER_ID_LAST, "");
        String messages = preferences.getString(Constants.PREF_NOTI_MESSAGES, "");




        ++countNotification;



        if (notificationFromCount >=1){
            //DISPLAY A MESSAGE LIKE 8 messages from 2 chats.
            if (from.equals(userIdLastNoti)){
                //Las notificaciones vienen del mismo usuario
                editor.putString(Constants.PREF_NOTI_MESSAGES, body +"\n"+ messages);

                Notification notification = new Notification();
                notification.setFrom(from);
                notification.setBody(body +"\n"+ messages);
                notification.setCountryId(countryId);
                notification.setSenderGender(senderGender);
                notification.setKeyRoom(keyRoom);
                sendNotification(notification);
                //Toast.makeText(this, countNotification + " del mismo usuario.", Toast.LENGTH_SHORT).show();
            }else{
                editor.putString(Constants.PREF_NOTI_USER_ID_LAST, from);

                ++notificationFromCount;

                //Toast.makeText(this, countNotification +"  de " + notificationFromCount + " usuario(s)", Toast.LENGTH_SHORT).show();
            }

        }else{

            ++notificationFromCount;
            editor.putString(Constants.PREF_NOTI_MESSAGES, body +"\n"+ messages);


            Notification notification = new Notification();
            notification.setFrom(from);
            notification.setBody(body);
            notification.setCountryId(countryId);
            notification.setSenderGender(senderGender);
            notification.setKeyRoom(keyRoom);
            sendNotification(notification);
        }

        editor.putInt(Constants.PREF_NOTI_COUNT, countNotification);
        editor.putInt(Constants.PREF_NOTI_COUNT_FROM, notificationFromCount);
        editor.apply();
        ShortcutBadger.applyCount(getApplicationContext(), countNotification);

        //sendNotificationPromoBroadcast(remoteMessage);
        //super.onMessageReceived(remoteMessage);
    }



    private void sendNotificationPromoBroadcast(RemoteMessage remoteMessage) {
        Intent intent = new Intent(ACTION_NOTIFY_NEW_NOTIFICATION);
        intent.putExtra("title", remoteMessage.getNotification().getTitle());
        intent.putExtra("description", remoteMessage.getNotification().getBody());
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(intent);
    }

    private void sendNotification(Notification notification) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Country country = new Country(getApplicationContext(), notification.getCountryId());

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(country.getDrawableID())
                .setContentTitle(notification.getSenderGender())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}
