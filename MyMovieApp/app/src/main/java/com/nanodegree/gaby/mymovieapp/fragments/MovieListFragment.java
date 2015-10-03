package com.nanodegree.gaby.mymovieapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nanodegree.gaby.mymovieapp.R;
import com.nanodegree.gaby.mymovieapp.adapters.MovieAdapter;
import com.nanodegree.gaby.mymovieapp.data.MovieContract;
import com.nanodegree.gaby.mymovieapp.sync.MovieSyncAdapter;
import com.nanodegree.gaby.mymovieapp.utils.MySharedPreferences;

/**
 * A list fragment representing a list of Movies. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link MovieDetailFragment}.

 */
public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SettingsDialogFragment.OnSettingsChangedListener{

    public static final int DIALOG_FRAGMENT = 1;
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final String SETTINGS_DIALOG_TAG = "SDF_TAG";
    private static final int MOVIE_LIST_LOADER = 0;
    private boolean mFavorites;
    private SettingsDialogFragment settingsDialog;
    private GridView mGridView;
    private MovieAdapter mMovieAdapter;
    private OnMovieSelectedListener mMovieSelectedListener;
    private MySharedPreferences mySharedPreferences;

    /* Must be implemented by host activity */
    public interface OnMovieSelectedListener {
        void onMovieSelected(long id);
    }

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = GridView.INVALID_POSITION;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_favorites);
        if(mFavorites){
           item.setIcon(android.R.drawable.btn_star_big_on);
        }else{
            item.setIcon(android.R.drawable.btn_star_big_off);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.movies_gridView);
        mMovieAdapter = new MovieAdapter(getActivity(), null, 0);
        mGridView.setAdapter(mMovieAdapter);
        mMovieSelectedListener = (OnMovieSelectedListener)getActivity();
        mySharedPreferences = new MySharedPreferences(getContext());
        mFavorites = mySharedPreferences.getPreferedFavorites();
        View emptyMovies = rootView.findViewById(R.id.empty_movie_view);
        mGridView.setEmptyView(emptyMovies);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                if (cursor != null) {
                    mMovieSelectedListener.onMovieSelected(cursor.getLong(1));
                    if(mGridView.getChoiceMode()==GridView.CHOICE_MODE_SINGLE) {
                        mMovieAdapter.setItemSelected(i);
                        mActivatedPosition = i;
                        mMovieAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            mMovieAdapter.setItemSelected(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
            mActivatedPosition = savedInstanceState.getInt(STATE_ACTIVATED_POSITION);
            mMovieAdapter.notifyDataSetChanged();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LIST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // This is called when a new Loader needs to be created.
        String sortOrder = "", selection= null;
        String[] selectionArgs = null;
        boolean filterFavorites = mySharedPreferences.getPreferedFavorites();
        int prefOrder = mySharedPreferences.getPreferedMoviesOrder();
        if(prefOrder == MySharedPreferences.ORDER_BY_POPULARITY){
            sortOrder = MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY + " DESC";
        }else{
            sortOrder = MovieContract.MovieEntry.COLUMN_MOVIE_RATING + " DESC";
        }
        if(filterFavorites){
            selection = MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE + " = ?";
            selectionArgs = new String[]{"1"};
        }
        String[] listColumns = {
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_IMDB_ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER
        };
        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                listColumns,
                selection,
                selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.swapCursor(data);
        if (mActivatedPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mGridView.smoothScrollToPosition(mActivatedPosition);
        }
        if(data!=null && data.getCount()>0 && mGridView.getChoiceMode()==GridView.CHOICE_MODE_SINGLE){
            mGridView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mGridView.performItemClick(mGridView, 0, mMovieAdapter.getItemId(0));
                }
            },100);

        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    @Override
    public void onSettingsChanged() {
        fetchMovies();
        getLoaderManager().restartLoader(MOVIE_LIST_LOADER, null, this);
    }

    private void fetchMovies() {
        MovieSyncAdapter.syncImmediately(getActivity());
    }


    private void showSettingsDialog() {
        if(settingsDialog==null) {
            settingsDialog = new SettingsDialogFragment();
        }
        settingsDialog.setTargetFragment(this, DIALOG_FRAGMENT);
        settingsDialog.show(getFragmentManager().beginTransaction(), SETTINGS_DIALOG_TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_order) {
            showSettingsDialog();
            return true;
        }else if (item.getItemId() == R.id.action_favorites) {
            toggleFavorites();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_list, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != GridView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        mGridView.setChoiceMode(activateOnItemClick
                ? GridView.CHOICE_MODE_SINGLE
                : GridView.CHOICE_MODE_NONE);

    }

    public void toggleFavorites(){
        boolean newFav = !mFavorites;
        mySharedPreferences.setPreferedFavorites(newFav);
        mFavorites = newFav;
        getActivity().invalidateOptionsMenu();
        getLoaderManager().restartLoader(MOVIE_LIST_LOADER, null, this);
    }
}
