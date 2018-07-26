package sky.victorlsn.test;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import sky.victorlsn.test.broadcast.NetworkReceiver;

/**
 * Created by victorlsn on 26/07/18.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    public MyApplication() {
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // This line registers the NetworkReceiver to detect changes on connectivity
        registerReceiver(new NetworkReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}
