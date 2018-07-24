package mercari.victorlsn.mercari.ui.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import mercari.victorlsn.mercari.MyApplication;
import mercari.victorlsn.mercari.data.Controller;

/**
 * Created by victorlsn on 24/07/18.
 */

public class MyProgressDialog extends ProgressDialog {

    private static MyProgressDialog instance = null;

    public static MyProgressDialog getInstance(Context context) {
        if (instance == null) {
            instance = new MyProgressDialog(context);
        }

        return instance;
    }

    public MyProgressDialog(Context context) {
        super(context);
    }
}
