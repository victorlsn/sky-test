package mercari.victorlsn.mercari.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import mercari.victorlsn.mercari.beans.ErrorResponse;
import mercari.victorlsn.mercari.beans.Product;
import mercari.victorlsn.mercari.data.Controller;
import mercari.victorlsn.mercari.interfaces.ProductsMVP;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by victorlsn on 23/07/18.
 */

public class ProductsModelImp implements ProductsMVP.Model {

    private ProductsMVP.Presenter presenter;

    public ProductsModelImp(ProductsMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getProducts(final String categoryUrl) {
        //TODO if categoryUrl == null

        Controller.getInstance().doApiCall().getProducts(categoryUrl).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (null != response.body()) {
                    List<Product> products = response.body();
                    presenter.requestProductsSuccessfully(products);
                } else {
                    try {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        presenter.requestProductsFailure(errorResponse.getError().getMessage());
                    } catch (Exception e) {
                        Log.e("Exception: ", e.getLocalizedMessage());
                        presenter.requestProductsFailure("Error requesting data. Please check your connection or try again later");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, Throwable t) {
                presenter.requestProductsFailure(t.getMessage());
            }
        });
    }
}
