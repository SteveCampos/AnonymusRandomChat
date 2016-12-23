package apps.steve.fire.randomchat.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

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
import apps.steve.fire.randomchat.R;
import apps.steve.fire.randomchat.Utils;
import apps.steve.fire.randomchat.interfaces.OnChatsListener;
import apps.steve.fire.randomchat.interfaces.OnPostListener;
import apps.steve.fire.randomchat.interfaces.OnSearchListener;
import apps.steve.fire.randomchat.model.ChatMessage;
import apps.steve.fire.randomchat.model.Connection;
import apps.steve.fire.randomchat.model.Country;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.model.Notification;
import apps.steve.fire.randomchat.model.RandomChat;
import apps.steve.fire.randomchat.model.User;

import static apps.steve.fire.randomchat.Utils.isBetween;

/**
 * Created by Steve on 22/08/2016.
 */

public class FirebaseHelper {
    private static final String TAG = FirebaseHelper.class.getSimpleName();
    private FirebaseDatabase firebaseDatabase;


    private DatabaseReference refRandoms;
    private DatabaseReference refWaiting;
    private DatabaseReference refUsers;
    private DatabaseReference refUserChats;
    private DatabaseReference refCountries;
    private DatabaseReference refPosts;
    private OnSearchListener listener;
    private OnChatsListener listenerChats;
    private OnPostListener listenerPosts;

    private String androidID;

    //Listeners
    private ValueEventListener nodeRandoms;
    private ValueEventListener chatsListener;
    private ValueEventListener chatsHotListener;
    private ValueEventListener postsListener;

    private Context context;
    private Query queryChats;
    private Query queryChatsHot;
    private Query queryPosts;

    private Country country;

    private int limit = 20;
    private int limitHots = 20;
    private int limitPost = 20;

    //private static boolean isPersisted = false;

    public FirebaseHelper(Context context) {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.refCountries = firebaseDatabase.getReference(Constants.CHILD_COUNTRIES);
        this.refUsers = firebaseDatabase.getReference(Constants.CHILD_USERS);
        this.context = context;

        //PERSISTENCE ENABLED
        refUsers.keepSynced(false);

    }


    public void incrementLimit(int limit) {
        this.limit += limit;
        listenChats();
    }

    public void incrementHotsLimit(int limit){
        this.limitHots += limit;
        listenChatsHot();
    }

    public void incrementPostLimit(int limit){
        this.limitPost += limit;
        listenPosts();
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
        this.refRandoms = refCountries.child(country.getNameID()).child(Constants.CHILD_RANDOMS);
        this.refPosts = refCountries.child(country.getNameID()).child(Constants.CHILD_POSTS);
        this.refWaiting = refRandoms.child(Constants.CHAT_STATE_WAITING);
        //refRandoms.keepSynced(false);
        refWaiting.keepSynced(true);
        listenPosts();
    }

