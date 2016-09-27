package apps.steve.fire.randomchat.firebase;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import apps.steve.fire.randomchat.Constants;
import apps.steve.fire.randomchat.interfaces.OnChatsListener;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.RandomChat;

/**
 * Created by Steve on 29/08/2016.
 */

public class FirebaseChats {

    private static final String TAG = FirebaseChats.class.getSimpleName();
    private String androidID;
    private OnChatsListener listener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference chatsReference;


    public FirebaseChats(String androidID, OnChatsListener listener) {
        this.androidID = androidID;
        this.listener = listener;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.chatsReference = firebaseDatabase.getReference(Constants.CHILD_USERS).child(androidID).child(Constants.CHILD_USERS_HISTO_CHATS);
        listenChats();
    }


    private void listenChats(){
        chatsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "DataSnapshot COUNT: "+ dataSnapshot.getChildrenCount());

                List<RandomChat> list = new ArrayList<>();
                if (dataSnapshot.getChildrenCount()>0){
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        Log.d(TAG, "CHILDREN KEY: "+ postSnapshot.getKey());

                        RandomChat chat = postSnapshot.getValue(RandomChat.class);
                        Log.d(TAG, chat.getKeyChat());
                        list.add(chat);
                    }
                }
                listener.onChatChangedListener(true, list);
                //setRecyclerView(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onChatChangedListener(false, null);
                //Toast.makeText(Historial.this, "DatabaseError: "+ databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }




}
