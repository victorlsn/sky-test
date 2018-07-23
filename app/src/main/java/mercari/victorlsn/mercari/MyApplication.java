package mercari.victorlsn.mercari;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import mercari.victorlsn.mercari.broadcast.NetworkReceiver;

/**
 * Created by victorlsn on 23/07/18.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private boolean useCache = false;

    public boolean shouldUseCache() {
        return useCache;
    }



    public MyApplication() {
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(new NetworkReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}
