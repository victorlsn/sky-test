package mercari.victorlsn.mercari.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by victorlsn on 23/07/18.
 */

public abstract class BaseFragment extends Fragment {
    protected abstract int layoutToInflate();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutToInflate(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
