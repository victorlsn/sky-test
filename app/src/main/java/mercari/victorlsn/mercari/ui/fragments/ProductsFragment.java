package mercari.victorlsn.mercari.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import mercari.victorlsn.mercari.R;
import mercari.victorlsn.mercari.beans.Product;
import mercari.victorlsn.mercari.enums.SortEnum;
import mercari.victorlsn.mercari.event.NetworkConnectedEvent;
import mercari.victorlsn.mercari.interfaces.ProductsMVP;
import mercari.victorlsn.mercari.presenters.ProductsPresenterImp;
import mercari.victorlsn.mercari.ui.activities.ImageViewerActivity;
import mercari.victorlsn.mercari.ui.adapters.GridSpacingItemDecoration;
import mercari.victorlsn.mercari.ui.adapters.ProductAdapter;
import mercari.victorlsn.mercari.util.AppTools;
import mercari.victorlsn.mercari.util.PreferencesUtil;

/**
 * Created by victorlsn on 23/07/18.
 */

public class ProductsFragment extends BaseFragment implements ProductsMVP.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.error_layout_icon_iv)
    ImageView errorImageView;
    @BindView(R.id.error_layout_message_tv)
    TextView errorTextView;

    private ProductsMVP.Presenter presenter;
    private ProgressDialog progressDialog;
    private String category;
    private String categoryUrl;
    private ArrayList<Product> products = new ArrayList<>();
    private boolean isVisibleToUser = false;
    private ProductAdapter adapter;
    private SortEnum currentSortingMethod;

    @Override
    protected int layoutToInflate() {
        return R.layout.products_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            category = bundle.getString(getString(R.string.category), "");
            categoryUrl = bundle.getString(getString(R.string.category_url), "");
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(getString(R.string.category), category);
        outState.putString(getString(R.string.category_url), categoryUrl);
        outState.putParcelableArrayList(getString(R.string.products), products);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            category = savedInstanceState.getString(getString(R.string.category));
            products = savedInstanceState.getParcelableArrayList(getString(R.string.products));
            categoryUrl = savedInstanceState.getString(getString(R.string.category_url));
        }

        if (products != null && products.size() != 0) {
            configAdapter(products);
        } else if (categoryUrl != null) {
            presenter.requestProducts(categoryUrl);
        }
    }

    public void setAdapterSortingMethod(SortEnum sortingMethod) {
        currentSortingMethod = sortingMethod;
        if (adapter != null) {
            adapter.sortBy(sortingMethod);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        configAdapter(products);
        initPresenter();
    }

    @Subscribe
    public void onEvent(NetworkConnectedEvent event) {
        if (getView() != null) {
            initPresenter();
            if (isVisibleToUser) {
                if (products.size() == 0) {
                    presenter.requestProducts(categoryUrl);
                }
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;

        if (getView() != null) {
            initPresenter();

            if (isVisibleToUser) {
                if (products.size() == 0) {
                    presenter.requestProducts(categoryUrl);
                }
            }
        }
    }


    private void initPresenter() {
        if (null == presenter) {
            presenter = new ProductsPresenterImp();
            presenter.attachView(this);
        }
    }

    /**
     * This method setups the RecyclerView used for displaying products.
     */
    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), AppTools.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(AppTools.getGridSpanCount(getActivity()), 16, true));
        }
    }

    /**
     * This method setups the ProductAdapter with the products received via API
     */
    private void configAdapter(List<Product> products) {
        adapter = new ProductAdapter(getActivity(), products);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ProductAdapter.onItemClickListener() {
            @Override
            public void onItemClickListener(View view, String url) {
                Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
                intent.putExtra("imageUrl", url);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), view, "TRANSITION");
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
            }
        });
        adapter.sortBy(currentSortingMethod);
    }

    @Override
    public void showProgressBar(boolean show) {
        boolean isShowing = PreferencesUtil.checkBoolean(getString(R.string.showing_dialog));
        if (show && !isShowing) {
            progressDialog = new ProgressDialog(getActivity());
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

    @Override
    public void showToast(String message, int duration) {
        AppTools.showToast(getActivity(), message, duration);
    }

    /**
     * This method is setups the screen in response to a successful API call.
     */
    @Override
    public void receiveProductsSuccessfully(List<Product> products) {
        errorImageView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);

        if (recyclerView != null) {
            configAdapter(products);
        }

        this.products = new ArrayList<>(products);
    }

    /**
     * This method is setups the screen in response to a unsuccessful API call.
     */
    @Override
    public void receiveProductsFailure() {
        errorImageView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }
}
