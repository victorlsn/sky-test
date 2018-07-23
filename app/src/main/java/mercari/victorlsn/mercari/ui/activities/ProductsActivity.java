package mercari.victorlsn.mercari.ui.activities;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import mercari.victorlsn.mercari.R;
import mercari.victorlsn.mercari.beans.Category;
import mercari.victorlsn.mercari.event.NetworkConnectedEvent;
import mercari.victorlsn.mercari.interfaces.CategoriesMVP;
import mercari.victorlsn.mercari.presenters.CategoriesPresenterImp;
import mercari.victorlsn.mercari.ui.adapters.PageFragmentAdapter;
import mercari.victorlsn.mercari.ui.fragments.ProductsFragment;
import mercari.victorlsn.mercari.util.AppTools;

/**
 * Created by victorlsn on 23/07/18.
 */

public class ProductsActivity extends BaseActivity implements CategoriesMVP.View {
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar_viewpager)
    Toolbar toolbar;

    private CategoriesMVP.Presenter presenter;
    private ProgressDialog progressDialog;
    private ArrayList<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_activity);
        initPresenter();

        prepareActionBar();

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        if(savedInstanceState == null && (categories == null || categories.size() == 0) && (progressDialog == null || !progressDialog.isShowing())) {
            presenter.requestCategories();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("categories", categories);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        categories = savedInstanceState.getParcelableArrayList("categories");

        if(categories != null && categories.size() != 0) {
            setupFragments();
            setupTabClick();
        }
        else {
            presenter.requestCategories();
        }
    }

    private void initPresenter() {
        if (null == presenter) {
            presenter = new CategoriesPresenterImp();
            presenter.attachView(this);
        }
    }

    private void prepareActionBar() {
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbarColor)));

        actionBar.setTitle("Mercari");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.icon_launcher);
    }

    private void setupFragments() {
        PageFragmentAdapter adapter = new PageFragmentAdapter(getSupportFragmentManager());

        for (Category category : categories) {
            ProductsFragment fragment = new ProductsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.category), category.getName());
            bundle.putString(getString(R.string.category_url), category.getData());
            fragment.setArguments(bundle);

            adapter.addFragment(fragment, category.getName());
        }

        viewPager.setAdapter(adapter);

        setupTabLayout(adapter);
    }

    /**
     * Método responsável por configurar os icones na tab do view pager.
     */
    private void setupTabLayout(PageFragmentAdapter adapter) {
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < adapter.getmFragmentTitles().size(); i++) {
            tabLayout.getTabAt(i).setText(adapter.getmFragmentTitles().get(i));
        }
    }

    private void setupTabClick() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                invalidateOptionsMenu();
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void showProgressBar(boolean show) {
        if(show){
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Retrieving products...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }else {
            if(null != progressDialog && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void showToast(String message, int duration) {
        AppTools.showToast(this, message, duration);
    }


    @Override
    public void receiveCategoriesSuccessfully(List<Category> categories) {
        this.categories = new ArrayList<>(categories);
        setupFragments();
        setupTabClick();
    }

    @Subscribe
    public void onEvent(NetworkConnectedEvent event) {
        initPresenter();
        if((categories == null || categories.size() == 0) && (progressDialog == null || !progressDialog.isShowing())) {
            presenter.requestCategories();
        }
    }
}
