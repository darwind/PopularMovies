package it.rugaard.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.rugaard.popularmovies.communication.MoviesAsyncTaskLoader;
import it.rugaard.popularmovies.models.Movie;

public class PopularMoviesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, MoviesRecyclerAdapter.OnMovieClickListener {
    private static final int SORTING_POPULARITY = 0;
    private static final int SORTING_RELEASE_YEAR = 1;
    private static final int SORTING_HIGHEST_RATED = 2;

    private static final String CURRENT_SORTING = "currentSorting";

    private MoviesRecyclerAdapter moviesRecyclerAdapter;
    private View progressBar;
    private TextView infoTextView;

    private int currentSorting = SORTING_POPULARITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies_list);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        }

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.number_of_rows));
        recyclerView.setLayoutManager(gridLayoutManager);
        moviesRecyclerAdapter = new MoviesRecyclerAdapter(this);
        recyclerView.setAdapter(moviesRecyclerAdapter);
        progressBar = findViewById(R.id.progressBar);
        infoTextView = (TextView) findViewById(R.id.infoTextView);

        if (savedInstanceState != null) {
            currentSorting = savedInstanceState.getInt(CURRENT_SORTING, SORTING_POPULARITY);
        }

        getSupportLoaderManager().initLoader(currentSorting, null, this).forceLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popularity:
                currentSorting = SORTING_POPULARITY;
                getSupportLoaderManager().initLoader(SORTING_POPULARITY, null, this).forceLoad();
                return true;
            case R.id.releaseYear:
                currentSorting = SORTING_RELEASE_YEAR;
                getSupportLoaderManager().initLoader(SORTING_RELEASE_YEAR, null, this).forceLoad();
                return true;
            case R.id.highestRated:
                currentSorting = SORTING_HIGHEST_RATED;
                getSupportLoaderManager().initLoader(SORTING_HIGHEST_RATED, null, this).forceLoad();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_SORTING, currentSorting);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        moviesRecyclerAdapter.updateData(new ArrayList<Movie>());
        toggleLoadingView(true, false);
        switch (id) {
            case SORTING_POPULARITY:
                return new MoviesAsyncTaskLoader(this, MoviesAsyncTaskLoader.POPULARITY);
            case SORTING_RELEASE_YEAR:
                return new MoviesAsyncTaskLoader(this, MoviesAsyncTaskLoader.RELEASE_YEAR);
            case SORTING_HIGHEST_RATED:
                return new MoviesAsyncTaskLoader(this, MoviesAsyncTaskLoader.HIGHEST_RATED);
            default:
                throw new UnsupportedOperationException("Unknown id passed");
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        moviesRecyclerAdapter.updateData(data);
        toggleLoadingView(false, data.isEmpty());
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        moviesRecyclerAdapter.updateData(new ArrayList<Movie>());
        toggleLoadingView(true, false);
    }

    private void toggleLoadingView(boolean show, boolean showEmptyText) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        if (showEmptyText) {
            infoTextView.setVisibility(View.VISIBLE);
            infoTextView.setText(getString(R.string.info_text_no_items));
        } else {
            infoTextView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            infoTextView.setText(getString(R.string.loading_text));
        }
    }

    @Override
    public void onClick(View view, Movie movie) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(DetailActivity.DETAIL_PARCELABLE, movie);
        startActivity(detailIntent);
    }
}