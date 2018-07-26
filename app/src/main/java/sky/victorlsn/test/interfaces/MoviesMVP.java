package sky.victorlsn.test.interfaces;

import java.util.List;

import sky.victorlsn.test.beans.Movie;

/**
 * Created by victorlsn on 26/07/18.
 */

public interface MoviesMVP {
    interface Model {
        void getMovies();
    }

    interface Presenter extends BaseMVP.Presenter {
        void requestMovies();

        void requestMoviesSuccessfully(List<Movie> movies);

        void requestMoviesFailure(String message);
    }

    interface View extends BaseMVP.View {
        void receiveMoviesSuccessfully(List<Movie> movies);

        void receiveMoviesFailure();
    }
}
