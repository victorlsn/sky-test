package sky.victorlsn.test.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import sky.victorlsn.test.R;
import sky.victorlsn.test.beans.Movie;
import sky.victorlsn.test.enums.SortEnum;
import sky.victorlsn.test.event.NetworkConnectedEvent;
import sky.victorlsn.test.interfaces.MoviesMVP;
import sky.victorlsn.test.presenters.MoviesPresenterImp;
import sky.victorlsn.test.ui.adapters.GridSpacingItemDecoration;
import sky.victorlsn.test.ui.adapters.MovieAdapter;
import sky.victorlsn.test.util.AppTools;
import sky.victorlsn.test.util.PreferencesUtil;

/**
 * Created by victorlsn on 26/07/18.
 */

public class MoviesActivity extends BaseActivity implements MoviesMVP.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.error_layout)
    ConstraintLayout errorLayout;
    @BindView(R.id.btn_menu)
    ImageView sortMenuButton;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private MoviesMVP.Presenter presenter;
    private ProgressDialog progressDialog;
    private ArrayList<Movie> movies = new ArrayList<>();
    private MovieAdapter adapter;
    private boolean isStartingActivity = false;

    @OnClick(R.id.btn_menu)
    public void onMenuClick() {
        setupPopupMenu();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferencesUtil.setBooleanValue(getString(R.string.showing_dialog), false);

        init();

        if (savedInstanceState == null && (movies == null || movies.size() == 0) && (progressDialog == null || !progressDialog.isShowing())) {
            presenter.requestMovies();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        isStartingActivity = false;
    }

    private void init() {
        setContentView(R.layout.movies_activity);
        initPresenter();

        initRecyclerView();
        configAdapter(movies);
        initSwipeRefreshLayout();
        prepareActionBar();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * This method setups the ProductAdapter with the products received via API
     */
    private void configAdapter(List<Movie> movies) {
        adapter = new MovieAdapter(this, movies);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MovieAdapter.onItemClickListener() {
            @Override
            public void onItemClickListener(View view, Movie movie) {
                Intent intent = new Intent(MoviesActivity.this, MovieDetailActivity.class);
                intent.putExtra("movie", movie);

                if (!isStartingActivity) {
                    isStartingActivity = true;
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(MoviesActivity.this, view, "TRANSITION");
                    ActivityCompat.startActivity(MoviesActivity.this, intent, options.toBundle());
                }
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, AppTools.getGridSpanCount(this));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(AppTools.getGridSpanCount(this), 16, true));
        }
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // As this is a update, it should not rely on cache, so the call is only made when there's internet available.
                if (AppTools.isOnline()) {
                    presenter.requestMovies();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    showToast(getString(R.string.connection_error), Toast.LENGTH_SHORT);
                }

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.movies), movies);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        movies = savedInstanceState.getParcelableArrayList(getString(R.string.movies));

        if (movies == null || movies.size() == 0) {
            presenter.requestMovies();
        }
    }


    private void setupPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, sortMenuButton);
        popupMenu.inflate(R.menu.sort_menu);
        popupMenu.setGravity(Gravity.END);
        popupMenu.show();
        setMenuActions(popupMenu);
    }

    private void setMenuActions(final PopupMenu menu) {
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_sort_title:
                        adapter.sortBy(SortEnum.TITLE);

                        break;
                    case R.id.menu_sort_duration:
                        adapter.sortBy(SortEnum.DURATION);

                        break;
                    case R.id.menu_sort_release_year:
                        adapter.sortBy(SortEnum.RELEASE_YEAR);

                    case R.id.menu_sort_default:
                        adapter.sortBy(SortEnum.DEFAULT);
                        break;
                }

                return false;

            }
        });
    }

    /**
     * This method initializes the current presenter.
     */
    private void initPresenter() {
        if (null == presenter) {
            presenter = new MoviesPresenterImp();
            presenter.attachView(this);
        }
    }

    /**
     * This method initializes the ActionBar.
     */
    private void prepareActionBar() {
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbarColor)));

        actionBar.setTitle(" CINE Sky");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.icon_launcher);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (!swipeRefreshLayout.isRefreshing()) {
            boolean isShowing = PreferencesUtil.checkBoolean(getString(R.string.showing_dialog));
            if (show && !isShowing) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle(getString(R.string.dialog_wait));
                progressDialog.setMessage(getString(R.string.dialog_retrieving));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();

                PreferencesUtil.setBooleanValue(getString(R.string.showing_dialog), true);
            } else {
                if (progressDialog != null && isShowing) {
                    progressDialog.dismiss();

                    PreferencesUtil.setBooleanValue(getString(R.string.showing_dialog), false);
                }
            }
        }
    }

    @Override
    public void showToast(String message, int duration) {
        AppTools.showToast(this, message, duration);
    }


    @Override
    public void receiveMoviesSuccessfully(List<Movie> movies) {
        this.movies = new ArrayList<>(movies);
        errorLayout.setVisibility(View.GONE);

        configAdapter(movies);

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            showToast(getString(R.string.filmes_atualizados), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void receiveMoviesFailure() {
        errorLayout.setVisibility(View.VISIBLE);

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            showToast(getString(R.string.filmes_atualizados), Toast.LENGTH_SHORT);
        }
    }

    @Subscribe
    public void onEvent(NetworkConnectedEvent event) {
        initPresenter();
        if ((movies == null || movies.size() == 0) && (progressDialog == null || !progressDialog.isShowing())) {
            presenter.requestMovies();
        }
    }
}
