package apps.steve.fire.randomchat.firebase;

import android.text.TextUtils;
import android.util.Log;
import android.view.Display;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.interfaces.OnRoomListener;
import apps.steve.fire.randomchat.model.ChatMessage;
import apps.steve.fire.randomchat.model.Chater;
import apps.steve.fire.randomchat.model.Connection;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.model.Notification;
import apps.steve.fire.randomchat.model.RandomChat;

/**
 * Created by Steve on 24/08/2016.
 */

public class FirebaseRoom {

    private static final String TAG = FirebaseRoom.class.getSimpleName();

    private String key;

    private OnRoomListener listener;

    private FirebaseDatabase firebaseDatabase;

    //ROOM REFERENCE
    private DatabaseReference roomReference;


    private DatabaseReference messagesReference;
    private DatabaseReference userReference;

    private String androidIDReceptor;
    private DatabaseReference receptorReference;

    private DatabaseReference roomReceptorReference;
    private DatabaseReference roomEmisorReference;
    private DatabaseReference stateReference;
    private DatabaseReference actionReference;

    private String androidID;

    ValueEventListener messagesListener;
    ValueEventListener chatStateListener;
    //ValueEventListener randomReceptorListener;
    //ValueEventListener randomEmisorListener;

    ValueEventListener roomChatterListener;
    ValueEventListener roomReceptorListener;
    ValueEventListener stateListener;
    ValueEventListener actionListener;

    private Emisor me;
    private Emisor him;
    private String action;
    private String state;

    List<ChatMessage> messages = new ArrayList<ChatMessage>();

    private Connection himConnection;

    public FirebaseRoom(String key, String androidID, OnRoomListener listener) {
        this.key = key;
        this.androidID = androidID;
        this.listener = listener;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.roomReference = firebaseDatabase.getReference(Constants.CHILD_RANDOMS).child(key);

        this.messagesReference = roomReference.child(Constants.CHILD_MESSAGES);
        this.userReference = firebaseDatabase.getReference(Constants.CHILD_USERS);

        this.roomReceptorReference = roomReference.child("receptor");
        this.roomEmisorReference = roomReference.child("emisor");
        this.stateReference = roomReference.child("estado");
        this.actionReference = roomReference.child("action");
        //listenRandomReceptor();
        listenRoom();
    }

