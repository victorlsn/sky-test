package sky.victorlsn.test.interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import sky.victorlsn.test.beans.Movie;

/**
 * Created by victorlsn on 26/07/18.
 */

public interface AppRestEndPoint {

    @Headers("Content-Type: application/json")
    @GET("Movies")
    Call<List<Movie>> getMovies();
}
