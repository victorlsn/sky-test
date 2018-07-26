package sky.victorlsn.test.ui.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import sky.victorlsn.test.R;
import sky.victorlsn.test.beans.Movie;
import sky.victorlsn.test.util.AppTools;

/**
 * Created by victorlsn on 26/07/18.
 */

public class MovieDetailActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cover_iv)
    ImageView coverIv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.synopsis_tv)
    TextView synopsisTv;
    @BindView(R.id.duration_tv)
    TextView durationTv;
    @BindView(R.id.release_year_tv)
    TextView releaseYearTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        ViewCompat.setTransitionName(coverIv, "TRANSITION");

        Movie movie = getIntent().getParcelableExtra("movie");

        if (movie != null) {
            Glide.with(this).load(movie.getCoverUrl()).apply(new RequestOptions().placeholder(R.drawable.image_not_found)).into(coverIv);
            titleTv.setText(movie.getTitle());
            synopsisTv.setText(String.format("Sinopse: %s", movie.getOverview()));
            durationTv.setText(String.format("Duração: %s", AppTools.standardTimeFormat(movie.getDuration())));
            releaseYearTv.setText(String.format("Ano: %s", movie.getReleaseYear()));
            prepareActionBar(movie.getTitle());
        }
    }

    /**
     * This method initializes the ActionBar.
     */
    private void prepareActionBar(String title) {
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbarColor)));

        actionBar.setTitle(title);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.icon_launcher);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
