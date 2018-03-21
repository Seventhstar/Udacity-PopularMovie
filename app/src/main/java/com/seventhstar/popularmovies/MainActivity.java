package com.seventhstar.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.seventhstar.popularmovies.service.Command;
import com.seventhstar.popularmovies.service.GetMoviesTask;


public class MainActivity extends AppCompatActivity implements GetMoviesTask.Listener{

    public final static String POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";

    private String mSortBy = POPULAR;
    MovieAdapter movieAdapter;

    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieAdapter = new MovieAdapter(this);

        GridView gridView = findViewById(R.id.movies_grid);
        gridView.setAdapter(movieAdapter);
        gridView.setNumColumns(2);

        apiKey = getString(R.string.api_key);
    }

    void getMovies() {

        GetMoviesTask.TaskCompleteNotify command = new GetMoviesTask.TaskCompleteNotify(this);
        new GetMoviesTask(mSortBy, command, apiKey, this).execute();

    }

    @Override
    public void onLoadFinished(Command command) {
        if (command instanceof GetMoviesTask.TaskCompleteNotify) {
            movieAdapter.refresh(((GetMoviesTask.TaskCompleteNotify) command).getMovies());
        }
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


}
