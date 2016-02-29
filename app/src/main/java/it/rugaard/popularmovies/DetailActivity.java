package it.rugaard.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import it.rugaard.popularmovies.models.Movie;
import it.rugaard.popularmovies.models.MovieImage;

public class DetailActivity extends AppCompatActivity {
    public static final String DETAIL_PARCELABLE = "detailParcelable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final Movie movie = getIntent().getParcelableExtra(DETAIL_PARCELABLE);
        setTitle(movie.getTitle());

        Glide.with(this)
                .load(new MovieImage(movie.getBackdropPath())
                        .withWidth(MovieImage.W_780)
                        .toUri())
                .into((ImageView) findViewById(R.id.backdropImageView));

        ((TextView) findViewById(R.id.synopsisTextView)).setText(movie.getOverview());
        ((TextView) findViewById(R.id.userRatingTextView)).setText(String.valueOf(movie.getVoteAverage()));
        ((TextView) findViewById(R.id.releaseDateTextView)).setText(movie.getReleaseDate());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}