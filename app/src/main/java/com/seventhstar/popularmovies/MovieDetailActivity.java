package com.seventhstar.popularmovies;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seventhstar.popularmovies.adapters.TrailersListAdapter;
import com.seventhstar.popularmovies.database.MovieEntry;
import com.seventhstar.popularmovies.model.Movie;
import com.seventhstar.popularmovies.model.Review;
import com.seventhstar.popularmovies.model.Reviews;
import com.seventhstar.popularmovies.model.Trailer;
import com.seventhstar.popularmovies.model.Trailers;
import com.seventhstar.popularmovies.service.Command;
import com.seventhstar.popularmovies.service.GetMovieDBDataTask;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


@SuppressWarnings("WeakerAccess")
public class MovieDetailActivity extends AppCompatActivity implements
        TrailersListAdapter.Callbacks, ReviewsListAdapter.Callbacks, GetMovieDBDataTask.Listener {
    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    public static final String EXTRA_REVIEWS = "EXTRA_REVIEWS";
    public static final String EXTRA_TRAILERS = "EXTRA_TRAILERS";

    @BindView(R.id.movie_title)
    TextView movieTitle;

    @BindView(R.id.movie_poster)
    ImageView moviePoster;

    @BindView(R.id.movie_poster_small)
    ImageView movieSmallPoster;

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

    @BindView(R.id.reviews_loading)
    TextView mLoadingReviews;

    @BindView(R.id.review_layout)
    LinearLayout review_layout;

    @BindView(R.id.trailers_list)
    RecyclerView mRecyclerViewForTrailers;
    private TrailersListAdapter mTrailersListAdapter;

    @BindView(R.id.reviews_list)
    RecyclerView mRecyclerViewForReviews;
    private ReviewsListAdapter mReviewsListAdapter;

    @BindView(R.id.button_make_favorite)
    Button mButtonMakeFavorite;

    @BindView(R.id.genres)
    TextView genresTextView;

    Movie movie;
    Boolean isFavorite;
    long movieID;
    GetMovieDBDataTask.TaskCompleteNotify mTaskCompleteNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        ButterKnife.bind(this);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        movieID = movie.getId();
        movieTitle.setText(movie.getTitle());

        movieRating.setText(getString(R.string.detail_rating_head, movie.getRating()));
        movieYear.setText(movie.getYear());
        movieReleaseDate.setText(getString(R.string.release_date_head, movie.getReleaseDate()));
        movieOverview.setText(movie.getOverview());
        moviePopularity.setText(getString(R.string.popularity_head, movie.getPopularity()));

        Picasso.with(this)
                .load(movie.getBackdropURL())
                .into(moviePoster);

        Picasso.with(this)
                .load(movie.getPreviewURL())
                .into(movieSmallPoster);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerViewForTrailers.setLayoutManager(layoutManager);
        mTrailersListAdapter = new TrailersListAdapter(new ArrayList<Trailer>(), this);
        mRecyclerViewForTrailers.setAdapter(mTrailersListAdapter);
        mRecyclerViewForTrailers.setNestedScrollingEnabled(false);

        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewsListAdapter = new ReviewsListAdapter(this);
        mRecyclerViewForReviews.setLayoutManager(layoutManager);
        mRecyclerViewForReviews.setAdapter(mReviewsListAdapter);

        mTaskCompleteNotify = new GetMovieDBDataTask.TaskCompleteNotify(this);

        GetOrRestoreData(savedInstanceState, EXTRA_MOVIE);
        GetOrRestoreData(savedInstanceState, EXTRA_TRAILERS);
        GetOrRestoreData(savedInstanceState, EXTRA_REVIEWS);

        isFavorite = isFavorite();
        updateFavoriteButton();
        mButtonMakeFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    removeFromFavorite();
                } else {
                    makeFavorite();
                }
            }
        });
    }

    private void GetOrRestoreData(Bundle savedInstanceState, String extraKey) {
        boolean restoreFromInstance = (savedInstanceState != null && savedInstanceState.containsKey(extraKey));

        switch (extraKey) {
            case EXTRA_TRAILERS:
                if (restoreFromInstance) {
                    mTrailersListAdapter.addTrailers(savedInstanceState.<Trailer>getParcelableArrayList(extraKey));
                } else {
                    new GetMovieDBDataTask<>(movieID, mTaskCompleteNotify, this, Trailers.class).execute();
                }
                break;
            case EXTRA_REVIEWS:
                if (restoreFromInstance) {
                    mLoadingReviews.setText("");
                    mReviewsListAdapter.addReviews(savedInstanceState.<Review>getParcelableArrayList(extraKey));
                } else {
                    new GetMovieDBDataTask<>(movieID, mTaskCompleteNotify, this, Reviews.class).execute();
                }
                break;
            case EXTRA_MOVIE:
                if (restoreFromInstance) {
                    genresTextView.setText((CharSequence) savedInstanceState.getParcelable(extraKey));
                } else {
                    new GetMovieDBDataTask<>(movieID, mTaskCompleteNotify, this, Movie.class).execute();
                }
        }
    }


    @SuppressLint("StaticFieldLeak")
    private void removeFromFavorite() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                getContentResolver().delete(
                        MovieEntry.CONTENT_URI,
                        MovieEntry.COLUMN_MOVIE_API_ID + " = " + movieID,
                        null);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                isFavorite = false;
                updateFavoriteButton();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    public void makeFavorite() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (!isFavorite()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieEntry.COLUMN_MOVIE_API_ID, movieID);
                    contentValues.put(MovieEntry.COLUMN_MOVIE_NAME, movie.getTitle());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_RATING, movie.getRating());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_SMALL_POSTER, movie.getPreviewURL());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_POSTER, movie.getBackdropURL());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_GENRES, movie.getGenresString());
                    getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                isFavorite = true;
                updateFavoriteButton();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateFavoriteButton() {
        int color;
        int title;
        if (isFavorite) {
            title = R.string.remove_favorite;
            color = R.color.remove_color;
        } else {
            title = R.string.make_favorite;
            color = R.color.add_color;
        }

        mButtonMakeFavorite.setText(title);
        mButtonMakeFavorite.setBackgroundColor(ContextCompat.getColor(this, color));
    }

    private boolean isFavorite() {
        Cursor movieCursor = getContentResolver().
                query(
                        MovieEntry.CONTENT_URI,
                        new String[]{MovieEntry.COLUMN_MOVIE_API_ID},
                        MovieEntry.COLUMN_MOVIE_API_ID + " = " + movieID,
                        null,
                        null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Trailer> trailers = mTrailersListAdapter.getTrailers();
        if (trailers != null && !trailers.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_TRAILERS, trailers);
        }

        ArrayList<Review> reviews = mReviewsListAdapter.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_REVIEWS, reviews);
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openTrailer(Trailer trailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerUrl())));
    }

    @Override
    public void onLoadFinished(Command command) {
        if (command instanceof GetMovieDBDataTask.TaskCompleteNotify) {

            GetMovieDBDataTask.TaskNames taskName = ((GetMovieDBDataTask.TaskCompleteNotify) command).taskName;

            switch (taskName) {
                case TRAILERS:
                    Trailers t = (Trailers) ((GetMovieDBDataTask.TaskCompleteNotify) command).getDataList();
                    mTrailersListAdapter.addTrailers(t.getTrailersList());
                    break;

                case REVIEWS:
                    mLoadingReviews.setText("");
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) review_layout.getLayoutParams();
                    lp.setMargins(0, 0, 0, 0);
                    review_layout.setLayoutParams(lp);

                    Reviews r = (Reviews) ((GetMovieDBDataTask.TaskCompleteNotify) command).getDataList();
                    mReviewsListAdapter.addReviews(r.getReviewsList());
                    break;

                case MOVIE:
                    Movie m = (Movie) ((GetMovieDBDataTask.TaskCompleteNotify) command).getDataList();
                    movie.setGenres(m.getGenres());
                    genresTextView.setText(m.getGenresString());
            }
        }
    }

    @Override
    public void read(Review review) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(review.getUrl())));
    }


}
