package apps.steve.fire.randomchat.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Locale;

import apps.steve.fire.randomchat.ChatActivity;
import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.activities.MainActivity;
import apps.steve.fire.randomchat.model.ChatMessage;
import apps.steve.fire.randomchat.model.Country;
import apps.steve.fire.randomchat.model.Notification;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Steve on 7/07/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";
    public static final String ACTION_NOTIFY_NEW_NOTIFICATION = "action_notify_new_notification";
    private static final int MY_NOTIFICATION_ID = 4096;

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.d(TAG, "onMessageSent s: " + s);
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
        int countryId = Country.PERU;

        try {
            countryId = Integer.parseInt(remoteMessage.getData().get("countryId"));
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e);
            Toast.makeText(getApplicationContext(), "onMessageReceived Exception: " + e, Toast.LENGTH_SHORT).show();
        }

        if (from.equals(ChatMessage.AUTOMATIC)){
            switch (body){
                case ChatMessage.WAITING:
                    body = getString(R.string.chat_state_waiting);
                    break;
                case ChatMessage.BLOCKED:
                    body = getString(R.string.message_automatic_blocked);
                    break;
                case ChatMessage.PARED:
                    body = getString(R.string.message_automatic_pared);
                    break;
                case ChatMessage.UNBLOCKED:
                    body = getString(R.string.message_automatic_unblocked);
                    break;
                default:
                    body = "default xd";
                    break;
            }
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
        //int countNotification = preferences.getInt(Constants.PREF_NOTI_COUNT, 0);
        //int notificationFromCount = preferences.getInt(Constants.PREF_NOTI_COUNT_FROM, 0);
        //String userIdLastNoti = preferences.getString(Constants.PREF_NOTI_USER_ID_LAST, "");
        //String messages = preferences.getString(Constants.PREF_NOTI_MESSAGES, "");

        String notificationsKeys = preferences.getString(Constants.PREF_NOTI_KEYS, "");
        String messagesRoom = preferences.getString(keyRoom, "");
        Log.d(TAG, "PREF_NOTI_KEYS : " + notificationsKeys);
        Log.d(TAG, "messages no readed from "+ keyRoom+": " + messagesRoom);

        notificationsKeys = notificationsKeys.contains(keyRoom) ? notificationsKeys : keyRoom + "." + notificationsKeys;
        Log.d(TAG, "notificationsKeys: " + notificationsKeys);

        //++countNotification;
/*
        if (notificationFromCount >=1){
            //DISPLAY A MESSAGE LIKE 8 messages from 2 chats.
            if (from.equals(userIdLastNoti)){
                //Las notificaciones vienen del mismo usuario

                Notification notification = new Notification();
                notification.setFrom(from);
                notification.setBody(body +"\n"+ messages);
                notification.setCountryId(countryId);
                notification.setSenderGender(senderGender);
                notification.setKeyRoom(keyRoom);
                sendNotificationInboxStyle(notification, getApplicationContext());
                //Toast.makeText(this, countNotification + " del mismo usuario.", Toast.LENGTH_SHORT).show();
            }else{
                editor.putString(Constants.PREF_NOTI_USER_ID_LAST, from);
                ++notificationFromCount;

                sendNotification(getApplicationContext(), notificationFromCount, messages +"\n"+ body);
                //Toast.makeText(this, countNotification +"  de " + notificationFromCount + " usuario(s)", Toast.LENGTH_SHORT).show();
            }

        }else{*/

        //++notificationFromCount;
        //editor.putString(Constants.PREF_NOTI_MESSAGES, messages + "\n" + body);
        editor.putString(Constants.PREF_NOTI_KEYS, notificationsKeys);
        editor.putString(keyRoom + "_" + Constants.PREF_NOTI_KEY_EXTRAS, from + "-" + countryId + "-" + senderGender);
        editor.putString(keyRoom, body + "\n" + messagesRoom);

        /*
        Notification notification = new Notification();
        notification.setFrom(from);
        notification.setBody(body + "\n" + messages);
        notification.setCountryId(countryId);
        notification.setSenderGender(senderGender);
        notification.setKeyRoom(keyRoom);
        sendNotificationInboxStyle(notification, getApplicationContext());*/
        //}

        editor.apply();

        int countMessages = 0;
        //LISTA DE TODAS LOS KEYS DE NOTIFICACIONES

        String keys[] = notificationsKeys.split("\\.");
        for (int i=0; i< keys.length; i++){
            if (!keys[i].isEmpty()) {
                //Create a Notification
                String extras[] = preferences.getString(keys[i] + "_" + Constants.PREF_NOTI_KEY_EXTRAS, "").split("-");
                String deviceFrom = extras[0];
                int country = Integer.parseInt(extras[1]);
                String gender = extras[2];
                String messages = preferences.getString(keys[i], "");


                Log.d(TAG, "keyRoom: " + keys[i]);
                Log.d(TAG, "messages no readed: " + messages);
                Log.d(TAG, "PREF_NOTI_KEY_EXTRAS: " + extras);
                Log.d(TAG, "deviceFrom: " + deviceFrom);
                Log.d(TAG, "country: " + country);
                Log.d(TAG, "gender: " + gender);

                countMessages += messages.split("\\n").length;
                Log.d(TAG, "countMessages("+i+  "): "+  countMessages);

                Notification n = new Notification(deviceFrom, "", messages, country, keys[i], gender, Constants.DELIVERED);
                sendNotificationInboxStyle(i, n ,getApplicationContext());
            }
        }
        editor.putInt(Constants.PREF_NOTI_COUNT, countMessages);
        editor.apply();
        //editor.putString(Constants.PREF_NOTI_MESSAGES, body +"\n"+ messages);
        //editor.putInt(Constants.PREF_NOTI_COUNT, countNotification);
        //editor.putInt(Constants.PREF_NOTI_COUNT_FROM, notificationFromCount);
        ShortcutBadger.applyCount(getApplicationContext(), countMessages);

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

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

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

    private void sendNotificationInboxStyle(int number, Notification n, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Country country = new Country(context, n.getCountryId());
        Bitmap profilePicture = BitmapFactory.decodeResource(
                context.getResources(),
                country.getDrawableID()
        );



        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String gender = "";
        switch (Constants.Genero.valueOf(n.getSenderGender())) {
            case CHICO:
                gender = context.getString(R.string.boy);
                break;
            case CHICA:
                gender = context.getString(R.string.girl);
                break;
        }
        String messages = n.getBody();
        //inboxStyle.addLine(messages);
        int count = 0;

        if (!messages.isEmpty()) {
            String[] lines = messages.split("\\n");
            for (int i = 0; i < lines.length; i++) {
                inboxStyle.addLine(lines[i]);
            }
            count = lines.length;
            inboxStyle.setSummaryText(String.format(Locale.getDefault(), getString(R.string.notification_count_messages), count));
        }
        inboxStyle.setBigContentTitle(gender);

        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("key_random", n.getKeyRoom());
        intent.putExtra("country_id", n.getCountryId());
        Log.d(TAG, "count extra_unread: " + count);
        intent.putExtra("extra_unread", count);
        intent.setAction(n.getKeyRoom());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.drawable.ic_whatshot_white_24dp);
        builder.setLargeIcon(profilePicture);
        builder.setSound(defaultSoundUri);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setContentText(n.getBody());
        builder.setStyle(inboxStyle);

        android.app.Notification notification = builder.build();
        NotificationManagerCompat.from(context).notify(MY_NOTIFICATION_ID + number, notification);
    }

    private void sendNotification(Context context, int authors, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap profilePicture = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.ic_whatshot_white_24dp
        );
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        builder.setSmallIcon(R.drawable.ic_whatshot_white_24dp);
        builder.setLargeIcon(profilePicture);
        builder.setSound(defaultSoundUri);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setContentText(body);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        //inboxStyle.addLine(body);

        if (!body.isEmpty()) {
            String[] lines = body.split("\\n");
            for (int i = 0; i < lines.length; i++) {
                inboxStyle.addLine(lines[i]);
            }
            inboxStyle.setSummaryText(String.format(Locale.getDefault(), getString(R.string.notification_summary_messages), authors, authors));
        }
        inboxStyle.setBigContentTitle(context.getString(R.string.app_name));
        builder.setStyle(inboxStyle);
        android.app.Notification notification = builder.build();
        NotificationManagerCompat.from(context).notify(MY_NOTIFICATION_ID, notification);
    }


}
