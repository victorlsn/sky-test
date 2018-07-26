package sky.victorlsn.test.ui.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by victorlsn on 24/07/18.
 */

public class MyProgressDialog extends ProgressDialog {

    private static MyProgressDialog instance = null;

    public MyProgressDialog(Context context) {
        super(context);
    }

    public static MyProgressDialog getInstance(Context context) {
        if (instance == null) {
            instance = new MyProgressDialog(context);
        }

        return instance;
    }
}
