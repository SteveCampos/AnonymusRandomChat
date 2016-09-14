package apps.steve.fire.randomchat.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;


import java.util.ArrayList;
import java.util.List;

import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.activities.MainActivity;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Steve on 8/07/2016.
 */

public class NotificationFireListener extends Service {

    public static final String TAG = NotificationFireListener.class.getSimpleName();
    private BubblesManager bubblesManager;
    private int NOTIFICATION_ID = 465023;
    //DATABASE REFERENCES
    DatabaseReference refRandoms;
    FirebaseDatabase database;

    private List<apps.steve.fire.randomchat.model.Notification> notifications = new ArrayList<>();
    private int count;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        database = FirebaseDatabase.getInstance();
        String androidID = Utils.getAndroidID(getApplicationContext());
        refRandoms = database.getReference(Constants.CHILD_NOTIFICATIONS).child(androidID);
        initializeBubblesManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        // FIRE DATABASE INSTANCE

        ChildEventListener handler;
        handler = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded dataSnapshot: " + dataSnapshot.toString() + ", s: " + s);

                apps.steve.fire.randomchat.model.Notification n = dataSnapshot.getValue(apps.steve.fire.randomchat.model.Notification.class);

                Log.d(TAG, "Notification Sender : " + n.getSender());
                Log.d(TAG, "Notification Body : " + n.getBody());
                notifications.clear();
                notifications.add(n);
                notifyDataSetChanged();

                //manageNotification(n);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded dataSnapshot: " + dataSnapshot.toString() + ", s: " + s);

                apps.steve.fire.randomchat.model.Notification n = dataSnapshot.getValue(apps.steve.fire.randomchat.model.Notification.class);
                Log.d(TAG, "Notification Sender : " + n.getSender());
                Log.d(TAG, "Notification Body : " + n.getBody());
                notifications.clear();
                notifications.add(n);
                notifyDataSetChanged();

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        refRandoms.addChildEventListener(handler);

        return Service.START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    private void notifyDataSetChanged() {
        Log.d(TAG, "notifyDataSetChanged");
        int noReaded = 0;
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getStatus() != Constants.READED) {
                sendNotificationInboxStyle(notifications.get(i), i);
                //addNewBubble(notifications.get(i));
                noReaded += countMessages(notifications.get(i).getBody());
            }
        }
        Log.d(TAG, "noReaded: " + noReaded);
        ShortcutBadger.applyCount(getApplicationContext(), noReaded);

    }

    /*
    private void manageNotification(apps.steve.fire.randomchat.model.Notification n) {
        addNewBubble(n);
        sendNotificationInboxStyle(n, 1);

        Utils.saveNoReaded(getApplicationContext(), countMessages(n.getBody()));

        ShortcutBadger.applyCount(getApplicationContext(), Utils.getNoReaded(getApplicationContext())); //for 1.1.4+
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }


    private void sendNotificationInboxStyle(apps.steve.fire.randomchat.model.Notification n, int i) {
        Context context = getApplicationContext();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.naturesvg);
        builder.setContentTitle(n.getSender());
        builder.setContentText(n.getBody());

        Bitmap profilePicture = BitmapFactory.decodeResource(
                context.getResources(),
                Utils.getDrawable(n.getSenderGender())
        );
        builder.setLargeIcon(profilePicture);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(n.getSender());
        String messages = n.getBody();

        if (!messages.isEmpty()) {
            String[] lines = messages.split("\\n");
            for (String line : lines) {
                inboxStyle.addLine(line);
            }
            inboxStyle.setSummaryText(lines.length + " nuevos mensajes.");
            //ShortcutBadger.applyCount(getApplicationContext(), lines.length); //for 1.1.4+
        }


        builder.setStyle(inboxStyle);


        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("current_item", 1);//1: LIST OF CHATS

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);

        Notification notification = builder.build();
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID + i, notification);

    }

    private int countMessages(String messages) {
        int count = 0;
        String[] lines = messages.split("\\n");
        for (String line : lines) {
            count++;
        }
        return count;
    }

    private void addNewBubble(apps.steve.fire.randomchat.model.Notification notification) {
        count++;
        Context context = getApplicationContext();

        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.bubble_layout, null);

        TextView bubbleCounter = (TextView) bubbleView.findViewById(R.id.bubble_counter);
        ImageView bubbleAvatar = (ImageView) bubbleView.findViewById(R.id.bubble_avatar);

        Glide.with(getApplicationContext()).
                load(Utils.getDrawable(notification.getSenderGender()))
                .placeholder(Utils.getDrawable(notification.getSenderGender()))
                .into(bubbleAvatar);

        bubbleCounter.setText("" + countMessages(notification.getBody()));

        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
            }
        });
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Toast.makeText(getApplicationContext(), "Bubble Clicke!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        bubbleView.setShouldStickToWall(true);
        Log.d(TAG, "COUNT BUBBLES ADDEDS: " + count);
        int y = 20 + 10 * count;
        Log.d(TAG, "Y : " + y);
        bubblesManager.addBubble(bubbleView, 8, y);
    }


    private void initializeBubblesManager() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_trash_layout)
                .build();
        bubblesManager.initialize();
    }


}
