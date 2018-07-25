package mercari.victorlsn.mercari.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import mercari.victorlsn.mercari.MyApplication;
import mercari.victorlsn.mercari.R;
import mercari.victorlsn.mercari.beans.Category;
import mercari.victorlsn.mercari.enums.SortEnum;
import mercari.victorlsn.mercari.event.NetworkConnectedEvent;
import mercari.victorlsn.mercari.interfaces.CategoriesMVP;
import mercari.victorlsn.mercari.presenters.CategoriesPresenterImp;
import mercari.victorlsn.mercari.ui.adapters.PageFragmentAdapter;
import mercari.victorlsn.mercari.ui.fragments.ProductsFragment;
import mercari.victorlsn.mercari.util.AppTools;
import mercari.victorlsn.mercari.util.PreferencesUtil;

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
    @BindView(R.id.refresh_layout_error_icon_iv)
    ImageView errorImageView;
    @BindView(R.id.refresh_layout_message_tv)
    TextView errorTextView;
    @BindView(R.id.btn_menu)
    ImageView sortMenuButton;

    @OnClick(R.id.fab)
    public void onClickFab() {
        showToast(getString(R.string.function_not_implemented), Toast.LENGTH_SHORT);
    }

    @OnClick(R.id.btn_menu)
    public void onMenuClick() {
        setupPopupMenu();
    }

    private CategoriesMVP.Presenter presenter;
    private ProgressDialog progressDialog;
    private ArrayList<Category> categories = new ArrayList<>();
    private HashMap<String, String> categoriesHashMap = new HashMap<>();
    private PageFragmentAdapter adapter;
    private SortEnum currentSortingMethod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferencesUtil.setBooleanValue(getString(R.string.showing_dialog), false);

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
        outState.putParcelableArrayList(getString(R.string.categories), categories);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        categories = savedInstanceState.getParcelableArrayList(getString(R.string.categories));

        if(categories != null && categories.size() != 0) {
            for (Category category : categories) {
                categoriesHashMap.put(category.getName(), category.getData());
            }

            setupFragments();
            setupTabClick();
        }
        else {
            presenter.requestCategories();
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
                    case R.id.menu_sort_name:
                        currentSortingMethod = SortEnum.NAME;

                        break;
                    case R.id.menu_sort_status:
                        currentSortingMethod = SortEnum.STATUS;

                        break;
                    case R.id.menu_sort_price:
                        currentSortingMethod = SortEnum.PRICE;

                        break;
                    case R.id.menu_sort_likes:
                        currentSortingMethod = SortEnum.LIKES;

                        break;
                    case R.id.menu_sort_comments:
                        currentSortingMethod = SortEnum.COMMENTS;
                        break;

                    case R.id.menu_sort_default:
                        currentSortingMethod = SortEnum.DEFAULT;
                        break;
                }

                for (int i = 0; i < adapter.getCount(); i++) {
                    ProductsFragment fragment = (ProductsFragment) adapter.getItem(i);
                    fragment.setAdapterSortingMethod(currentSortingMethod);
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
            presenter = new CategoriesPresenterImp();
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

        actionBar.setTitle("Mercari");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.icon_launcher);
    }

    /**
     * This method setups the fragments and its adapter, depending on whether the app should use fixed or dynamic categories.
     * @see MyApplication#shouldUseFixedCategories()
     */
    private void setupFragments() {
        boolean shouldUseFixedCategories = MyApplication.getInstance().shouldUseFixedCategories();

         adapter = new PageFragmentAdapter(getSupportFragmentManager());

        // If the app should use fixed/pre-ordered categories, this part of the code generates them manually.
        if (shouldUseFixedCategories) {

            ProductsFragment menFragment = new ProductsFragment();
            Bundle men = new Bundle();
            men.putString(getString(R.string.category), getString(R.string.men_category));
            men.putString(getString(R.string.category_url), categoriesHashMap.get(getString(R.string.men_category)));
            menFragment.setArguments(men);
            adapter.addFragment(menFragment, getString(R.string.men_category));


            ProductsFragment allFragment = new ProductsFragment();
            Bundle all = new Bundle();
            all.putString(getString(R.string.category), getString(R.string.all_category));
            all.putString(getString(R.string.category_url), categoriesHashMap.get(getString(R.string.all_category)));
            allFragment.setArguments(all);
            adapter.addFragment(allFragment, getString(R.string.all_category));

            ProductsFragment womenFragment = new ProductsFragment();
            Bundle women = new Bundle();
            women.putString(getString(R.string.category), getString(R.string.women_category));
            women.putString(getString(R.string.category_url), categoriesHashMap.get("Woman"));
            womenFragment.setArguments(women);
            adapter.addFragment(womenFragment, getString(R.string.women_category));

        }

        // If the app shouldn't use fixed/pre-ordered categories, this part of the code generates them automatically from the JSON received via API.
        else {
            for (Category category : categories) {
                ProductsFragment fragment = new ProductsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.category), category.getName());
                bundle.putString(getString(R.string.category_url), category.getData());
                fragment.setArguments(bundle);

                adapter.addFragment(fragment, category.getName());
            }
        }

        viewPager.setAdapter(adapter);

        setupTabLayout(adapter);
    }

    /**
     * This method setups the Tabs of the ViewPager
     */
    private void setupTabLayout(PageFragmentAdapter adapter) {
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < adapter.getmFragmentTitles().size(); i++) {
            tabLayout.getTabAt(i).setText(adapter.getTitle(i));
        }
    }

    /**
     * This method setups the Clicks on the Tabs of the ViewPager
     */
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
        boolean isShowing = PreferencesUtil.checkBoolean(getString(R.string.showing_dialog));
        if(show && !isShowing){
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getString(R.string.dialog_wait));
            progressDialog.setMessage(getString(R.string.dialog_retrieving));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            PreferencesUtil.setBooleanValue(getString(R.string.showing_dialog), true);
        }else {
            if(progressDialog != null && isShowing){
                progressDialog.dismiss();

                PreferencesUtil.setBooleanValue(getString(R.string.showing_dialog), false);
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

        for (Category category : categories) {
            categoriesHashMap.put(category.getName(), category.getData());
        }

        errorImageView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);

        setupFragments();
        setupTabClick();
    }

    @Override
    public void receiveCategoriesFailure() {
        errorImageView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onEvent(NetworkConnectedEvent event) {
        initPresenter();
        if((categories == null || categories.size() == 0) && (progressDialog == null || !progressDialog.isShowing())) {
            presenter.requestCategories();
        }
    }
}