    private void listenRoom() {
        listenMessages();
        roomChatterListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "roomChatterListener dataSnapshot: " + dataSnapshot);
                if (dataSnapshot != null) {
                    Emisor temp = dataSnapshot.getValue(Emisor.class);

                    if (temp == null) {
                        return;
                    }

                    if (temp.getKeyDevice().equals(androidID)) {
                        me = temp;
                        listener.onMeReaded(me);
                    } else {
                        him = temp;
                        listenHimConnection();
                        listener.onHimReaded(him);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "roomChatterListener databaseError: " + databaseError);
            }
        };

        stateListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "stateListener dataSnapshot: " + dataSnapshot);
                if (dataSnapshot != null) {
                    state = dataSnapshot.getValue(String.class);
                    listener.onRoomStateChanged(state);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "stateListener databaseError: " + databaseError);
            }
        };
        actionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "actionListener dataSnapshot: " + dataSnapshot);
                if (dataSnapshot != null) {
                    action = dataSnapshot.getValue(String.class);
                    listener.onRoomActionChanged(action);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "stateListener databaseError: " + databaseError);
            }
        };

        roomEmisorReference.addListenerForSingleValueEvent(roomChatterListener);
        roomReceptorReference.addListenerForSingleValueEvent(roomChatterListener);
        stateReference.addValueEventListener(stateListener);
        actionReference.addValueEventListener(actionListener);
    }


    public void sendMessage(final ChatMessage message) {

        String keyMessage = messagesReference.push().getKey();
        String messagePath = "/" + Constants.CHILD_RANDOMS + "/" + key + "/" + Constants.CHILD_MESSAGES + "/" + keyMessage;

        String notificationPath = "/" + Constants.CHILD_NOTIFICATIONS + "/" + androidIDReceptor + "/" + androidID;

        Map<String, Object> messagePost = new HashMap<>();

        Map<String, Object> messageValues = message.toMap();

        messagePost.put(messagePath, messageValues);
        if (me != null && him != null) {

            String to = "/" + Constants.CHILD_USERS + "/" + me.getKeyDevice() + "/" + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;
            String toReceptor = "/" + Constants.CHILD_USERS + "/" + him.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;

            messagePost.put(to + "/lastMessage", messageValues);
            messagePost.put(toReceptor + "/lastMessage", messageValues);

            List<String> messageNoReaded = calculateMessagesNoReaded();

            //messagePost.put(to + "/noReaded", messageNoReaded.size());
            messagePost.put(toReceptor + "/noReaded", messageNoReaded.size());

            if (himConnection != null) {
                if (! himConnection.getState().equals(Constants.STATE_ONLINE)) {
                    String messages = getStringofArray(messageNoReaded);
                    messagePost.put(notificationPath, new Notification(messages, androidID, key, androidID, Constants.SENT).toMap());

                }
            }

        }


        firebaseDatabase.getReference().updateChildren(messagePost, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                listener.onMessagedSended(databaseError == null, message, databaseError == null ? null : databaseError.getMessage());
            }
        });
    }

    private List<String> calculateMessagesNoReaded() {
        List<String> messageNoReaded = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getMessageStatus() != Constants.READED && messages.get(i).getAndroidID().equals(androidID)) { //&& messages.get(i).getAndroidID().equals(androidID)
                messageNoReaded.add(messages.get(i).getMessageText());
            }
        }
        Log.d(TAG, "messageNoReaded.size(): " + messageNoReaded.size());
        return messageNoReaded;
    }

    public void changeAction(String action) {
        actionReference.setValue(action);
    }

    private String getStringofArray(List<String> list) {
        String messages = "";
        for (int i = 0; i < list.size(); i++) {
            messages += list.get(i);
            if (list.size() != (i - 1)) {
                messages += "\n";
            }
        }
        return messages;
    }

    /*
    public void createHistoryChat(String android_id, Chater me, Chater emisor) {


        String pathToHisto = "/" + android_id + "/" + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;
        String pathToHistoReceptor = "/" + emisor.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;

        if (!android_id.equals(me.getKeyDevice())) {
            pathToHisto = "/" + emisor.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;
            pathToHistoReceptor = "/" + android_id + "/" + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;
        }


        Map<String, Object> histo_user_me = new HashMap<>();

        histo_user_me.put(pathToHisto + "/keyChat", key);
        histo_user_me.put(pathToHisto + "/star", false);
        histo_user_me.put(pathToHisto + "/hot", false);
        histo_user_me.put(pathToHisto + "/like", false);
        histo_user_me.put(pathToHisto + "/noReaded", 0);
        histo_user_me.put(pathToHisto + "/lastMessage/messageText", "No messages.");
        histo_user_me.put(pathToHisto + "/lastMessage/androidID", android_id);
        histo_user_me.put(pathToHisto + "/lastMessage/messageStatus", Constants.SENT);
        histo_user_me.put(pathToHisto + "/lastMessage/messageType", Constants.MESSAGE_TEXT);
        histo_user_me.put(pathToHisto + "/lastMessage/messageTime", new Date().getTime());


        histo_user_me.put(pathToHisto + "/me/edad", me.getEdad());
        histo_user_me.put(pathToHisto + "/me/genero", me.getGenero());
        histo_user_me.put(pathToHisto + "/me/keyDevice", me.getKeyDevice());
        histo_user_me.put(pathToHisto + "/me/looking/edadMin", me.getLooking().getEdadMin());
        histo_user_me.put(pathToHisto + "/me/looking/edadMax", me.getLooking().getEdadMax());
        histo_user_me.put(pathToHisto + "/me/looking/genero", me.getLooking().getGenero());
        histo_user_me.put(pathToHisto + "/emisor/edad", emisor.getEdad());
        histo_user_me.put(pathToHisto + "/emisor/genero", emisor.getGenero());
        histo_user_me.put(pathToHisto + "/emisor/keyDevice", emisor.getKeyDevice());
        histo_user_me.put(pathToHisto + "/emisor/looking/edadMin", emisor.getLooking().getEdadMin());
        histo_user_me.put(pathToHisto + "/emisor/looking/edadMax", emisor.getLooking().getEdadMax());
        histo_user_me.put(pathToHisto + "/emisor/looking/genero", emisor.getLooking().getGenero());

        histo_user_me.put(pathToHistoReceptor + "/keyChat", key);
        histo_user_me.put(pathToHistoReceptor + "/star", false);
        histo_user_me.put(pathToHistoReceptor + "/hot", false);
        histo_user_me.put(pathToHistoReceptor + "/like", false);
        histo_user_me.put(pathToHistoReceptor + "/noReaded", 0);
        histo_user_me.put(pathToHistoReceptor + "/lastMessage/messageText", "No messages.");
        histo_user_me.put(pathToHistoReceptor + "/lastMessage/androidID", emisor.getKeyDevice());
        histo_user_me.put(pathToHistoReceptor + "/lastMessage/messageStatus", Constants.SENT);
        histo_user_me.put(pathToHistoReceptor + "/lastMessage/messageType", Constants.MESSAGE_TEXT);
        histo_user_me.put(pathToHistoReceptor + "/lastMessage/messageTime", new Date().getTime());

        histo_user_me.put(pathToHistoReceptor + "/me/edad", emisor.getEdad());
        histo_user_me.put(pathToHistoReceptor + "/me/genero", emisor.getGenero());
        histo_user_me.put(pathToHistoReceptor + "/me/keyDevice", emisor.getKeyDevice());
        histo_user_me.put(pathToHistoReceptor + "/me/looking/edadMin", emisor.getLooking().getEdadMin());
        histo_user_me.put(pathToHistoReceptor + "/me/looking/edadMax", emisor.getLooking().getEdadMax());
        histo_user_me.put(pathToHistoReceptor + "/me/looking/genero", emisor.getLooking().getGenero());
        histo_user_me.put(pathToHistoReceptor + "/emisor/edad", me.getEdad());
        histo_user_me.put(pathToHistoReceptor + "/emisor/genero", me.getGenero());
        histo_user_me.put(pathToHistoReceptor + "/emisor/keyDevice", me.getKeyDevice());
        histo_user_me.put(pathToHistoReceptor + "/emisor/looking/edadMin", me.getLooking().getEdadMin());
        histo_user_me.put(pathToHistoReceptor + "/emisor/looking/edadMax", me.getLooking().getEdadMax());
        histo_user_me.put(pathToHistoReceptor + "/emisor/looking/genero", me.getLooking().getGenero());

        firebaseDatabase.getReference(Constants.CHILD_USERS).updateChildren(histo_user_me, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //listener.onChatHistoryCreated(databaseError == null, databaseError != null ? databaseError.getMessage() : null);
            }
        });
    }*/

    private void listenMessages() {
        messagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "There are " + dataSnapshot.getChildrenCount() + " messages");
                messages.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ChatMessage message = postSnapshot.getValue(ChatMessage.class);
                    message.setKeyMessage(postSnapshot.getKey());
                    Log.d(Constants.TAG, "MESSAGE: " + message.getMessageText() + " - " + message.getAndroidID());
                    //SI LO ESTÁ LEYENDO DEL SERVER, ESO QUIERO DECIR, QUE YA ESTÁ EN EL SERVER, SIEMPRE.
                    //message.setMessageStatus(Constants.DELIVERED);
                    messages.add(message);
                }
                listener.onReadMessages(messages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "listenMessages databaseError: " + databaseError.getMessage());
            }
        };
        messagesReference.limitToLast(10).addValueEventListener(messagesListener);
    }

    public void removeMessageListener() {
        messagesReference.removeEventListener(messagesListener);
    }


    public void setReaded(List<ChatMessage> messages) {
        if ( me == null || him == null){
            return;
        }

        messages = getMessageNoReaded(messages);
        if (messages.size() > 0) {

            String messagePath = "/" + Constants.CHILD_RANDOMS + "/" + key + "/" + Constants.CHILD_MESSAGES;//UPDATE MESSAGES STATUS
            String to = "/" + Constants.CHILD_USERS + "/" + me.getKeyDevice() + "/" + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key; //UPDATE COUNT
            String toReceptor = "/" + Constants.CHILD_USERS + "/" + him.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;
            String notificationPath = "/" + Constants.CHILD_NOTIFICATIONS + "/" + me.getKeyDevice() + "/" + him.getKeyDevice();

            Map<String, Object> messageReaded = new HashMap<>();
            for (ChatMessage message : messages) {
                messageReaded.put(messagePath + "/" + message.getKeyMessage(), message.toMap());
            }
            messageReaded.put(to + "/noReaded", 0);
            messageReaded.put(to + "/lastMessage/messageStatus", Constants.READED);
            messageReaded.put(to + "/lastMessage/messageTime", new Date().getTime());

            messageReaded.put(toReceptor + "/lastMessage/messageStatus", Constants.READED);
            messageReaded.put(toReceptor + "/lastMessage/messageTime", new Date().getTime());


            messageReaded.put(notificationPath + "/status", Constants.READED);

            firebaseDatabase.getReference().updateChildren(messageReaded);

        }
    }

    private List<ChatMessage> getMessageNoReaded(List<ChatMessage> messages) {
        List<ChatMessage> messageNoReaded = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            ChatMessage message = messages.get(i);
            if (message.getMessageStatus() != Constants.READED && message.getAndroidID().equals(him.getKeyDevice())) {
                message.setMessageStatus(Constants.READED);
                messageNoReaded.add(message);
            }
        }
        return messageNoReaded;
    }

    private void listenHimConnection() {
        Log.d(TAG, "listenHimConnection");
        if (him == null) {
            return;
        }
        this.receptorReference = firebaseDatabase.getReference(Constants.CHILD_USERS).child(him.getKeyDevice()).child(Constants.CHILD_CONNECTION);
        receptorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    Log.d(TAG, "listenReceptorConnection dataSnapshot: " + dataSnapshot);
                    himConnection = dataSnapshot.getValue(Connection.class);
                    listener.onReceptorConnectionChanged(himConnection.getState(), himConnection.getLastConnection());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "listenReceptorConnection databaseError: " + databaseError.getMessage());
            }
        });
    }



    /*
    private void listenRandomReceptor(){
        randomReceptorListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Log.d(TAG, "randomReceptorReference dataSnapshot: " + dataSnapshot);
                    //Receptor receptor = dataSnapshot.getValue(Receptor.class);
                    Emisor receptor = dataSnapshot.getValue(Emisor.class);
                    if (!TextUtils.isEmpty(receptor.getKeyDevice()) && !receptor.getKeyDevice().equals(androidID)){
                        androidIDReceptor = receptor.getKeyDevice();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        randomReceptorReference.addValueEventListener(randomReceptorListener);

    }
    private void listenRandomEmisor(){
        randomEmisorListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Log.d(TAG, "randomEmisorReference dataSnapshot: " + dataSnapshot);
                    Emisor emisor = dataSnapshot.getValue(Emisor.class);
                    if (!TextUtils.isEmpty(emisor.getKeyDevice()) && !emisor.getKeyDevice().equals(androidID)){
                        androidIDReceptor = emisor.getKeyDevice();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        randomEmisorReference.addValueEventListener(randomEmisorListener);
    }*/

}
