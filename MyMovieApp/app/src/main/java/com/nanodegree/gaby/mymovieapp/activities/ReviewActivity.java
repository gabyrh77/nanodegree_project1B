package com.nanodegree.gaby.mymovieapp.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.nanodegree.gaby.mymovieapp.R;
import com.nanodegree.gaby.mymovieapp.adapters.ReviewAdapter;
import com.nanodegree.gaby.mymovieapp.data.MovieContract;

public class ReviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String ARG_ITEM_ID = "item_id";
    private static final int REVIEW_LIST_LOADER = 0;

    private long mMovieId;
    private ListView mReviewList;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if(savedInstanceState!=null){
            mMovieId = savedInstanceState.getLong(ARG_ITEM_ID);
        }else if (getIntent().getExtras().containsKey(ARG_ITEM_ID)) {
            mMovieId = getIntent().getLongExtra(ARG_ITEM_ID, 0);
        }
        mReviewList = (ListView) findViewById(R.id.review_list);
        mReviewAdapter = new ReviewAdapter(this, null, 0);
        mReviewList.setAdapter(mReviewAdapter);
        getSupportLoaderManager().initLoader(REVIEW_LIST_LOADER, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ARG_ITEM_ID, mMovieId);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
           // NavUtils.navigateUpTo(this, new Intent(this, MovieListActivity.class));
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                MovieContract.ReviewEntry.CONTENT_URI,
                MovieContract.ReviewEntry.REVIEW_COLUMNS,
                MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(mMovieId)},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mReviewAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mReviewAdapter.swapCursor(null);
    }

}
