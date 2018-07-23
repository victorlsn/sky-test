package mercari.victorlsn.mercari.interfaces;

import java.util.List;

import mercari.victorlsn.mercari.beans.Category;
import mercari.victorlsn.mercari.beans.Product;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

/**
 * Created by victorlsn on 23/07/18.
 */

public interface AppRestEndPoint {

    @Headers("Content-Type: application/json")
    @GET("master.json")
    Call<List<Category>> getUrls();

    @Headers("Content-Type: application/json")
    @GET
    Call<List<Product>> getProducts(@Url String url);
}
