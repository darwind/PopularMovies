package it.rugaard.popularmovies.communication;

import android.content.Context;
import android.support.annotation.StringDef;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import it.rugaard.popularmovies.models.Movie;
import it.rugaard.popularmovies.models.Page;

/**
 * Created by Kasper on 28/02/16.
 */
public class MoviesAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({POPULARITY, RELEASE_YEAR, HIGHEST_RATED})
    public @interface SortBy {}

    public static final String POPULARITY = "popularity.desc";
    public static final String RELEASE_YEAR = "release_date.desc";
    public static final String HIGHEST_RATED = "vote_average.desc";

    private @SortBy String sorting;

    public MoviesAsyncTaskLoader(Context context, @SortBy String sorting) {
        super(context);
        this.sorting = sorting;
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> movies;
        try {
            OkHttpClient httpClient = new OkHttpClient();

            // FIXME : Insert your own API key below.
            Request request = new Request
                    .Builder()
                    .url("http://api.themoviedb.org/3/discover/movie?sort_by=" + sorting + "&api_key=[insert API key here]&page=1")
                    .build();

            Response response = httpClient.newCall(request).execute();
            Page page = new Gson().fromJson(response.body().string(), Page.class);
            movies = page.getResults();
        } catch (IOException e) {
            Log.e(MoviesAsyncTaskLoader.class.getSimpleName(), e.toString());
            movies = new ArrayList<>(); // Returning empty list if something went wrong.
        }
        return movies;
    }
}