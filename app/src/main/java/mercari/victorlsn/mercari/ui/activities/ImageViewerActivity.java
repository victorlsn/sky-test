package mercari.victorlsn.mercari.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import mercari.victorlsn.mercari.R;

/**
 * Created by victorlsn on 23/07/18.
 */

public class ImageViewerActivity extends BaseActivity {
    @BindView(R.id.image_view)
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer_activity);

        ViewCompat.setTransitionName(imageView, "TRANSITION");

        String url = getIntent().getStringExtra("imageUrl");
        Glide.with(this).load(url).apply(new RequestOptions().placeholder(R.drawable.image_not_found)).into(imageView);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
