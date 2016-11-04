package apps.steve.fire.randomchat.firebase;

import android.content.Context;
import android.util.Log;

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
import apps.steve.fire.randomchat.model.Connection;
import apps.steve.fire.randomchat.model.Country;
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

    //ROOM REFERENCE
    private DatabaseReference roomReference;


    private DatabaseReference messagesReference;
    private DatabaseReference userReference;


    private DatabaseReference receptorReference;

    private DatabaseReference roomReceptorReference;
    private DatabaseReference roomEmisorReference;
    private DatabaseReference stateReference;
    private DatabaseReference actionReference;


    private DatabaseReference userHistoRoomRef;
    private DatabaseReference userHistoRoomHotRef;
    private String androidID;

    private ValueEventListener messagesListener;

    private ValueEventListener roomChatterListener;
    private ValueEventListener stateListener;
    private ValueEventListener actionListener;

    private ValueEventListener userRoomHotListener;


    private Emisor me;
    private Emisor him;
    private String action;
    private String state;

    private List<ChatMessage> messages = new ArrayList<ChatMessage>();

    private Connection himConnection;

    private boolean hot = false;

    private Country country;


    public FirebaseRoom(int countryId, String key, String androidID, Context context) {

        this.key = key;
        this.androidID = androidID;
        this.listener = (OnRoomListener) context;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        //this.roomReference = firebaseDatabase.getReference(Constants.CHILD_RANDOMS).child(key);
        country = new Country(context, countryId);

        this.roomReference = firebaseDatabase.getReference(Constants.CHILD_COUNTRIES).child(country.getNameID()).child(Constants.CHILD_RANDOMS).child(Constants.CHAT_STATE_PARED).child(key);

        this.messagesReference = roomReference.child(Constants.CHILD_MESSAGES);
        this.userReference = firebaseDatabase.getReference(Constants.CHILD_USERS);

        this.roomReceptorReference = roomReference.child("receptor");
        this.roomEmisorReference = roomReference.child("emisor");
        this.stateReference = roomReference.child("estado");
        this.actionReference = roomReference.child("action");

        this.userHistoRoomRef = userReference.child(androidID).child(Constants.CHILD_USERS_HISTO_CHATS).child(key);
        this.userHistoRoomHotRef = userHistoRoomRef.child("hot");


        roomReference.keepSynced(true);
        messagesReference.keepSynced(true);


        //listenRandomReceptor();
        listenRoom();
        listenHot();
    }

    private void listenRoom() {
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

        roomEmisorReference.addValueEventListener(roomChatterListener);
        roomReceptorReference.addValueEventListener(roomChatterListener);
        stateReference.addValueEventListener(stateListener);
        actionReference.addValueEventListener(actionListener);
        listenMessages();
    }


    public void sendMessage(final ChatMessage message) {

        String keyMessage = messagesReference.push().getKey();
        //String messagePath = "/" + Constants.CHILD_RANDOMS + "/" + key + "/" + Constants.CHILD_MESSAGES + "/" + keyMessage;
        String messagePath = "/" + Constants.CHILD_COUNTRIES + "/" + country.getNameID() + "/" +  Constants.CHILD_RANDOMS + "/"+ Constants.CHAT_STATE_PARED + "/" + key + "/" + Constants.CHILD_MESSAGES + "/" + keyMessage;


        Map<String, Object> messagePost = new HashMap<>();

        Map<String, Object> messageValues = message.toMap();

        messagePost.put(messagePath, messageValues);


        if (me != null) {

            String to = "/" + Constants.CHILD_USERS + "/" + me.getKeyDevice() + "/" + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;
            messagePost.put(to + "/lastMessage", messageValues);


            if (him != null) {

                String toReceptor = "/" + Constants.CHILD_USERS + "/" + him.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;
                String notificationPath = "/" + Constants.CHILD_NOTIFICATIONS + "/" + keyMessage;

                messagePost.put(toReceptor + "/lastMessage", messageValues);
                List<String> messageNoReaded = calculateMessagesNoReaded();

                //messagePost.put(to + "/noReaded", messageNoReaded.size());
                messagePost.put(toReceptor + "/noReaded", messageNoReaded.size());

                if (himConnection != null) {
                    if (!himConnection.getState().equals(Constants.STATE_ONLINE)) {
                        String messages = getStringofArray(messageNoReaded);
                        messagePost.put(notificationPath, new Notification(me.getKeyDevice(), him.getKeyDevice(), message.getMessageText(), country.getCountryID(), key, me.getGenero(), Constants.SENT).toMap());

                    }
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

    public void block(){

        Map<String, Object> blockChat = new HashMap<>();

        //String roomState = "/" + Constants.CHILD_RANDOMS + "/" + key + "/" + Constants.CHILD_STATE + "/";
        String roomState = "/" + Constants.CHILD_COUNTRIES + "/" + country.getNameID() + "/" +  Constants.CHILD_RANDOMS + "/"+ Constants.CHAT_STATE_PARED + "/" + key + "/" + Constants.CHILD_STATE + "/";
        blockChat.put(roomState, Constants.CHAT_STATE_BLOCK);

        if (me != null){
            String meRoomState = "/" + Constants.CHILD_USERS + "/" + me.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key + "/" + Constants.CHILD_STATE + "/";
            blockChat.put(meRoomState, Constants.CHAT_STATE_BLOCK);

            if (him != null){
                String himRoomState = "/" + Constants.CHILD_USERS + "/" + him.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key + "/" + Constants.CHILD_STATE + "/";;
                blockChat.put(himRoomState, Constants.CHAT_STATE_BLOCK);
            }
        }

        firebaseDatabase.getReference().updateChildren(blockChat, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d(TAG, databaseError == null ? "block": "block databaseError: "+ databaseError);
            }
        });
    }

    public void changeName(String nameChat){
        if (me != null){
            Map<String, Object> roomName = new HashMap<>();
            String meRoomHimName = "/" + Constants.CHILD_USERS + "/" + me.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key + "/" + "himName" + "/";
            roomName.put(meRoomHimName, nameChat);

            firebaseDatabase.getReference().updateChildren(roomName, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Log.d(TAG, databaseError == null ? "setNameChat true": "setNameChat databaseError: "+ databaseError);
                }
            });
        }
    }


    public void hot() {

        Log.d(TAG, "isHot: " + hot);

        Map<String, Object> hotMap = new HashMap<>();
        hotMap.put("hot", !hot);

        userHistoRoomRef.updateChildren(hotMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null){
                    listener.onRoomHot(!hot);
                }
            }
        });
    }

    private void listenHot() {
        userRoomHotListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "userRoomHotListener dataSnapshot: " + dataSnapshot);
                if (dataSnapshot!=null){
                    hot = dataSnapshot.getValue(Boolean.class);
                    listener.onRoomHot(hot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "userRoomHotListener databaseError: " + databaseError);
            }
        };
        userHistoRoomHotRef.addValueEventListener(userRoomHotListener);

    }

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
        messagesReference.limitToLast(20).addValueEventListener(messagesListener);
    }

    public void removeMessageListener() {
        messagesReference.removeEventListener(messagesListener);
    }


    public void setReaded(List<ChatMessage> messages) {
        if (me == null || him == null || country==null) {
            Log.d(TAG, "me == null || him == null || country==null");
            return;
        }

        messages = getMessageNoReaded(messages);
        if (messages.size() > 0) {

            //String messagePath = "/" + Constants.CHILD_RANDOMS + "/" + key + "/" + Constants.CHILD_MESSAGES;//UPDATE MESSAGES STATUS
            String messagePath = "/" + Constants.CHILD_COUNTRIES + "/" + country.getNameID() + "/" +  Constants.CHILD_RANDOMS + "/"+ Constants.CHAT_STATE_PARED + "/" + key + "/" + Constants.CHILD_MESSAGES ;
            String to = "/" + Constants.CHILD_USERS + "/" + me.getKeyDevice() + "/" + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key; //UPDATE COUNT
            String toReceptor = "/" + Constants.CHILD_USERS + "/" + him.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + key;
            //String notificationPath = "/" + Constants.CHILD_NOTIFICATIONS + "/" + me.getKeyDevice() + "/" + him.getKeyDevice();

            Map<String, Object> messageReaded = new HashMap<>();
            for (ChatMessage message : messages) {
                messageReaded.put(messagePath + "/" + message.getKeyMessage(), message.toMap());
            }
            messageReaded.put(to + "/noReaded", 0);
            messageReaded.put(to + "/lastMessage/messageStatus", Constants.READED);
            messageReaded.put(to + "/lastMessage/messageTime", new Date().getTime());

            messageReaded.put(toReceptor + "/lastMessage/messageStatus", Constants.READED);
            messageReaded.put(toReceptor + "/lastMessage/messageTime", new Date().getTime());

//            messageReaded.put(notificationPath + "/status", Constants.READED);

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
                    if (himConnection!=null){
                        listener.onReceptorConnectionChanged(himConnection.getState(), himConnection.getLastConnection());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "listenReceptorConnection databaseError: " + databaseError.getMessage());
            }
        });
    }

}
