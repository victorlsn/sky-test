package sky.victorlsn.test.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sky.victorlsn.test.beans.ErrorResponse;
import sky.victorlsn.test.beans.Movie;
import sky.victorlsn.test.data.Controller;
import sky.victorlsn.test.interfaces.MoviesMVP;

/**
 * Created by victorlsn on 26/07/18.
 */

public class MoviesModelImp implements MoviesMVP.Model {

    private MoviesMVP.Presenter presenter;

    public MoviesModelImp(MoviesMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getMovies() {

        Controller.getInstance().doApiCall().getMovies().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                if (null != response.body()) {
                    List<Movie> movies = response.body();
                    presenter.requestMoviesSuccessfully(movies);
                } else {
                    try {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        presenter.requestMoviesFailure(errorResponse.getError().getMessage());
                    } catch (Exception e) {
                        Log.e("Exception: ", e.getLocalizedMessage());
                        presenter.requestMoviesFailure("Error requesting data. Please check your connection or try again later");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, Throwable t) {
                Log.e("Exception: ", t.getLocalizedMessage());
                presenter.requestMoviesFailure("Error requesting data. Please check your connection or try again later");
            }
        });
    }
}
