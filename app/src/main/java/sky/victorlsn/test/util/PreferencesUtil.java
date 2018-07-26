package sky.victorlsn.test.util;

import android.preference.PreferenceManager;

import sky.victorlsn.test.MyApplication;

/**
 * Created by victorlsn on 24/07/18.
 */

public class PreferencesUtil {

    public static boolean checkBoolean(String key) {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).getBoolean(key, false);
    }

    public static void setBooleanValue(String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()).edit().putBoolean(key, value).commit();
    }
}
