package mercari.victorlsn.mercari.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import mercari.victorlsn.mercari.MyApplication;
import mercari.victorlsn.mercari.R;

/**
 * Created by victorlsn on 23/07/18.
 */

public class AppTools {

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) MyApplication.getInstance().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean showToast(Context context, String message, int duration){
        if(null == context) return false;
        if(null == message || message.isEmpty()) return false;

        if(duration != Toast.LENGTH_SHORT && duration != Toast.LENGTH_LONG) return false;

        Toast.makeText(context, message, duration).show();

        return false;
    }


    public static int getGridSpanCount(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth  = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.recycler_item_size);
        return Math.round(screenWidth / cellWidth);
    }
}
