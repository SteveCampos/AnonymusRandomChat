package apps.steve.fire.randomchat.activities;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Steve on 3/11/2016.
 */

public class MyApplication extends Application {

    public MyApplication() {
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //SugarContext.terminate();
    }
}
