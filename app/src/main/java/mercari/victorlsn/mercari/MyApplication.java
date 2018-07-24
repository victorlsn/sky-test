package mercari.victorlsn.mercari;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import mercari.victorlsn.mercari.broadcast.NetworkReceiver;
import mercari.victorlsn.mercari.data.Controller;

/**
 * Created by victorlsn on 23/07/18.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    /**
     * This variable determines whether the app should use Fixed Categories.
     */
    private boolean fixedCategories = true;

    public boolean shouldUseFixedCategories() {
        return fixedCategories;
    }

    public MyApplication() {
        instance = this;
        Controller.getInstance().setUseCache(false);
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
