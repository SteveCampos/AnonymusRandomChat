package apps.steve.fire.randomchat;

import android.app.Application;
import android.os.Handler;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by madhur on 3/1/15.
 */
public class App extends Application {

    private static App Instance;
    public static volatile Handler applicationHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance=this;
        applicationHandler = new Handler(getInstance().getMainLooper());
        NativeLoader.initNativeLibs(App.getInstance());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public static App getInstance()
    {
        return Instance;
    }
}
