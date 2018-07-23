package mercari.victorlsn.mercari.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import mercari.victorlsn.mercari.beans.Category;
import mercari.victorlsn.mercari.beans.ErrorResponse;
import mercari.victorlsn.mercari.data.Controller;
import mercari.victorlsn.mercari.interfaces.CategoriesMVP;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by victorlsn on 23/07/18.
 */

public class CategoriesModelImp implements CategoriesMVP.Model{
    private CategoriesMVP.Presenter presenter;

    public CategoriesModelImp(CategoriesMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getCategories() {
        Controller.getInstance().doApiCall().getUrls().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (null != response.body()) {
                    List<Category> categories = response.body();
                    presenter.requestCategoriesSuccessfully(categories);
                }
                else {
                    try {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        presenter.requestCategoriesFailure(errorResponse.getError().getMessage());
                    }
                    catch (Exception e) {
                        Log.e("Exception: ", e.getLocalizedMessage());
                        presenter.requestCategoriesFailure("Error requesting data. Please check your connection or try again later");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, Throwable t) {
                presenter.requestCategoriesFailure(t.getMessage());
            }
        });
    }
}
