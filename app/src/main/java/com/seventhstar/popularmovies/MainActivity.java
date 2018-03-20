package com.seventhstar.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.seventhstar.popularmovies.model.Movie;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public final static String POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";

    private String mSortBy = POPULAR;

    Movie[] moviesList = {
            new Movie(1, "Cupcake", "1.5"),
            new Movie(2, "Donut", "1.6"),
            new Movie(3, "Eclair", "2.0-2.1"),
            new Movie(4, "Froyo", "2.2-2.2.3"),
            new Movie(5, "GingerBread", "2.3-2.3.7"),
            new Movie(6, "Honeycomb", "3.0-3.2.6"),
            new Movie(7, "Ice Cream Sandwich", "4.0-4.0.4"),
            new Movie(8, "Jelly Bean", "4.1-4.3.1"),
            new Movie(9, "KitKat", "4.4-4.4.4"),
            new Movie(10, "Lollipop", "5.0-5.1.1")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieAdapter movieAdapter = new MovieAdapter(this, Arrays.asList(moviesList));

        GridView gridView = findViewById(R.id.movies_grid);
        gridView.setAdapter(movieAdapter);
        gridView.setNumColumns(2);
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
                break;
            case R.id.sort_by_popular:
                mSortBy = POPULAR;
                item.setChecked(true);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
