package sky.victorlsn.test.interfaces;

/**
 * Created by victorlsn on 26/07/18.
 */

public interface BaseMVP {
    interface Presenter {
        boolean attachView(BaseMVP.View view);
    }

    interface View {
        void showProgressBar(boolean show);


        void showToast(String message, int duration);
    }
}
