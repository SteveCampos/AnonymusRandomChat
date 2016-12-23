package apps.steve.fire.randomchat;

import android.app.Activity;
import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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


import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import apps.steve.fire.randomchat.activities.FullImageActivity;
import apps.steve.fire.randomchat.activities.MainActivity;
import apps.steve.fire.randomchat.adapters.ChatAdapter;
import apps.steve.fire.randomchat.firebase.FirebaseRoom;
import apps.steve.fire.randomchat.firebase.FirebaseStorageHelper;
import apps.steve.fire.randomchat.interfaces.OnChatMessageListener;
import apps.steve.fire.randomchat.interfaces.OnRoomListener;
import apps.steve.fire.randomchat.model.ChatMessage;
import apps.steve.fire.randomchat.model.Country;
import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.widgets.Emoji;
import apps.steve.fire.randomchat.widgets.EmojiView;
import apps.steve.fire.randomchat.widgets.SizeNotifierRelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import me.leolin.shortcutbadger.ShortcutBadger;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import static apps.steve.fire.randomchat.R.id.imageView;


public class ChatActivity extends AppCompatActivity implements SizeNotifierRelativeLayout.SizeNotifierRelativeLayoutDelegate, NotificationCenter.NotificationCenterDelegate, OnRoomListener, OnChatMessageListener {

    private static final String TAG = ChatActivity.class.getSimpleName();
    private static final int RC_PHOTO_PICKER = 144;
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

