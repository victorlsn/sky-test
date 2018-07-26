package sky.victorlsn.test.presenters;

import android.widget.Toast;

import java.util.List;

import sky.victorlsn.test.beans.Movie;
import sky.victorlsn.test.interfaces.BaseMVP;
import sky.victorlsn.test.interfaces.MoviesMVP;
import sky.victorlsn.test.models.MoviesModelImp;

/**
 * Created by victorlsn on 26/07/18.
 */

public class MoviesPresenterImp implements MoviesMVP.Presenter {
    private MoviesMVP.View view;
    private MoviesMVP.Model model;

    public MoviesPresenterImp() {
        model = new MoviesModelImp(this);
    }

    @Override
    public boolean attachView(BaseMVP.View view) {
        if (null == view) return false;
        this.view = (MoviesMVP.View) view;

        return true;
    }

    @Override
    public void requestMovies() {
        view.showProgressBar(true);
        model.getMovies();
    }

    @Override
    public void requestMoviesSuccessfully(List<Movie> movies) {
        view.showProgressBar(false);
        view.receiveMoviesSuccessfully(movies);
    }

    @Override
    public void requestMoviesFailure(String message) {
        view.showProgressBar(false);
        if (null == message || message.isEmpty()) return;
        view.showToast(message, Toast.LENGTH_SHORT);
        view.receiveMoviesFailure();
    }
}
