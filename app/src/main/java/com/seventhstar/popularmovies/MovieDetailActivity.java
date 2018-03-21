package com.seventhstar.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seventhstar.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    @BindView(R.id.movie_title)
    TextView movieTitle;

    @BindView(R.id.movie_poster)
    ImageView moviePoster;

    @BindView(R.id.movie_rating)
    TextView movieRating;

    @BindView(R.id.movie_popularity)
    TextView moviePopularity;

    @BindView(R.id.movie_year)
    TextView movieYear;

    @BindView(R.id.movie_description)
    TextView movieOverview;

    @BindView(R.id.release_date)
    TextView movieReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        ButterKnife.bind(this);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        movieTitle.setText(movie.getTitle());

        movieRating.setText(getString(R.string.detail_rating_head,  movie.getRating()));
        movieYear.setText(movie.getYear());
        movieReleaseDate.setText( getString(R.string.release_date_head, movie.getReleaseDate()) );
        movieOverview.setText(movie.getOverview());
        moviePopularity.setText(getString(R.string.popularity_head, movie.getPopularity()));

        Picasso.with(this)
                .load(movie.getOriginalURL())
                .into(moviePoster);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