    private boolean isHistorialCreated;

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.chat_edit_text1)
    EditText chatEditText1;
    @BindView(R.id.enter_chat1)
    ImageView enterChatView1;
    @BindView(R.id.emojiButton)
    ImageView emojiButton;
    @BindView(R.id.chat_layout)
    SizeNotifierRelativeLayout sizeNotifierRelativeLayout;
    @BindView(R.id.my_toolbar)
    Toolbar toolbar;
    @BindView(R.id.tb_title)
    TextView title;
    @BindView(R.id.tb_last_connection)
    TextView textLastConnection;
    @BindView(R.id.av_top_banner)
    AdView mAdView;

    private FirebaseRoom firebaseRoom;

    public String androidID;

    private Emisor me;
    private Emisor him;

    private boolean isHot = false;

    private boolean mIsRunning = false;
    private String keyRandom;
    private int countryId;
    private String chatState = Constants.CHAT_STATE_BLOCK;



    private boolean isBack = false;
    private long lastConnection = 0;
    private String connectionState = Constants.STATE_OFFLINE;


    //Storage Firebase
    FirebaseStorageHelper fireStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_chat);

        Nammu.init(getApplicationContext());
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            Log.d(TAG, "savedInstanceState getState: " + savedInstanceState.getBoolean("isHistorialCreated"));
            isHistorialCreated = savedInstanceState.getBoolean("isHistorialCreated");
        }

        androidID = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Intent intent = getIntent();

        this.keyRandom = intent.getStringExtra("key_random");
        this.countryId = intent.getIntExtra("country_id", Country.PERU);

        if (intent.hasExtra("extra_unread")){
            int unread = intent.getIntExtra("extra_unread", 0);
            Log.d(TAG, "extra_unread: " + unread);
            updateShortCut(unread, keyRandom);
        }

    }
    private void updateShortCut(int x, String keyRandom) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();

        int noReaded = preferences.getInt(Constants.PREF_NOTI_COUNT, 0);
        String notiKeys = preferences.getString(Constants.PREF_NOTI_KEYS, "");

        Log.d(TAG, "preferences.getInt(Constants.PREF_NOTI_COUNT, 0): " + noReaded);
        Log.d(TAG, "item.getNoReaded(): " + x);
        Log.d(TAG, " count - noReaded: " + (noReaded - x));
        Log.d(TAG, "preferences.getString(Constants.PREF_NOTI_KEYS, ): " + notiKeys);

        String notiKeysRemoved = notiKeys.replace(keyRandom + ".", "");
        Log.d(TAG, "notiKeysRemoved " + notiKeysRemoved);

        noReaded = noReaded > x ? (noReaded - x) : 0;
        Log.d(TAG, "RESULTADO RESTA : " + noReaded);


        editor.putString(keyRandom, ""); //Notifications messages to keyChat Readed!
        editor.putString(Constants.PREF_NOTI_KEYS, notiKeysRemoved);
        editor.putInt(Constants.PREF_NOTI_COUNT, noReaded);
        editor.apply();


        ShortcutBadger.applyCount(getActivity(), noReaded);
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        isBack = false;
        if (mAdView != null) {
            mAdView.resume();
        }
        mIsRunning = true;
        ButterKnife.bind(this);
        firebaseRoom = new FirebaseRoom(countryId, keyRandom, androidID, this);
        firebaseRoom.setOn();
        fireStorage = new FirebaseStorageHelper(androidID);
        initViews();
        super.onResume();
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
                .build();
        mAdView.loadAd(adRequest);
    }

    private void setRecyclerView() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(chatMessages, this, this);
        recycler.setAdapter(chatAdapter);
    }

    private void setToolbar() {

        setSupportActionBar(toolbar);
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBack= true;
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void intentPickerImage(){
        //EasyImage.openGallery(this, 0);

        /**Permission check only required if saving pictures to root of sdcard*/
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            EasyImage.openGallery(this, 0);
        } else {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    EasyImage.openGallery(ChatActivity.this, 0);
                }
                @Override
                public void permissionRefused() {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                onPicturesReturned(imageFile);
            }


            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
            }
        });
    }

    private void showFullPicture(Uri uri){
        Intent intent = new Intent(getActivity(), FullImageActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }

    private void onPicturesReturned(File imageFile) {
        Snackbar.make(toolbar, R.string.action_sending, Snackbar.LENGTH_LONG).show();
        //progressBar.setVisibility(View.VISIBLE);

        final long now = - new Date().getTime();
        File compressedImageFile = Compressor.getDefault(getActivity()).compressToFile(imageFile);

        fireStorage.uploadFile(Uri.fromFile(compressedImageFile), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        // When the image has successfully uploaded, we get its download URL
                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        if(downloadUrl!= null){
                            firebaseRoom.sendMessage(new ChatMessage(
                                    downloadUrl.toString(),
                                    androidID,
                                    Constants.SENT,
                                    Constants.MESSAGE_IMAGE,
                                    now,
                                    ""
                            ));
                        }

                        // Set the download URL to the message box, so that the user can send it to the database
                        /*textview.setText("URL IMAGE : \n" + downloadUrl.toString()
                                + "\nLastPathSegment : " + downloadUrl.getLastPathSegment());
                        //begintoDownload(downloadUrl);
                        progressBar.setVisibility(View.INVISIBLE);*/

                        /*imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), FullImageActivity.class);
                                intent.setData(downloadUrl);
                                startActivity(intent);
                            }
                        });

                        */
                    }
                }, new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        /*
                        Log.d(Constants.TAG, "getBytesTransferred : " + taskSnapshot.getBytesTransferred());
                        Log.d(Constants.TAG, "getTotalByteCount : " + taskSnapshot.getTotalByteCount());
                        progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                        textview.setText(""+progress);
                        progressBar.setProgress((int) progress);*/
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
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

                if (v == chatEditText1) {
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

            if (!isWriting){
                intentPickerImage();
            }else{
                if (v == enterChatView1) {
                    sendMessage(chatEditText1.getText().toString(), Constants.MESSAGE_TEXT);
                }
                chatEditText1.setText("");
            }





        }
    };

    private boolean isWriting;
    private final TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //Log.d(TAG, "onTextChanged chatEditText1.getText().toString(): " + chatEditText1.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            Log.d(TAG, "afterTextChanged editable.length(): " + editable.length());
            if (editable.length() == 0) {
                isWriting = false;
                enterChatView1.setImageResource(R.drawable.ic_insert_photo);
                firebaseRoom.changeAction(Constants.CHAT_STATE_NO_ACTION);
            } else {
                isWriting = true;
                enterChatView1.setImageResource(R.drawable.ic_chat_send_active);
                firebaseRoom.changeAction(androidID + Constants.CHAT_STATE_WRITING);
            }
        }
    };

    private void sendMessage(final String messageText, final String messageType) {

        if (messageText.trim().length() == 0)
            return;
        if (chatState.contains(Constants.CHAT_STATE_BLOCK) && !chatState.contains(Constants.CHAT_STATE_UNBLOCKED)) {
            Snackbar.make(toolbar, R.string.message_automatic_blocked, Snackbar.LENGTH_LONG).show();
            return;
        }


        final ChatMessage message = new ChatMessage();
        message.setMessageStatus(Constants.SENT);
        message.setMessageText(messageText);
        message.setAndroidID(androidID);
        message.setMessageType(messageType);
        message.setMessageTime(-new Date().getTime()); // NEGATIVE VALUE, TO ORDERBYVALUE.
        chatMessages.add(message);

        firebaseRoom.sendMessage(message);
    }

    @Override
    public void onBackPressed() {
        isBack= true;
        super.onBackPressed();
    }
    private AppCompatActivity getActivity() {
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

        } else {
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
        Log.d(Constants.TAG, "" + item.getTitle());
        switch (item.getItemId()) {
            case R.id.action_block:
                // User chose the "Settings" item, show the app settings UI...
                if (chatState.contains(Constants.CHAT_STATE_UNBLOCKED)) {
                    firebaseRoom.block(true);
                    break;
                } else if (chatState.equals(androidID + Constants.CHAT_STATE_BLOCK)) {//ESTÁ BLOQUEADO POR EL MISMO
                    firebaseRoom.block(false);
                    break;
                } else if (chatState.contains(Constants.CHAT_STATE_BLOCK)) {
                    //Está bloqueado por el otro
                    Snackbar.make(toolbar, R.string.action_unblock_imposible, Snackbar.LENGTH_LONG).show();
                    break;
                } else {
                    firebaseRoom.block(true);
                    break;
                }

            case R.id.action_hot:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                firebaseRoom.hot();
                break;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                break;
        }
        return super.onOptionsItemSelected(item);
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
            App.getInstance().getSharedPreferences("emoji", 0).edit().putInt("kbd_height", keyboardHeight).apply();
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
        Log.d(TAG, "onDestroy");
        firebaseRoom.removeMessageListener();
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
    }

    /**
     * Get the system status bar height
     *
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
        mIsRunning = false;
        hideEmojiPopup();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        if (!isBack){
            firebaseRoom.setOff();
        }
        super.onStop();
    }

    @Override
    public void onMessagedSended(boolean success, ChatMessage message, String error) {

    }

    @Override
    public void onReadMessages(List<ChatMessage> messages) {
        chatMessages.clear();
        Log.d(TAG, "onReadMessages size: " + messages.size());
        chatMessages.addAll(messages);
        if (chatAdapter != null) {
            chatAdapter.notifyDataSetChanged();
        }
        recycler.scrollToPosition(messages.size() - 1);
        if (mIsRunning) {
            firebaseRoom.setReaded(messages);
        }
    }

    @Override
    public void onMeReaded(Emisor me) {
        Log.d(TAG, "onMeReaded");
        if (me == null) {
            return;
        }

        this.me = me;

        String genero = null;
        switch (Constants.Genero.valueOf(me.getLooking().getGenero())) {
            case CHICO:
                genero = getString(R.string.boy);
                break;
            case CHICA:
                genero = getString(R.string.girl);
                break;
        }

        title.setText(genero);

    }

    @Override
    public void onHimReaded(Emisor him) {
        Log.d(TAG, "onHimReaded");
        if (him == null) {
            return;
        }
        this.him = him;
    }


    @Override
    public void onReceptorConnectionChanged(String state, long lastConnection) {
        Log.d(TAG, "onReceptorConnectionChanged state: " + state + ", lastConnection: " + lastConnection);
        this.lastConnection = lastConnection;
        this.connectionState = state;
        if (textLastConnection != null) {
            if (state.equals(Constants.STATE_ONLINE)) {
                textLastConnection.setText(R.string.state_online);
            } else {
                textLastConnection.setText(Utils.calculateLastConnection(new Date(lastConnection), new Date(), getActivity()));
            }
        }
    }

    @Override
    public void onRoomStateChanged(String state) {
        Log.d(TAG, "onRoomStateChanged state: " + state);
        if (state == null) {
            return;
        }
        chatState = state;

        String lastConnection = "";
        switch (state) {
            case Constants.CHAT_STATE_WAITING:
                lastConnection = getString(R.string.chat_state_waiting);
                break;
            case Constants.CHAT_STATE_PARED:
                lastConnection = getString(R.string.message_automatic_pared);
                //Snackbar.make(toolbar, lastConnection, Snackbar.LENGTH_LONG).show();
                break;
        }

        if (state.equals(androidID + Constants.CHAT_STATE_BLOCK)) {
            lastConnection = getString(R.string.message_automatic_blocked);
            invalidateOptionsMenu();
        }
        if (state.contains(Constants.CHAT_STATE_UNBLOCKED)) {
            lastConnection = getString(R.string.message_automatic_unblocked);
            invalidateOptionsMenu();
        }


        textLastConnection.setText(lastConnection);
    }

    @Override
    public void onRoomActionChanged(String action) {
        Log.d(TAG, "onRoomActionChanged action: " + action);

        if (him == null || me == null) {
            return;
        }

        if (action.equals(him.getKeyDevice() + Constants.CHAT_STATE_WRITING)) {
            textLastConnection.setText(R.string.chat_state_writing);
        } else if (action.equals(Constants.CHAT_STATE_NO_ACTION)) {
            if (connectionState.equals(Constants.STATE_ONLINE)) {
                textLastConnection.setText(R.string.state_online);
            } else {
                textLastConnection.setText(Utils.calculateLastConnection(new Date(lastConnection), new Date(), getActivity()));
            }
        }
    }

    @Override
    public void onRoomHot(boolean isHot) {
        Log.d(TAG, "onRoomHot: " + isHot);
        this.isHot = isHot;
        invalidateOptionsMenu();

        //String message = isHot ? getString(R.string.action_hot) : getString(R.string.error);
        /*
        if (isHot){
            Snackbar.make(sizeNotifierRelativeLayout, getString(R.string.action_hot), Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            firebaseRoom.hot();
                        }
                    }).show();
        }*/
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int ID = isHot ? R.drawable.ic_whatshot_red_24dp : R.drawable.ic_whatshot_white_24dp;
        menu.findItem(R.id.action_hot).setIcon(ContextCompat.getDrawable(getActivity(), ID));

        int titleBlock = R.string.action_block;

        if (chatState.contains(Constants.CHAT_STATE_BLOCK)) {
            titleBlock = R.string.action_unblock;
        }

        if (chatState.contains(Constants.CHAT_STATE_UNBLOCKED)) {
            titleBlock = R.string.action_block;
        }

        menu.findItem(R.id.action_block).setTitle(titleBlock);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onChatMessageListener(ChatMessage chatMessage) {
        switch (chatMessage.getMessageType()){
            case Constants.MESSAGE_IMAGE:
                Intent intent = new Intent(getActivity(), FullImageActivity.class);
                intent.setData(Uri.parse(chatMessage.getMessageText()));
                startActivity(intent);
                break;

        }
    }
}
