package sky.victorlsn.test.data;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import sky.victorlsn.test.MyApplication;
import sky.victorlsn.test.interfaces.AppRestEndPoint;
import sky.victorlsn.test.util.AppTools;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by victorlsn on 26/07/18.
 * This class implements the single instance controller used for making API calls.
 */
public class Controller {

    private static Controller instance = null;
    private AppRestEndPoint apiCall;

    /**
     * This variable determines whether the app should use Cache for HTTP Calls.
     */
    private boolean useCache = true;

    private Controller() {
        File cacheDir = null;
        if (shouldUseCache()) {
            cacheDir = MyApplication.getInstance().getApplicationContext().getCacheDir();
        }

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient
                .Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS);

        if (shouldUseCache()) {
            okHttpBuilder.cache(new Cache(cacheDir, 10 * 1024 * 1024)) // 10 MB
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Request request = chain.request();
                            if (AppTools.isOnline()) {
                                request = request.newBuilder().header("Cache-Control", "public, max-age=" + 31536000).build();
                            } else {
                                request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 31536000).build();
                            }
                            return chain.proceed(request);
                        }
                    });
        }

        OkHttpClient client = okHttpBuilder.build();

        String BASE_URL = "https://sky-exercise.herokuapp.com/api/";

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiCall = retrofit.create(AppRestEndPoint.class);
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }

        return instance;
    }

    public boolean shouldUseCache() {
        return useCache;
    }

    public AppRestEndPoint doApiCall() {
        return apiCall;
    }
}
