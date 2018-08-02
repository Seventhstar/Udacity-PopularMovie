package com.seventhstar.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.seventhstar.popularmovies.adapters.MovieRecyclerAdapter;
import com.seventhstar.popularmovies.database.MovieEntry;
import com.seventhstar.popularmovies.model.Movie;
import com.seventhstar.popularmovies.service.Command;
import com.seventhstar.popularmovies.service.DBTools;
import com.seventhstar.popularmovies.service.GetMoviesTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


@SuppressWarnings("WeakerAccess")
public class MainActivity extends AppCompatActivity implements
        GetMoviesTask.Listener, MovieRecyclerAdapter.Callbacks, LoaderManager.LoaderCallbacks<Cursor>,
        MovieRecyclerAdapter.RecycleViewOnItemClickListener {

    private final static String POPULAR = "popular";
    private final static String TOP_RATED = "top_rated";
    private final static String FAVORITES = "favorites";
    private static final String EXTRA_MOVIES = "EXTRA_MOVIES";
    private static final String EXTRA_SORT_BY = "EXTRA_SORT_BY";
    private static final String EXTRA_GRID_POSITION = "EXTRA_GRID_POSITION";
    private static final int FAVORITES_LOADER = 9;

    private String mSortBy = POPULAR;
    private String apiKey;
    private MovieRecyclerAdapter movieRecyclerAdapter;

    @BindView(R.id.movies_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.bnv_main_activity)
    BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener navItemSelectedListener;
    private boolean isBeforeSelectedNavItem;
    private int selectedNavItemId;

    private LoaderManager.LoaderCallbacks callbacks;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_SORT_BY, mSortBy);
        ArrayList<Movie> movies = movieRecyclerAdapter.getMovies();
        if (movies != null && !movies.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_MOVIES, movies);
//            outState.putInt(EXTRA_GRID_POSITION, gridView.getFirstVisiblePosition());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        movieRecyclerAdapter = new MovieRecyclerAdapter(this, this);

        recyclerView.setAdapter(movieRecyclerAdapter);

        Resources resources = getResources();
        int columns = resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                ? resources.getInteger(R.integer.main_grid_column_portrait)
                : resources.getInteger(R.integer.main_grid_column_landscape);

        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));
        recyclerView.setHasFixedSize(true);


        apiKey = BuildConfig.API_KEY;

        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getString(EXTRA_SORT_BY);

            if (savedInstanceState.containsKey(EXTRA_MOVIES)) {
                List<Movie> movies = savedInstanceState.getParcelableArrayList(EXTRA_MOVIES);
                movieRecyclerAdapter.refresh((ArrayList<Movie>) movies);
            }
        } else {
            getMovies();
        }

        navItemSelectedListener = getNavigationItemSelectedListener();
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemSelectedListener);
        bottomNavigationView.setOnNavigationItemReselectedListener(getNavigationItemReselectedListener());
        bottomNavigationView.setSelectedItemId(selectedNavItemId);

        callbacks = this;
    }

    @NonNull
    private BottomNavigationView.OnNavigationItemReselectedListener getNavigationItemReselectedListener() {
        return new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (!isBeforeSelectedNavItem) {
                    navItemSelectedListener.onNavigationItemSelected(item);
                    isBeforeSelectedNavItem = !isBeforeSelectedNavItem;
                }
            }
        };
    }

    @NonNull
    private BottomNavigationView.OnNavigationItemSelectedListener getNavigationItemSelectedListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedNavItemId = item.getItemId();

                switch (item.getItemId()) {
                    case R.id.sort_by_top_rated:
                        mSortBy = TOP_RATED;
                        break;
                    case R.id.sort_by_popular:
                        mSortBy = POPULAR;
                        break;
                    case R.id.sort_by_favorites:
                        mSortBy = FAVORITES;
                }

                if (selectedNavItemId == R.id.sort_by_favorites) {
                    getSupportLoaderManager().initLoader(FAVORITES_LOADER, null, callbacks);
                } else {
                    getMovies();
                }

                return true;
            }
        };
    }

    private void getMovies() {
        if (mSortBy.equals(FAVORITES)) {
            getSupportLoaderManager().initLoader(FAVORITES_LOADER, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            GetMoviesTask.TaskCompleteNotify command = new GetMoviesTask.TaskCompleteNotify(this);
            new GetMoviesTask(mSortBy, command, apiKey, this).execute();
        }
    }

    @Override
    public void onLoadFinished(Command command) {
        if (command instanceof GetMoviesTask.TaskCompleteNotify) {
            ArrayList<Movie> movies = ((GetMoviesTask.TaskCompleteNotify) command).getMovies();
            if (movies == null) {
                Toast.makeText(this, getString(R.string.api_movie_db_problem), Toast.LENGTH_SHORT).show();
            } else {
                movieRecyclerAdapter.refresh(movies);
            }
        }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> cursorLoader, Cursor cursor) {
        if (mSortBy.equals(FAVORITES)) {
            movieRecyclerAdapter.fromCursor(cursor);
        }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }


    @Override
    public void openMovie(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }


    @NonNull
    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        mLoadingIndicator.setVisibility(View.VISIBLE);

        CursorLoader cursorLoader = new CursorLoader(this,
                MovieEntry.CONTENT_URI,
                MovieEntry.MovieColumns.columnNames(),
                null,
                null,
                null);

        return cursorLoader;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onItemResultRecyclerClick(Movie movie) {
        openMovie(movie);
    }

    @Override
    public void onFavoriteClick(Movie movie) {
        movie.switchFavorite();
        if (movie.isFavorite()) {
            DBTools.makeFavorite(this, movie);
        } else {
            DBTools.removeFromFavorite(this, movie);
        }
    }
}
