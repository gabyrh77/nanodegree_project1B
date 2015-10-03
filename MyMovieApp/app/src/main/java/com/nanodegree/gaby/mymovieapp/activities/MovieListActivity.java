package com.nanodegree.gaby.mymovieapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nanodegree.gaby.mymovieapp.R;
import com.nanodegree.gaby.mymovieapp.fragments.MovieDetailFragment;
import com.nanodegree.gaby.mymovieapp.fragments.MovieListFragment;
import com.nanodegree.gaby.mymovieapp.sync.MovieSyncAdapter;
import com.nanodegree.gaby.mymovieapp.utils.Utils;


/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MovieListFragment} and the item details
 * (if present) is a {@link MovieDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link MovieListFragment.OnMovieSelectedListener} interface
 * to listen for item selections.
 */
public class MovieListActivity extends AppCompatActivity
        implements MovieListFragment.OnMovieSelectedListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((MovieListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.movie_list))
                    .setActivateOnItemClick(true);
        }else{
            mTwoPane = false;
        }
        MovieSyncAdapter.initializeSyncAdapter(this);
    }


    @Override
    public void onMovieSelected(long id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(MovieDetailFragment.ARG_ITEM_ID, id);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MovieDetailActivity.class);
            detailIntent.putExtra(MovieDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    public void showContentDescription(View view){
        Utils.showContentDescription(getApplicationContext(), view);
    }

    public void openTrailer(View view){
        Utils.openTrailer(this, view);
    }
}
