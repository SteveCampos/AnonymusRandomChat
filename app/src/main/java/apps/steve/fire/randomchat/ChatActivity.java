package apps.steve.fire.randomchat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.steve.fire.randomchat.adapters.ChatAdapter;
import apps.steve.fire.randomchat.firebase.FirebaseRoom;
import apps.steve.fire.randomchat.interfaces.OnRoomListener;
import apps.steve.fire.randomchat.model.ChatMessage;
import apps.steve.fire.randomchat.model.Chater;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.widgets.Emoji;
import apps.steve.fire.randomchat.widgets.EmojiView;
import apps.steve.fire.randomchat.widgets.SizeNotifierRelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatActivity extends AppCompatActivity implements SizeNotifierRelativeLayout.SizeNotifierRelativeLayoutDelegate, NotificationCenter.NotificationCenterDelegate, OnRoomListener {

    private static final String TAG = ChatActivity.class.getSimpleName();
    //private EditText chatEditText1;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    //private ImageView enterChatView1, emojiButton;
    private ChatAdapter chatAdapter;
    private EmojiView emojiView;
    //private SizeNotifierRelativeLayout sizeNotifierRelativeLayout;
    private boolean showingEmoji;
    private int keyboardHeight;
    private boolean keyboardVisible;
    private WindowManager.LayoutParams windowLayoutParams;
    private String android_id = "ANDROID_ID_UNRESOLVED";

    //DATABASE REFERENCES
    DatabaseReference randomChat;
    FirebaseDatabase database;

    private boolean isHistorialCreated;

    @BindView(R.id.recycler) RecyclerView recycler;
    @BindView(R.id.chat_edit_text1) EditText chatEditText1;
    @BindView(R.id.enter_chat1) ImageView enterChatView1;
    @BindView(R.id.emojiButton) ImageView emojiButton;
    @BindView(R.id.chat_layout) SizeNotifierRelativeLayout sizeNotifierRelativeLayout;
    @BindView(R.id.my_toolbar) Toolbar toolbar;
    @BindView(R.id.tb_title) TextView title;
    @BindView(R.id.tb_last_connection) TextView textLastConnection;
    @BindView(R.id.av_top_banner) AdView mAdView;

    private FirebaseRoom firebaseRoom;

    public String androidID;
    private String androidIDReceptor;
    private boolean isRecepterOnline = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initViews();

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            Log.d(TAG, "savedInstanceState getState: " + savedInstanceState.getBoolean("isHistorialCreated"));
            isHistorialCreated = savedInstanceState.getBoolean("isHistorialCreated");
        }

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        String key_random = getIntent().getStringExtra("key_random");
        String start_type = getIntent().getStringExtra("start_type");

        androidID = getIntent().getStringExtra("android_id");
        androidIDReceptor = getIntent().getStringExtra("android_id_receptor");



        firebaseRoom = new FirebaseRoom(key_random, androidID, this);

        if (!TextUtils.isEmpty(androidIDReceptor) && !TextUtils.isEmpty(androidID)){
            /*if (!androidID.equals(android_id)){
                androidIDReceptor = androidID;
                androidID = android_id;
            }*/
            firebaseRoom.listenReceptorConnection(androidIDReceptor);
        }

        // FIRE DATABASE INSTANCE
        //database = FirebaseDatabase.getInstance();
        //randomChat = database.getReference(Constants.CHILD_RANDOMS).child(key_random).child(Constants.CHILD_MESSAGES);




        // Attach an listener to read the data at our posts reference
        /*randomChat.limitToLast(10).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                Log.d(Constants.TAG,"There are " + dataSnapshot.getChildrenCount() + " messages");
                chatMessages.clear();
                for (com.google.firebase.database.DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ChatMessage message = postSnapshot.getValue(ChatMessage.class);
                    Log.d(Constants.TAG, message.getMessageText() + " - " + message.getAndroidID());
                    //SI LO ESTÁ LEYENDO DEL SERVER, ESO QUIERO DECIR, QUE YA ESTÁ EN EL SERVER, SIEMPRE.
                    message.setMessageStatus(Constants.DELIVERED);
                    chatMessages.add(message);
                }

                if(chatAdapter!=null)
                    chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(Constants.TAG,"The read failed: " + databaseError.getMessage());
                Toast.makeText(getActivity(), "THE READ FAILED : " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/

        manageType(start_type, key_random);

        // Display icon in the toolbar
        /*
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        */


    }

    private void initViews() {

        AndroidUtilities.statusBarHeight = getStatusBarHeight();


        // Hide the emoji on click of edit text
        chatEditText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showingEmoji)
                    hideEmojiPopup();
            }
        });


        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmojiPopup(!showingEmoji);
            }
        });

        chatEditText1.setOnKeyListener(keyListener);

        enterChatView1.setOnClickListener(clickListener);

        chatEditText1.addTextChangedListener(watcher1);

        sizeNotifierRelativeLayout.delegate = this;

        NotificationCenter.getInstance().addObserver(this, NotificationCenter.emojiDidLoaded);

        setRecyclerView();
        setToolbar();
        loadAds();
    }

    private void loadAds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("7D610238E4AC96FE6016B6B6DF36209A")
                .addTestDevice("6A4C8AA799F4A30921C02DC505824DC0")
                .build();
        mAdView.loadAd(adRequest);
    }

    private void setRecyclerView(){
        recycler.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter= new ChatAdapter(chatMessages, this);
        recycler.setAdapter(chatAdapter);
    }
    private void setToolbar() {

        setSupportActionBar(toolbar);
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Toast.makeText(getActivity(), "Navigation Icon Clicked.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void manageType(String start_type, String key_random){

        Log.d(TAG, "START TYPE: "+ start_type);
        title.setText("");
        textLastConnection.setText(R.string.chat_state_waiting);
        switch (start_type){
            case Constants._HERE:
                //Receptor me = getIntent().getParcelableExtra("me");
                //String ageGroup = getAgeGroup(Constants.Genero.valueOf(me.getLooking().getGenero()), me.getEdad());
                break;
            case Constants._PARED:
                //AQUÍ CREAR LOS REGISTROS DE CHAT, POR CADA USUARIO.
                //ANTES DE REALIZAR ESO, CREAR A LOS USUARIOS


                Emisor receptor_me = getIntent().getParcelableExtra("me");
                Log.d(TAG, "RECEPTOR ME");
                Log.d(TAG, "PARED TYPE : "  + receptor_me.getClass().getSimpleName());
                Log.d(TAG, "PARED KEY DEVICE : "  + receptor_me.getKeyDevice());
                Log.d(TAG, "PARED EDAD : "  + receptor_me.getEdad());
                Log.d(TAG, "PARED GENERO : "  + receptor_me.getGenero());
                Log.d(TAG, "PARED LOOGINK GENERO : "  + receptor_me.getLooking().getGenero());
                Emisor emisor = getIntent().getParcelableExtra("emisor");
                Log.d(TAG, "EMISRO HER/HIM");
                Log.d(TAG, "PARED TYPE : "  + emisor.getClass().getSimpleName());
                Log.d(TAG, "PARED KEY DEVICE : "  + emisor.getKeyDevice());
                Log.d(TAG, "PARED EDAD : "  + emisor.getEdad());
                Log.d(TAG, "PARED GENERO : "  + emisor.getGenero());
                Log.d(TAG, "PARED LOOGINK GENERO : "  + receptor_me.getLooking().getGenero());

                Chater c_me = new Chater(receptor_me);
                Log.d(TAG, "RECEPTOR CHATER ME");
                Log.d(TAG, "PARED TYPE : "  + c_me.getClass().getSimpleName());
                Log.d(TAG, "PARED KEY DEVICE : "  + c_me.getKeyDevice());
                Log.d(TAG, "PARED EDAD : "  + c_me.getEdad());
                Log.d(TAG, "PARED GENERO : "  + c_me.getGenero());
                Log.d(TAG, "PARED LOOGINK GENERO : "  + c_me.getLooking().getGenero());

                Chater c_emisor = new Chater(emisor);

                Log.d(TAG, "CHATER EMISOR HER/HIM");
                Log.d(TAG, "PARED TYPE : "  + c_emisor.getClass().getSimpleName());
                Log.d(TAG, "PARED KEY DEVICE : "  + c_emisor.getKeyDevice());
                Log.d(TAG, "PARED EDAD : "  + c_emisor.getEdad());
                Log.d(TAG, "PARED GENERO : "  + c_emisor.getGenero());
                Log.d(TAG, "PARED LOOGINK GENERO : "  + c_emisor.getLooking().getGenero());


                firebaseRoom.createHistoryChat(androidID, new Chater(receptor_me), new Chater(emisor));
                //createHistoChats(key_random, new Chater(receptor_me), new Chater(emisor));
                break;
            case Constants._NOTIFICATION:
                break;
            default:
                break;
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState isHistorialCreated putState: " + isHistorialCreated);
        outState.putBoolean("isHistorialCreated", isHistorialCreated);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press

                EditText editText = (EditText) v;

                if(v==chatEditText1)
                {
                    sendMessage(editText.getText().toString(), Constants.MESSAGE_TEXT);
                }

                chatEditText1.setText("");

                return true;
            }
            return false;

        }
    };

    private ImageView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v==enterChatView1)
            {
                sendMessage(chatEditText1.getText().toString(), Constants.MESSAGE_TEXT);
            }

            chatEditText1.setText("");

        }
    };

    private final TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            Log.d(TAG, "onTextChanged chatEditText1.getText().toString(): " + chatEditText1.getText().toString());
            /*if (chatEditText1.getText().toString().equals("")) {

            } else {
                //enterChatView1.setImageResource(R.drawable.ic_chat_send);
                //firebaseRoom.changeState(Constants.CHAT_STATE_NO_ACTION);
            }*/
        }

        @Override
        public void afterTextChanged(Editable editable) {
            Log.d(TAG, "afterTextChanged editable.length(): " + editable.length());
            if(editable.length()==0){
                enterChatView1.setImageResource(R.drawable.ic_chat_send);
                firebaseRoom.changeState(Constants.CHAT_STATE_NO_ACTION);
            }else{
                enterChatView1.setImageResource(R.drawable.ic_chat_send_active);
                firebaseRoom.changeState(androidID + Constants.CHAT_STATE_WRITING);
            }
        }
    };

    private void sendMessage(final String messageText, final String messageType){

        if(messageText.trim().length()==0)
            return;


        final ChatMessage message = new ChatMessage();
        message.setMessageStatus(Constants.SENT);
        message.setMessageText(messageText);
        message.setAndroidID(android_id);
        message.setMessageType(messageType);
        message.setMessageTime(new Date().getTime());
        chatMessages.add(message);

        firebaseRoom.sendMessage(androidID, androidIDReceptor, message, isRecepterOnline ,calculateMessagesNoReaded());
    }

    private List<String> calculateMessagesNoReaded(){
        List<String> messageNoReaded = new ArrayList<>();
        for (int i=0; i< chatMessages.size(); i++){
            if (chatMessages.get(i).getMessageStatus() != Constants.READED && chatMessages.get(i).getAndroidID().equals(androidID)){ //&& chatMessages.get(i).getAndroidID().equals(androidID)
                messageNoReaded.add(chatMessages.get(i).getMessageText());
            }
        }
        Log.d(TAG, "messageNoReaded.size(): " +messageNoReaded.size());
        return messageNoReaded;
    }

    private AppCompatActivity getActivity()
    {
        return this;
    }


    /**
     * Show or hide the emoji popup
     *
     * @param show
     */
    private void showEmojiPopup(boolean show) {
        showingEmoji = show;

        if (show) {
            if (emojiView == null) {
                if (getActivity() == null) {
                    return;
                }
                emojiView = new EmojiView(getActivity());

                emojiView.setListener(new EmojiView.Listener() {
                    public void onBackspace() {
                        chatEditText1.dispatchKeyEvent(new KeyEvent(0, 67));
                    }

                    public void onEmojiSelected(String symbol) {
                        int i = chatEditText1.getSelectionEnd();
                        if (i < 0) {
                            i = 0;
                        }
                        try {
                            CharSequence localCharSequence = Emoji.replaceEmoji(symbol, chatEditText1.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20));
                            chatEditText1.setText(chatEditText1.getText().insert(i, localCharSequence));
                            int j = i + localCharSequence.length();
                            chatEditText1.setSelection(j, j);
                        } catch (Exception e) {
                            Log.e(Constants.TAG, "Error showing emoji");
                        }
                    }
                });


                windowLayoutParams = new WindowManager.LayoutParams();
                windowLayoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
                if (Build.VERSION.SDK_INT >= 21) {
                    windowLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
                } else {
                    windowLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
                    windowLayoutParams.token = getActivity().getWindow().getDecorView().getWindowToken();
                }
                windowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            }

            final int currentHeight;

            if (keyboardHeight <= 0)
                keyboardHeight = App.getInstance().getSharedPreferences("emoji", 0).getInt("kbd_height", AndroidUtilities.dp(200));

            currentHeight = keyboardHeight;

            WindowManager wm = (WindowManager) App.getInstance().getSystemService(Activity.WINDOW_SERVICE);

            windowLayoutParams.height = currentHeight;
            windowLayoutParams.width = AndroidUtilities.displaySize.x;

            try {
                if (emojiView.getParent() != null) {
                    wm.removeViewImmediate(emojiView);
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, e.getMessage());
            }

            try {
                wm.addView(emojiView, windowLayoutParams);
            } catch (Exception e) {
                Log.e(Constants.TAG, e.getMessage());
                return;
            }

            if (!keyboardVisible) {
                if (sizeNotifierRelativeLayout != null) {
                    sizeNotifierRelativeLayout.setPadding(0, 0, 0, currentHeight);
                }

                return;
            }

        }
        else {
            removeEmojiWindow();
            if (sizeNotifierRelativeLayout != null) {
                sizeNotifierRelativeLayout.post(new Runnable() {
                    public void run() {
                        if (sizeNotifierRelativeLayout != null) {
                            sizeNotifierRelativeLayout.setPadding(0, 0, 0, 0);
                        }
                    }
                });
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(Constants.TAG, ""+item.getTitle());
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Log.d(Constants.TAG, "ID.action_favorite");
                Snackbar.make(sizeNotifierRelativeLayout, "Hot.", Snackbar.LENGTH_LONG).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Remove emoji window
     */
    private void removeEmojiWindow() {
        if (emojiView == null) {
            return;
        }
        try {
            if (emojiView.getParent() != null) {
                WindowManager wm = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
                wm.removeViewImmediate(emojiView);
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage());
        }
    }



    /**
     * Hides the emoji popup
     */
    public void hideEmojiPopup() {
        if (showingEmoji) {
            showEmojiPopup(false);
        }
    }

    /**
     * Check if the emoji popup is showing
     *
     * @return
     */
    public boolean isEmojiPopupShowing() {
        return showingEmoji;
    }



    /**
     * Updates emoji views when they are complete loading
     *
     * @param id
     * @param args
     */
    @Override
    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.emojiDidLoaded) {
            if (emojiView != null) {
                emojiView.invalidateViews();
            }

            if (recycler != null) {
                recycler.invalidate();
            }
        }
    }

    @Override
    public void onSizeChanged(int height) {

        Rect localRect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);

        WindowManager wm = (WindowManager) App.getInstance().getSystemService(Activity.WINDOW_SERVICE);
        if (wm == null || wm.getDefaultDisplay() == null) {
            return;
        }


        if (height > AndroidUtilities.dp(50) && keyboardVisible) {
            keyboardHeight = height;
            App.getInstance().getSharedPreferences("emoji", 0).edit().putInt("kbd_height", keyboardHeight).commit();
        }


        if (showingEmoji) {
            int newHeight = 0;

            newHeight = keyboardHeight;

            if (windowLayoutParams.width != AndroidUtilities.displaySize.x || windowLayoutParams.height != newHeight) {
                windowLayoutParams.width = AndroidUtilities.displaySize.x;
                windowLayoutParams.height = newHeight;

                wm.updateViewLayout(emojiView, windowLayoutParams);
                if (!keyboardVisible) {
                    sizeNotifierRelativeLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            if (sizeNotifierRelativeLayout != null) {
                                sizeNotifierRelativeLayout.setPadding(0, 0, 0, windowLayoutParams.height);
                                sizeNotifierRelativeLayout.requestLayout();
                            }
                        }
                    });
                }
            }
        }


        boolean oldValue = keyboardVisible;
        keyboardVisible = height > 0;
        if (keyboardVisible && sizeNotifierRelativeLayout.getPaddingBottom() > 0) {
            showEmojiPopup(false);
        } else if (!keyboardVisible && keyboardVisible != oldValue && showingEmoji) {
            showEmojiPopup(false);
        }

    }

    @Override
    public void onDestroy() {
        firebaseRoom.removeMessageListener();
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
    }

    /**
     * Get the system status bar height
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
        hideEmojiPopup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onChatHistoryCreated(boolean success, String error) {

    }

    @Override
    public void onMessagedSended(boolean success, ChatMessage message, String error) {

    }

    @Override
    public void onReadMessages(List<ChatMessage> messages) {
        chatMessages.clear();
        Log.d(TAG, "onReadMessages size: "+ messages.size());
        chatMessages.addAll(messages);
        if (chatAdapter != null) {
            chatAdapter.notifyDataSetChanged();
        }
        recycler.scrollToPosition(messages.size() - 1);

        firebaseRoom.setReaded(messages);
    }



    @Override
    public void onReceptorConnectionChanged(String state, long lastConnection) {
        Log.d(TAG, "onReceptorConnectionChanged state: " + state+", lastConnection: " + lastConnection);
        if (textLastConnection!=null){
            if (state.equals(Constants.STATE_ONLINE)){
                isRecepterOnline = true;
                textLastConnection.setText(R.string.state_online);
            }else {
                isRecepterOnline = false;
                textLastConnection.setText(Utils.calculateLastConnection(new Date(lastConnection), new Date(), getActivity()));
            }
        }
    }

    @Override
    public void onChatStateChanged(String state) {
        Log.d(TAG, "onChatStateChanged state: " + state);
        if (textLastConnection!=null){
            if (state.equals(androidIDReceptor + Constants.CHAT_STATE_WRITING)){
                textLastConnection.setText(R.string.chat_state_writing);
            }else if(state.equals(Constants.CHAT_STATE_NO_ACTION)){
                if (isRecepterOnline){
                    textLastConnection.setText(R.string.state_online);
                }
            }if (state.equals(Constants.CHAT_STATE_PARED)){
                //Display and amazing message here! Chat is pared!
                Toast.makeText(getActivity(), Constants.CHAT_STATE_PARED, Toast.LENGTH_SHORT).show();
                textLastConnection.setText(R.string.state_online);
            }
        }
    }
}
