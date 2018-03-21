package com.seventhstar.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.seventhstar.popularmovies.model.Movie;
import com.seventhstar.popularmovies.service.Command;
import com.seventhstar.popularmovies.service.GetMoviesTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements
        GetMoviesTask.Listener, MovieAdapter.Callbacks {

    private final static String POPULAR = "popular";
    private final static String TOP_RATED = "top_rated";
    private static final String EXTRA_SORT_BY = "EXTRA_SORT_BY";

    private String mSortBy = POPULAR;
    private String apiKey;
    private MovieAdapter movieAdapter;

    @BindView(R.id.movies_grid)
    GridView gridView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_SORT_BY, mSortBy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        movieAdapter = new MovieAdapter(this);
        gridView.setAdapter(movieAdapter);
        gridView.setNumColumns(getResources().getInteger(R.integer.grid_columns_count));

        apiKey = getString(R.string.api_key);

        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getString(EXTRA_SORT_BY);
        }
        getMovies();
    }

    private void getMovies() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        GetMoviesTask.TaskCompleteNotify command = new GetMoviesTask.TaskCompleteNotify(this);
        new GetMoviesTask(mSortBy, command, apiKey, this).execute();
    }

    @Override
    public void onLoadFinished(Command command) {
        if (command instanceof GetMoviesTask.TaskCompleteNotify) {
            movieAdapter.refresh(((GetMoviesTask.TaskCompleteNotify) command).getMovies());
        }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by_menu, menu);

        switch (mSortBy) {
            case POPULAR:
                menu.findItem(R.id.sort_by_popular).setChecked(true);
                break;
            case TOP_RATED:
                menu.findItem(R.id.sort_by_top_rated).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_top_rated:
                mSortBy = TOP_RATED;
                item.setChecked(true);
                getMovies();
                break;
            case R.id.sort_by_popular:
                mSortBy = POPULAR;
                item.setChecked(true);
                getMovies();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openMovie(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }
}
