package apps.steve.fire.randomchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.steve.fire.randomchat.adapters.HistorialChatAdapter;
import apps.steve.fire.randomchat.interfaces.OnChatItemClickListener;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.RandomChat;

public class Historial extends AppCompatActivity implements OnChatItemClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference   historialReference;
    private String android_id;
    private String TAG = Historial.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android_id = Utils.getAndroidID(this);
        Log.d(TAG, "ANDROID ID: " + android_id);
        firebaseDatabase = FirebaseDatabase.getInstance();
        historialReference = firebaseDatabase.getReference(Constants.CHILD_USERS).child(android_id).child(Constants.CHILD_USERS_HISTO_CHATS);

        historialReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "DataSnapshot COUNT: "+ dataSnapshot.getChildrenCount());

                List<HistorialChat> list = new ArrayList<HistorialChat>();
                if (dataSnapshot.getChildrenCount()>0){
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        Log.d(TAG, "CHILDREN KEY: "+ postSnapshot.getKey());

                        HistorialChat historialChat = postSnapshot.getValue(HistorialChat.class);
                        Log.d(TAG, historialChat.getKeyChat());
                        list.add(historialChat);
                    }
                }
                setRecyclerView(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Historial.this, "DatabaseError: "+ databaseError, Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setRecyclerView(List<HistorialChat> listHistorialChats){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
/*
        HistorialChatAdapter historialChatAdapter = new HistorialChatAdapter(
                listHistorialChats, getApplicationContext(), android_id,
                this);*/
        //recyclerView.setAdapter(historialChatAdapter);
    }


    private void updateLikedStatus(HistorialChat item, final String typeLiked, final boolean state, final View view, final String gender){

        DatabaseReference mainRef = firebaseDatabase.getReference();
        //.getReference(Constants.CHILD_USERS).child(android_id).child(Constants.CHILD_USERS_HISTO_CHATS);
        Map<String, Object> likedStatus = new HashMap<>();
        String pathToLike = "/"+ Constants.CHILD_USERS+"/"+android_id+"/"+Constants.CHILD_USERS_HISTO_CHATS +"/"+item.getKeyChat()+"/"+typeLiked;
        Log.d(TAG, "pathToLike: "+ pathToLike);
        String pathToUserTrend = "/"+ Constants.CHILD_TRENDS+"/"+item.getEmisor().getKeyDevice();
        Log.d(TAG, "pathToUserTrend: "+ pathToUserTrend);
        likedStatus.put(pathToLike, state);
        likedStatus.put(pathToUserTrend + "/edad", item.getEmisor().getEdad());
        likedStatus.put(pathToUserTrend + "/genero", item.getEmisor().getGenero());
        likedStatus.put(pathToUserTrend + "/keyDevice", item.getEmisor().getKeyDevice());

        likedStatus.put(pathToUserTrend + "/"+typeLiked+"/"+item.getMe().getKeyDevice()+"/liked", state);
            likedStatus.put(pathToUserTrend + "/"+typeLiked+"/"+item.getMe().getKeyDevice()+"/edad", item.getMe().getEdad());
            likedStatus.put(pathToUserTrend + "/"+typeLiked+"/"+item.getMe().getKeyDevice()+"/genero", item.getMe().getGenero());
            likedStatus.put(pathToUserTrend + "/"+typeLiked+"/"+item.getMe().getKeyDevice()+"/keyDevice", item.getMe().getKeyDevice());

        mainRef.updateChildren(likedStatus, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null){
                    Snackbar.make(view,
                            String.format(getResources().getString(R.string.historial_action_error), Utils.capitalize(typeLiked)),
                            Snackbar.LENGTH_SHORT)
                            .show();
                }else {
                    if (state){
                        Snackbar.make(
                                view,
                                String.format(
                                        getResources().getString(R.string.historial_action_like),
                                        Utils.capitalize(typeLiked)),
                                Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });
    }

    private AppCompatActivity getActivity(){
        return this;
    }

    private void launchChatActivity(RandomChat item, String start_type) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(getActivity(), ChatActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("key_random", item.getKeyChat());
        i.putExtra("start_type", start_type);
        // brings up the second activity
        startActivity(i);
    }

    @Override
    public void onChatItemClicked(RandomChat item, View view, boolean liked) {
        launchChatActivity(item, Constants._HERE);
    }
}