    public void createUser(String id, User user) {

        Log.d(TAG, "createUser ...");
        refUsers.child(id).updateChildren(user.toMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    //ON SUCCESS
                    Log.d(TAG, "createUser SUCCESS");
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstTime", true);
                    editor.apply();

                } else {
                    Log.d(TAG, "createUser databaseError: " + databaseError);
                }
            }
        });
        /*
        refUsers.child(id).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    //ON SUCCESS
                    Log.d(TAG, "createUser SUCCESS");
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstTime", true);
                    editor.apply();

                } else {
                    Log.d(TAG, "createUser databaseError: " + databaseError);
                }
            }
        });*/
    }



    //SEARCH CHAT

    public void startChat(OnSearchListener listener) {
        this.listener = listener;
        if (country==null){
            listener.onNotCountrySelected();
            return;
        }
        Emisor me = Utils.getEmisor(context);
        readNodeRandoms(me);
    }

    public void removeListenerRead(){
        //refRandoms.removeEventListener(nodeRandoms);
    }

    private void readNodeRandoms(final Emisor me) {

        Log.d(TAG, "readNodeRandoms...");
        //Query queryRandoms = refRandoms.orderByChild("estado").equalTo(RandomChat.WAITING);

        //Query queryRandoms = refRandoms.orderByChild("estado").equalTo(RandomChat.WAITING);

        Query queryWaiting = refWaiting.orderByChild("search").equalTo(me.getLooking().getGenero()+"_LOOKING_"+me.getGenero());

        //YO SOY EL RECEPTOR
        /*
        receptorPared = new Receptor();
        receptorPared.setEdad(emisorEdad);
        receptorPared.setGenero(genero.name());
        receptorPared.setKeyDevice(android_id);
        receptorPared.setLooking(new Looking(receptorEdadMin, receptorEdadMax, generoReceptor.name()));*/

        nodeRandoms = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String key = "";
                Log.d(TAG, "There are " + dataSnapshot.getChildrenCount() + " RandomChats");
                Log.d(TAG, "SNAPTSHOT : " + dataSnapshot.getValue());
                boolean encontrado = false;
                Emisor emisor = null;

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "GETKEY() : " + postSnapshot.getKey());

                        RandomChat chatRandom = postSnapshot.getValue(RandomChat.class);

                        Log.d(TAG, "EMISOR KEYDEVICE : " + chatRandom.getEmisor().getKeyDevice());
                        Log.d(TAG, "EMISOR GENERO: " + chatRandom.getEmisor().getGenero());
                        Log.d(TAG, "EMISOR  EDAD : " + chatRandom.getEmisor().getEdad());
                        Log.d(TAG, "EMISOR LOOKING GENERO : " + chatRandom.getEmisor().getLooking().getGenero());
                        Log.d(TAG, "EMISOR LOOKING EDAD MIN : " + chatRandom.getEmisor().getLooking().getEdadMin());
                        Log.d(TAG, "EMISOR LOOKING EDAD MAX : " + chatRandom.getEmisor().getLooking().getEdadMax());
                        Log.d(TAG, "CHAT ESTADO : " + chatRandom.getEstado());
                        Log.d(TAG, "CHAT TIME CREATION: " + chatRandom.getTime());
                        //Log.d(TAG, "USERS NUMBER : " + chatRandom.getUserNumber());


                        //COMPARAR SI EL ANDROID ID DEVICE, NO ES EL MISMO QUE EL ID DEVICE DE ESTE DISPOSITIVO.
                        if (!chatRandom.getEmisor().getKeyDevice().equals(me.getKeyDevice()) && chatRandom.getEmisor().getLooking().getGenero().equals(me.getGenero())) {
                            Log.d(TAG, "SOY EL SEXO QUE ESTÁ BUSCANDO : " + true);
                            if (isBetween(me.getEdad(), chatRandom.getEmisor().getLooking().getEdadMin(), chatRandom.getEmisor().getLooking().getEdadMax())) {
                                Log.d(TAG, "ESTOY ENTRE LA EDAD QUE BUSCA : " + true);
                                Log.d(TAG, "DEFINITIVAMENTE, SOY LO QUE BUSCA : " + true);

                                Log.d(TAG, "AHORA VAMOS A VER SI ES LO QUE BUSCO : XD");

                                if (me.getLooking().getGenero().equals(chatRandom.getEmisor().getGenero())) {
                                    Log.d(TAG, "ES EL GÉNERO QUE BUSCO : " + true);

                                    if (isBetween(chatRandom.getEmisor().getEdad(), me.getLooking().getEdadMin(), me.getLooking().getEdadMax())) {
                                        Log.d(TAG, "ES LA EDAD QUE BUSCO PUTITOS  : " + true);
                                        Log.d(TAG, "AQUÍ HAY UN MATCH PERRAS");
                                        encontrado = true;
                                        emisor = chatRandom.getEmisor();
                                        key = postSnapshot.getKey();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                //AQUÍ CREAR UN NUEVO CHAT SI NO SE ENCONTRÓ
                //O VALIDAR SI EL CHAT SIGUE ESPERANDO XDDDD.
                if (!encontrado) {
                    createNewChat(
                            new Emisor(
                                    me.getKeyDevice(),
                                    me.getEdad(),
                                    me.getGenero(),
                                    me.getLooking()
                            )
                    );
                } else {
                    paredChat(key, me, emisor);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "DatabaseError: " + databaseError.getMessage());
                //enableInputs();
                //Snackbar.make(fab, "ERROR: "+ databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        };

        queryWaiting.addListenerForSingleValueEvent(nodeRandoms);
    }

    public void createNewPost(final Emisor me, ChatMessage post, OnPostListener listenerP){
        this.listenerPosts = listenerP;
        Log.d(TAG, "createNewPost ...");
        String keyChat = "";
        String keyMessagePost = "";

        keyChat = refPosts.push().getKey();
        keyMessagePost = refPosts.child(keyChat).child("messages").push().getKey();

        post.setKeyMessage(keyMessagePost);

        RandomChat chatRandom = new RandomChat();
        Emisor receptor = new Emisor();

        chatRandom.setEmisor(me);
        chatRandom.setReceptor(receptor);
        chatRandom.setEstado(RandomChat.WAITING);
        chatRandom.setSearch(me.getGenero()+"_LOOKING_"+me.getLooking().getGenero());
        chatRandom.setAction(Constants.CHAT_STATE_NO_ACTION);
        chatRandom.setTime(-new Date().getTime());

        chatRandom.setKeyChat(keyChat);
        chatRandom.setNoReaded(0);
        chatRandom.setHot(false);
        chatRandom.setLastMessage(post);
        chatRandom.setCountry(country);


        Log.d(TAG, "KEY CHAT : " + keyChat);

        //String pathToHisto = "/" + Constants.CHILD_USERS + "/" + me.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + keyChat;
        //String pathToPared = "/" + Constants.CHILD_COUNTRIES + "/" + country.getNameID() + "/" +  Constants.CHILD_RANDOMS + "/"+ Constants.CHAT_STATE_PARED + "/" + keyChat;
        //String pathToWaiting = "/" + Constants.CHILD_COUNTRIES + "/" + country.getNameID() + "/" +  Constants.CHILD_RANDOMS + "/"+ Constants.CHAT_STATE_WAITING + "/" + keyChat;
        String pathToPost = "/" + Constants.CHILD_COUNTRIES + "/" + country.getNameID() + "/" +  Constants.CHILD_POSTS  + "/" + keyChat;


        Map<String, Object> chat = new HashMap<>();

        //chat.put(pathToWaiting, chatRandom.toMap());
        chat.put(pathToPost, chatRandom.toHistoMap());
        //chat.put(pathToPared, chatRandom.toHistoMap());
        //chat.put(pathToHisto, chatRandom.toHistoMap());


        final String finalKeyChat = keyChat;
        firebaseDatabase.getReference().updateChildren(chat, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.d(TAG, "createNewChat databaseError: " + databaseError.getMessage());
                    listenerPosts.onFailed(databaseError.getMessage());
                } else {
                    Log.d(TAG, "createNewChat SUCCESS: " + true);
                    listenerPosts.onPostCreated();
                }
            }
        });
    }

    public void paredByPost(RandomChat randomChat, Emisor me, OnPostListener listener){
        this.listenerPosts = listener;

        ChatMessage postMessage = randomChat.getLastMessage();
        Emisor emisor = randomChat.getEmisor();

        final String keyChat = refUserChats.push().getKey();
        String keyPostMessage = refPosts.push().getKey();

        randomChat.setReceptor(me);
        randomChat.setEstado(Constants.CHAT_STATE_PARED);
        randomChat.setAction(Constants.CHAT_STATE_NO_ACTION);
        randomChat.setTime(- new Date().getTime());
        randomChat.setLastMessage(ChatMessage.getParedByPostMessage());
        randomChat.setKeyChat(keyChat);

        String pathToPared = "/" + Constants.CHILD_COUNTRIES + "/" + country.getNameID() + "/" +  Constants.CHILD_RANDOMS + "/"+ Constants.CHAT_STATE_PARED + "/" + keyChat;
        String pathToHisto = "/" + Constants.CHILD_USERS + "/" + me.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + keyChat;
        String pathToHistoEmisor = "/" + Constants.CHILD_USERS + "/" + emisor.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + keyChat;
        String notificationPath = "/" + Constants.CHILD_NOTIFICATIONS + "/" + keyChat;


        Map<String, Object> updateNodes = new HashMap<>();


        ChatMessage paredMessage = ChatMessage.getParedByPostMessage();

        updateNodes.put(pathToPared + "/emisor", emisor.toMap());
        updateNodes.put(pathToPared + "/receptor", me.toMap());
        updateNodes.put(pathToPared + "/estado", Constants.CHAT_STATE_PARED);
        updateNodes.put(pathToPared + "/action", Constants.CHAT_STATE_NO_ACTION);
        updateNodes.put(pathToPared + "/time", -new Date().getTime());
        updateNodes.put(pathToPared + "/messages/"+ keyChat + "/messageText", paredMessage.getMessageText());
        updateNodes.put(pathToPared + "/messages/"+ keyChat + "/androidID", paredMessage.getAndroidID());
        updateNodes.put(pathToPared + "/messages/"+ keyChat + "/messageStatus", paredMessage.getMessageStatus());
        updateNodes.put(pathToPared + "/messages/"+ keyChat + "/messageType", paredMessage.getMessageType());
        updateNodes.put(pathToPared + "/messages/"+ keyChat + "/messageTime", paredMessage.getMessageTime());

        updateNodes.put(pathToPared + "/messages/"+ keyPostMessage + "/messageText", postMessage.getMessageText());
        updateNodes.put(pathToPared + "/messages/"+ keyPostMessage + "/androidID", postMessage.getAndroidID());
        updateNodes.put(pathToPared + "/messages/"+ keyPostMessage + "/messageStatus", postMessage.getMessageStatus());
        updateNodes.put(pathToPared + "/messages/"+ keyPostMessage + "/messageType", postMessage.getMessageType());
        updateNodes.put(pathToPared + "/messages/"+ keyPostMessage + "/messageTime", postMessage.getMessageTime());


        updateNodes.put(pathToHisto, randomChat.toHistoMap());
        updateNodes.put(pathToHistoEmisor, randomChat.toHistoMap());

        updateNodes.put(notificationPath, new Notification(me.getKeyDevice(), emisor.getKeyDevice(), context.getString(R.string.message_say_hi), country.getCountryID(), keyChat, me.getGenero(), Constants.SENT).toMap());

        firebaseDatabase.getReference().updateChildren(updateNodes, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d(TAG, "DatabaseReference : " + databaseReference);
                if (databaseError != null) {
                    //HUBO UN PUTO ERROR
                    Log.d(TAG, "updateReceptor, databaseError : " + databaseError.getMessage());
                    listenerPosts.onFailed(databaseError.getMessage());
                    //readNodeRandoms();
                } else {
                    //ON SUCCESS.
                    listenerPosts.onChatLaunched(country.getCountryID(), keyChat);
                }
            }
        });

    }


    private void createNewChat(final Emisor me) {
        Log.d(TAG, "createNewChat ...");
        String keyChat = "";

        keyChat = refWaiting.push().getKey();

        RandomChat chatRandom = new RandomChat();
        /*
        Emisor emisor = new Emisor();
        emisor.setKeyDevice(android_id);
        emisor.setEdad(emisorEdad);
        emisor.setGenero(genero.name());
        emisor.setLooking(new Looking(receptorEdadMin, receptorEdadMax, generoReceptor.name()));*/

        //EL RECEPTOR ESTARÁ VACÍO
        //final Receptor receptor = new Receptor();
        Emisor receptor = new Emisor();

        chatRandom.setEmisor(me);
        chatRandom.setReceptor(receptor);
        chatRandom.setEstado(RandomChat.WAITING);
        chatRandom.setSearch(me.getGenero()+"_LOOKING_"+me.getLooking().getGenero());
        chatRandom.setAction(Constants.CHAT_STATE_NO_ACTION);
        chatRandom.setTime(new Date().getTime());

        chatRandom.setKeyChat(keyChat);
        chatRandom.setNoReaded(0);
        chatRandom.setHot(false);
        chatRandom.setLastMessage(ChatMessage.getWaitingMessage());
        chatRandom.setCountry(country);

        Log.d(TAG, "KEY CHAT : " + keyChat);

        String pathToHisto = "/" + Constants.CHILD_USERS + "/" + me.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + keyChat;
        String pathToWaiting = "/" + Constants.CHILD_COUNTRIES + "/" + country.getNameID() + "/" +  Constants.CHILD_RANDOMS + "/"+ Constants.CHAT_STATE_WAITING + "/" + keyChat;
        String pathToPared = "/" + Constants.CHILD_COUNTRIES + "/" + country.getNameID() + "/" +  Constants.CHILD_RANDOMS + "/"+ Constants.CHAT_STATE_PARED + "/" + keyChat;


        Map<String, Object> chat = new HashMap<>();

        chat.put(pathToWaiting, chatRandom.toMap());
        chat.put(pathToPared, chatRandom.toMap());
        chat.put(pathToHisto, chatRandom.toHistoMap());

        /*

        chat.put(pathToHisto + "/me/edad", me.getEdad());
        chat.put(pathToHisto + "/me/genero", me.getGenero());
        chat.put(pathToHisto + "/me/keyDevice", me.getKeyDevice());
        chat.put(pathToHisto + "/me/looking/edadMin", me.getLooking().getEdadMin());
        chat.put(pathToHisto + "/me/looking/edadMax", me.getLooking().getEdadMax());




        /*chat.put(pathToHisto + "/me/looking/genero", me.getLooking().getGenero());
        chat.put(pathToHisto + "/emisor/edad", emisor.getEdad());
        chat.put(pathToHisto + "/emisor/genero", emisor.getGenero());
        chat.put(pathToHisto + "/emisor/keyDevice", emisor.getKeyDevice());
        chat.put(pathToHisto + "/emisor/looking/edadMin", emisor.getLooking().getEdadMin());
        chat.put(pathToHisto + "/emisor/looking/edadMax", emisor.getLooking().getEdadMax());
        chat.put(pathToHisto + "/emisor/looking/genero", emisor.getLooking().getGenero());

        chat.put(pathToHisto + "/keyChat", keyChat);
        chat.put(pathToHisto + "/star", false);
        chat.put(pathToHisto + "/hot", false);
        chat.put(pathToHisto + "/like", false);
        chat.put(pathToHisto + "/noReaded", 0);


        chat.put(pathToHisto + "/estado", RandomChat.WAITING);
        chat.put(pathToHisto + "/action", Constants.CHAT_STATE_NO_ACTION);
        chat.put(pathToHisto + "/time", new Date().getTime());

        chat.put(pathToHisto + "/lastMessage/messageText", "No messages.");
        chat.put(pathToHisto + "/lastMessage/androidID", androidID);
        chat.put(pathToHisto + "/lastMessage/messageStatus", Constants.SENT);
        chat.put(pathToHisto + "/lastMessage/messageType", Constants.MESSAGE_TEXT);
        chat.put(pathToHisto + "/lastMessage/messageTime", new Date().getTime());*/

        final String finalKeyChat = keyChat;
        firebaseDatabase.getReference().updateChildren(chat, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.d(TAG, "createNewChat databaseError: " + databaseError.getMessage());
                    listener.onFailed(databaseError.getMessage());
                } else {
                    Log.d(TAG, "createNewChat SUCCESS: " + true);
                    listener.onChatLaunched(country.getCountryID(), finalKeyChat);
                }
            }
        });

        /*
        refNewChat.setValue(chatRandom, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

            }
        });*/
    }

    private void paredChat(final String key, final Emisor me, final Emisor emisor) {
        Log.d(TAG, "paredChat ...");
        Log.d(TAG, "key: " + key);
        //DatabaseReference estadoKey = refRandoms.child(key).child("estado");

        DatabaseReference estadoKey = refWaiting.child(key).child("estado");

        estadoKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "refWaiting.child(key).child(\"estado\") DataSnapshot : " + dataSnapshot);

                if (dataSnapshot != null){
                    if (dataSnapshot.getValue().equals(RandomChat.WAITING)) {
                        updateReceptor(key, RandomChat.PARED, me, emisor);
                    }
                } else {
                    //YA NO ESTÁ ESPERANDO ALGÚN PUTITO SE EMPAREJÓ JUSTO ANTES QUE YO. CARAJO
                    Log.d(TAG, "ALGÚN PUTITO, SE EMPAREJÓ MILISEGUNDOS ANTES QUE YO : " + true);
                    //SI ESTO PASA, ENTONCES VUELVO A LEER TODOS LOS REGISTROS
                    readNodeRandoms(me);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "DatabaseError : " + databaseError);
            }
        });
    }

    private void updateReceptor(final String keyChat, String state, final Emisor me, final Emisor emisor) {

        Log.d(TAG, "updateReceptor node...: ");
        Log.d(TAG, "key: " + keyChat);
        Log.d(TAG, "state: " + state);

        //String pathToRandom = "/" + Constants.CHILD_RANDOMS + "/"+ keyChat;

        String pathToWaiting = "/" + Constants.CHILD_COUNTRIES + "/" + country.getNameID() + "/" +  Constants.CHILD_RANDOMS + "/"+ Constants.CHAT_STATE_WAITING + "/" + keyChat;
        String pathToPared = "/" + Constants.CHILD_COUNTRIES + "/" + country.getNameID() + "/" +  Constants.CHILD_RANDOMS + "/"+ Constants.CHAT_STATE_PARED + "/" + keyChat;

        String pathToHistoEmisor = "/" + Constants.CHILD_USERS + "/" + emisor.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + keyChat;
        String pathToHistoMe = "/" + Constants.CHILD_USERS + "/" + me.getKeyDevice() + "/" + Constants.CHILD_USERS_HISTO_CHATS + "/" + keyChat;

        //SIGUE ESPERANDO EMPAREJAR YA!!!!!!

        long now = new Date().getTime();

        Map<String, Object> updateNodes = new HashMap<String, Object>();
        //state = RandomChat.PARED

        RandomChat nodeRandom = new RandomChat();
        nodeRandom.setEmisor(emisor);
        nodeRandom.setReceptor(me);
        nodeRandom.setEstado(Constants.CHAT_STATE_PARED);
        nodeRandom.setAction(Constants.CHAT_STATE_NO_ACTION);
        nodeRandom.setTime(now);
        nodeRandom.setKeyChat(keyChat);
        nodeRandom.setHot(false);
        nodeRandom.setLastMessage(ChatMessage.getParedMessage());
        nodeRandom.setNoReaded(0);
        nodeRandom.setCountry(country);


        updateNodes.put(pathToWaiting, null);
        updateNodes.put(pathToPared + "/emisor", emisor.toMap());
        updateNodes.put(pathToPared + "/receptor", me.toMap());
        updateNodes.put(pathToPared + "/estado", Constants.CHAT_STATE_PARED);
        updateNodes.put(pathToPared + "/action", Constants.CHAT_STATE_NO_ACTION);
        updateNodes.put(pathToPared + "/time", now);
        updateNodes.put(pathToPared + "/messages/"+keyChat, ChatMessage.getParedMessage().toMap());

        updateNodes.put(pathToHistoEmisor, nodeRandom.toHistoMap());
        updateNodes.put(pathToHistoMe, nodeRandom.toHistoMap());


        firebaseDatabase.getReference().updateChildren(updateNodes, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d(TAG, "DatabaseReference : " + databaseReference);
                if (databaseError != null) {
                    //HUBO UN PUTO ERROR
                    Log.d(TAG, "updateReceptor, databaseError : " + databaseError.getMessage());
                    listener.onFailed(databaseError.getMessage());
                    //readNodeRandoms();
                } else {

                    //ON SUCCESS.
                    listener.onChatLaunched(country.getCountryID(), keyChat);

                }
            }
        });
    }


    public void initChats(String androidID, OnChatsListener listenerChats) {

        this.androidID = androidID;
        this.refUserChats = firebaseDatabase.getReference(Constants.CHILD_USERS).child(androidID).child(Constants.CHILD_USERS_HISTO_CHATS);
        refUserChats.keepSynced(true);
        this.listenerChats = listenerChats;
        listenChats();
        listenChatsHot();
    }

    private void listenChatsHot() {
        queryChatsHot = refUserChats.orderByChild("hot").equalTo(true).limitToFirst(limitHots);
        chatsHotListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null){
                    return;
                }

                Log.d(TAG, "listenChatsHot DataSnapshot COUNT: " + dataSnapshot.getChildrenCount());
                List<RandomChat> list = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.getValue() != null){
                            Log.d(TAG, "CHILDREN KEY: " + postSnapshot.getKey());
                            RandomChat chat = postSnapshot.getValue(RandomChat.class);
                            //Log.d(TAG, "keyChat : " + chat.getKeyChat());
                            list.add(chat);
                        }
                    }
                }

                if (listenerChats!=null){
                    listenerChats.onChatsHotListener(list.size()>0, list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listenerChats!=null){
                    listenerChats.onChatsHotListener(false, null);
                }
            }
        };

        queryChatsHot.addValueEventListener(chatsHotListener);
    }

    public void removeChatsListener(){
        //refUserChats.removeEventListener(chatsListener);
        queryChats.removeEventListener(chatsListener);
    }



    private void listenChats() {
        queryChats = refUserChats.orderByChild("lastMessage/messageTime").limitToFirst(limit);
        chatsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if (dataSnapshot == null) {
                    return;
                }

                Log.d(TAG, "DataSnapshot COUNT: " + dataSnapshot.getChildrenCount());

                List<RandomChat> list = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.getValue() != null){
                            Log.d(TAG, "CHILDREN KEY: " + postSnapshot.getKey());
                            Log.d(TAG, "postSnapshot"+ postSnapshot);
                            RandomChat chat = postSnapshot.getValue(RandomChat.class);
//                            Log.d(TAG, "keyChat: " + chat.getKeyChat());
                            list.add(chat);
                        }
                    }
                }

                if (listenerChats!=null){
                    listenerChats.onChatChangedListener(list.size()>0, list);
                }

                //setRecyclerView(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listenerChats!=null){
                    listenerChats.onChatChangedListener(false, null);
                }
                //Toast.makeText(Historial.this, "DatabaseError: "+ databaseError, Toast.LENGTH_SHORT).show();
            }
        };
        queryChats.addValueEventListener(chatsListener);
    }

    private void listenPosts(){
        queryPosts = refPosts.orderByChild("time").limitToFirst(limitPost);
        postsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if (dataSnapshot == null){
                    return;
                }

                Log.d(TAG, "DataSnapshot COUNT: " + dataSnapshot.getChildrenCount());

                List<RandomChat> list = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.getValue() != null){
                            Log.d(TAG, "CHILDREN KEY: " + postSnapshot.getKey());
                            RandomChat chat = postSnapshot.getValue(RandomChat.class);
                            Log.d(TAG, "chat: "+ chat.getKeyChat());
                            list.add(chat);
                        }
                    }
                }

                if (listenerChats!=null){
                    listenerChats.onPostsListener(list.size()>0, list);
                }

                //setRecyclerView(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listenerChats!=null){
                    listenerChats.onPostsListener(false, null);
                }
                //Toast.makeText(Historial.this, "DatabaseError: "+ databaseError, Toast.LENGTH_SHORT).show();
            }
        };
        queryPosts.addValueEventListener(postsListener);
    }

    public void setOff(){
        refUsers.child(androidID).child(Constants.CHILD_CONNECTION).setValue(new Connection(Constants.STATE_OFFLINE, new Date().getTime()));
    }
    public void setOn(){
        refUsers.child(androidID).child(Constants.CHILD_CONNECTION).setValue(new Connection(Constants.STATE_ONLINE, new Date().getTime()));
    }




}
