package apps.steve.fire.randomchat.firebase;

import android.text.TextUtils;
import android.util.Log;
import android.view.Display;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

/**
 * Created by Steve on 24/08/2016.
 */

public class FirebaseRoom {

    private static final String TAG = FirebaseRoom.class.getSimpleName();
    private String key;
    private OnRoomListener listener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference roomReference;
    private DatabaseReference messagesReference;
    private DatabaseReference userReference;
    private DatabaseReference chatStateReference;

    private String androidIDReceptor;
    private DatabaseReference receptorReference;

    private DatabaseReference randomReceptorReference;
    private DatabaseReference randomEmisorReference;

    private String androidID;

    ValueEventListener messagesListener;
    ValueEventListener chatStateListener;
    ValueEventListener randomReceptorListener;
    ValueEventListener randomEmisorListener;

    public FirebaseRoom(String key, String androidID, OnRoomListener listener) {
        this.key = key;
        this.androidID = androidID;
        this.listener = listener;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.roomReference = firebaseDatabase.getReference(Constants.CHILD_RANDOMS).child(key);
        this.messagesReference = roomReference.child(Constants.CHILD_MESSAGES);
        this.userReference = firebaseDatabase.getReference(Constants.CHILD_USERS);
        this.chatStateReference = roomReference.child(Constants.CHILD_STATE);
        this.randomReceptorReference = roomReference.child("receptor");
        this.randomEmisorReference = roomReference.child("emisor");
        listenMessages();
        listenChatState();
        listenRandomReceptor();
    }

    public void sendMessage(String androidID, String androidIDReceptor, final ChatMessage message, boolean isReceptorOnline, List<String> messageNoReaded) {

        this.androidID = androidID;

        String keyMessage = messagesReference.push().getKey();
        String messagePath = "/" + Constants.CHILD_RANDOMS + "/" + key + "/" + Constants.CHILD_MESSAGES + "/" + keyMessage;
        String to = "/" + Constants.CHILD_USERS + "/" + androidID + "/" + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;
        String toReceptor = "/" + Constants.CHILD_USERS + "/" + androidIDReceptor + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;

        String notificationPath = "/" + Constants.CHILD_NOTIFICATIONS + "/" + androidIDReceptor + "/" + androidID;

        Map<String, Object> messagePost = new HashMap<>();

        Map<String, Object> messageValues = message.toMap();

        messagePost.put(messagePath, messageValues);
        if (!TextUtils.isEmpty(androidID) && !TextUtils.isEmpty(androidIDReceptor)) {
            messagePost.put(to + "/lastMessage", messageValues);
            messagePost.put(toReceptor + "/lastMessage", messageValues);

            //messagePost.put(to + "/noReaded", messageNoReaded.size());
            messagePost.put(toReceptor + "/noReaded", messageNoReaded.size());

            if (!isReceptorOnline) {
                String messages = getStringofArray(messageNoReaded);
                messagePost.put(notificationPath, new Notification(messages, androidID, key, androidID, Constants.SENT).toMap());

            }
        }


        firebaseDatabase.getReference().updateChildren(messagePost, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                listener.onMessagedSended(databaseError == null, message, databaseError == null ? null : databaseError.getMessage());
            }
        });

        /*
        messagesReference.push().setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                listener.onMessagedSended(databaseError == null, message ,databaseError == null ? null : databaseError.getMessage());
            }
        });*/
    }

    public void changeState(String state) {
        chatStateReference.setValue(state);
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
                listener.onChatHistoryCreated(databaseError == null, databaseError != null ? databaseError.getMessage() : null);
            }
        });
    }

    public void listenMessages() {

        messagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<ChatMessage> messages = new ArrayList<ChatMessage>();
                Log.d(TAG, "There are " + dataSnapshot.getChildrenCount() + " messages");
                //chatMessages.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ChatMessage message = postSnapshot.getValue(ChatMessage.class);
                    message.setKeyMessage(postSnapshot.getKey());
                    Log.d(Constants.TAG, message.getMessageText() + " - " + message.getAndroidID());
                    //SI LO ESTÁ LEYENDO DEL SERVER, ESO QUIERO DECIR, QUE YA ESTÁ EN EL SERVER, SIEMPRE.
                    //message.setMessageStatus(Constants.DELIVERED);
                    messages.add(message);
                }
                listener.onReadMessages(messages);


                //if(chatAdapter!=null)
                //    chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "listenMessages databaseError: " + databaseError.getMessage());
                //Toast.makeText(getActivity(), "THE READ FAILED : " + databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        };
        messagesReference.limitToLast(10).addValueEventListener(messagesListener);
    }

    public void removeMessageListener() {
        messagesReference.removeEventListener(messagesListener);
    }

    private void listenConnection() {
        /*
        userReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HistorialChat chat = dataSnapshot.getValue(HistorialChat.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    public void setReaded(List<ChatMessage> messages) {
        messages = getMessageNoReaded(messages);
        if (messages.size() > 0) {

            String messagePath = "/" + Constants.CHILD_RANDOMS + "/" + key + "/" + Constants.CHILD_MESSAGES;//UPDATE MESSAGES STATUS
            String to = "/" + Constants.CHILD_USERS + "/" + androidID + "/" + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key; //UPDATE COUNT
            String toReceptor = "/" + Constants.CHILD_USERS + "/" + androidIDReceptor + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;
            String notificationPath = "/" + Constants.CHILD_NOTIFICATIONS + "/" + androidID + "/" + androidIDReceptor;

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
            if (message.getMessageStatus() != Constants.READED && message.getAndroidID().equals(androidIDReceptor)) {
                message.setMessageStatus(Constants.READED);
                messageNoReaded.add(message);
            }
        }

        return messageNoReaded;
    }

    public void listenReceptorConnection(String androidIDReceptor) {
        this.androidIDReceptor = androidIDReceptor;
        this.receptorReference = firebaseDatabase.getReference(Constants.CHILD_USERS).child(androidIDReceptor).child(Constants.CHILD_CONNECTION);
        receptorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    Log.d(TAG, "listenReceptorConnection dataSnapshot: " + dataSnapshot);
                    Connection connection = dataSnapshot.getValue(Connection.class);
                    listener.onReceptorConnectionChanged(connection.getState(), connection.getLastConnection());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "listenReceptorConnection databaseError: " + databaseError.getMessage());
            }
        });
    }

    private void listenChatState() {
        chatStateListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Log.d(TAG, "listenChatState dataSnapshot: " + dataSnapshot);
                    String estado = dataSnapshot.getValue(String.class);
                    listener.onChatStateChanged(estado);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "listenChatState databaseError: " + databaseError.getMessage());
            }
        };
        chatStateReference.addValueEventListener(chatStateListener);
    }

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
    }

}
