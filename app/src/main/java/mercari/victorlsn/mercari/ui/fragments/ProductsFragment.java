package mercari.victorlsn.mercari.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import mercari.victorlsn.mercari.R;
import mercari.victorlsn.mercari.beans.Product;
import mercari.victorlsn.mercari.event.NetworkConnectedEvent;
import mercari.victorlsn.mercari.interfaces.ProductsMVP;
import mercari.victorlsn.mercari.presenters.ProductsPresenterImp;
import mercari.victorlsn.mercari.ui.activities.ImageViewerActivity;
import mercari.victorlsn.mercari.ui.adapters.GridSpacingItemDecoration;
import mercari.victorlsn.mercari.ui.adapters.ProductAdapter;
import mercari.victorlsn.mercari.util.AppTools;

/**
 * Created by victorlsn on 23/07/18.
 */

public class ProductsFragment extends BaseFragment implements ProductsMVP.View{
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ProductsMVP.Presenter presenter;
    private ProgressDialog progressDialog;
    private String category;
    private String categoryUrl;
    private ProductAdapter adapter;
    ArrayList<Product> products = new ArrayList<>();
    private boolean isViewShown = false;
    private boolean isVisibleToUser = false;


    @Override
    protected int layoutToInflate() {
        return R.layout.products_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            category = bundle.getString(getString(R.string.category), "");
            categoryUrl = bundle.getString(getString(R.string.category_url), "");
        }

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("category", category);
        outState.putString("category_url", categoryUrl);
        outState.putParcelableArrayList("products", products);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            category = savedInstanceState.getString("category");
            products = savedInstanceState.getParcelableArrayList("products");
            categoryUrl = savedInstanceState.getString("categoryUrl");
        }

        if(products != null && products.size() != 0) {
            configAdapter(products);
        }
        else if (categoryUrl != null) {
            presenter.requestProducts(categoryUrl);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        configAdapter(products);
        initPresenter();
    }

    @Subscribe
    public void onEvent(NetworkConnectedEvent event) {
        if (getView() != null) {
            isViewShown = true;
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
            isViewShown = true;
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

    private void initRecyclerView(){
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), AppTools.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(AppTools.getGridSpanCount(getActivity()), 16, true));
    }

    private void configAdapter(List<Product> products){
        adapter = new ProductAdapter( getActivity(), products);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ProductAdapter.onItemClickListener() {
            @Override
            public void onItemClickListener(View view, String url) {
                Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
                intent.putExtra("imageUrl", url);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), view, "TRANSITION");
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());            }
        });
    }

    @Override
    public void showProgressBar(boolean show) {
        if(show){
            progressDialog = new ProgressDialog(getActivity());
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
        AppTools.showToast(getActivity(), message, duration);
    }

    @Override
    public void receiveProductsSuccessfully(List<Product> products) {
        if (recyclerView != null) {
            configAdapter(products);
        }
        this.products = new ArrayList<>(products);
    }
}
