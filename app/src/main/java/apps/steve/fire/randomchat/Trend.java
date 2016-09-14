package apps.steve.fire.randomchat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import apps.steve.fire.randomchat.adapters.TrendAdapter;
import apps.steve.fire.randomchat.model.HistorialChat;
import apps.steve.fire.randomchat.model.UserLiked;
import apps.steve.fire.randomchat.model.UserTrend;

public class Trend extends AppCompatActivity implements TrendAdapter.OnUserTrendListener {

    private static final String TAG = Trend.class.getSimpleName();
    private FirebaseDatabase appDatabase;
    private DatabaseReference trendReference;
    private String android_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appDatabase = FirebaseDatabase.getInstance();
        trendReference = appDatabase.getReference(Constants.CHILD_TRENDS);
        android_id = Utils.getAndroidID(this);

        trendReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "dataSnapshot: " +dataSnapshot);
                Log.d(TAG, "getChildrenCount: "+ dataSnapshot.getChildrenCount());

                List<UserTrend> listUsers = new ArrayList<UserTrend>();

                if(dataSnapshot.getChildrenCount()>0){
                    for (DataSnapshot userTrend :dataSnapshot.getChildren()){
                        Log.d(TAG, "DataSnapshot userTrend: " + userTrend);

                        String genero = userTrend.child("genero").getValue(String.class);
                        int edad = userTrend.child("edad").getValue(int.class);
                        String keyDevice = userTrend.child("keyDevice").getValue(String.class);

                        Log.d(TAG, "USER TREND genero: "+ genero);
                        Log.d(TAG, "USER TREND edad: "+ edad);
                        Log.d(TAG, "USER TREND keyDevice: "+ keyDevice);

                        DataSnapshot hotSnapShot = userTrend.child("hot");
                        DataSnapshot starSnapShot = userTrend.child("star");
                        DataSnapshot likeSnapShot = userTrend.child("like");
                        Log.d(TAG, "USER TREND NUMBER OF HOTS LIKED: "+ hotSnapShot.getChildrenCount());

                        List<UserLiked> listHotLikes = new ArrayList<UserLiked>();
                        List<UserLiked> listStarLikes = new ArrayList<UserLiked>();
                        List<UserLiked> listLikeLikes = new ArrayList<UserLiked>();

                        boolean hotedByMe = false;
                        boolean likedByme = false;
                        boolean staredByme = false;
                        for (DataSnapshot hotLikes :hotSnapShot.getChildren()){
                            UserLiked userLiked= hotLikes.getValue(UserLiked.class);
                            listHotLikes.add(userLiked);
                            Log.d(TAG, "LIKE getKeyDevice: "+ userLiked.getKeyDevice());
                            Log.d(TAG, "LIKE getGenero: "+ userLiked.getGenero());
                            Log.d(TAG, "LIKE getEdad: "+ userLiked.getEdad());
                            if (userLiked.getKeyDevice().equals(android_id)){
                                hotedByMe = true;
                            }
                        }
                        for (DataSnapshot  starLikes:starSnapShot.getChildren()){

                            UserLiked userLiked= starLikes.getValue(UserLiked.class);
                            listStarLikes.add(userLiked);
                            Log.d(TAG, "STAR getKeyDevice: "+ userLiked.getKeyDevice());
                            Log.d(TAG, "STAR getGenero: "+ userLiked.getGenero());
                            Log.d(TAG, "STAR getEdad: "+ userLiked.getEdad());
                            if (userLiked.getKeyDevice().equals(android_id)){
                                staredByme = true;
                            }
                        }
                        for (DataSnapshot likeLiked :likeSnapShot.getChildren()){
                            UserLiked userLiked= likeLiked.getValue(UserLiked.class);
                            listLikeLikes.add(userLiked);
                            Log.d(TAG, "LIKE getKeyDevice: "+ userLiked.getKeyDevice());
                            Log.d(TAG, "LIKE getGenero: "+ userLiked.getGenero());
                            Log.d(TAG, "LIKE getEdad: "+ userLiked.getEdad());
                            if (userLiked.getKeyDevice().equals(android_id)){
                                likedByme = true;
                            }
                        }

                        UserTrend user = new UserTrend(edad, genero, keyDevice, listLikeLikes, listStarLikes, listHotLikes, likedByme, hotedByMe, staredByme);
                        listUsers.add(user);


                    }
                    setRecyclerView(listUsers);

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "databaseError: "+ databaseError);

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

    private void setRecyclerView(List<UserTrend> listTrends){
        RecyclerView rvTrend = (RecyclerView) findViewById(R.id.rvTrend);
        rvTrend.setLayoutManager(new LinearLayoutManager(this));
        rvTrend.setAdapter(new TrendAdapter(listTrends, this, this));
    }

    @Override
    public void onUserTrendListener(UserTrend item, View view, boolean liked) {

    }
}
