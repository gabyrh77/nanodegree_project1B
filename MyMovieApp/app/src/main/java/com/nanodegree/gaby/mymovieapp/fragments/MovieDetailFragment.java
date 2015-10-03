package com.nanodegree.gaby.mymovieapp.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nanodegree.gaby.mymovieapp.R;
import com.nanodegree.gaby.mymovieapp.activities.MovieDetailActivity;
import com.nanodegree.gaby.mymovieapp.activities.MovieListActivity;
import com.nanodegree.gaby.mymovieapp.activities.ReviewActivity;
import com.nanodegree.gaby.mymovieapp.activities.VideoActivity;
import com.nanodegree.gaby.mymovieapp.api.MovieAPIService;
import com.nanodegree.gaby.mymovieapp.data.MovieContract;
import com.nanodegree.gaby.mymovieapp.service.MovieExtrasService;
import com.nanodegree.gaby.mymovieapp.utils.Utils;


/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, ShareActionProvider.OnShareTargetSelectedListener{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_URI = "item_uri";
    public static final String ARG_ITEM_ID = "item_id";
    private static final int MOVIE_ITEM_LOADER = 0;
    private static final int VIDEO_LIST_LOADER = 1;
    private static final int REVIEW_LIST_LOADER = 2;
    private long mMovieId;
    private String mTrailerUri;
    private TextView mTitleTextView;
    private TextView mSynopsisTextView;
    private TextView mReleaseDateTextView;
    private TextView mPopularityTextView;
    private TextView mRatingTextView;
    private ImageView mPosterImageView;
    private ImageView mFavoriteImageButton;
    private ViewStub mVideoViewStub;
    private ViewStub mReviewViewStub;
    private ShareActionProvider mShareActionProvider;
    private ProgressBar mExtraProgress;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(savedInstanceState!=null){
            mMovieId = savedInstanceState.getLong(ARG_ITEM_ID);
            mTrailerUri = savedInstanceState.getString(ARG_ITEM_URI);
        }else if (getArguments().containsKey(ARG_ITEM_ID)) {
            mMovieId = getArguments().getLong(ARG_ITEM_ID);
            mTrailerUri = null;
        }
        MovieServiceReceiver mDownloadStateReceiver =
                new MovieServiceReceiver();
        IntentFilter intentFilter  = new IntentFilter(MovieExtrasService.BROADCAST_ACTION_COMPLETE);
        // Registers the MovieServiceReceiver and its intent filters
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mDownloadStateReceiver,
                intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mTitleTextView = (TextView) rootView.findViewById(R.id.movie_title);
        mReleaseDateTextView = (TextView) rootView.findViewById(R.id.movie_release_date);
        mSynopsisTextView = (TextView) rootView.findViewById(R.id.movie_synopsis);
        mRatingTextView = (TextView) rootView.findViewById(R.id.movie_rating);
        mPopularityTextView = (TextView) rootView.findViewById(R.id.movie_popularity);
        mPosterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
        mFavoriteImageButton = (ImageButton) rootView.findViewById(R.id.favorite_button);
        mFavoriteImageButton.setOnClickListener(this);
        mExtraProgress = (ProgressBar) rootView.findViewById(R.id.movie_extra_progressbar);
        mVideoViewStub = (ViewStub) rootView.findViewById(R.id.movie_trailer_stub);
        mReviewViewStub = (ViewStub) rootView.findViewById(R.id.movie_review_stub);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        updateMovieExtras();
        getLoaderManager().initLoader(MOVIE_ITEM_LOADER, null, this);
        getLoaderManager().initLoader(VIDEO_LIST_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LIST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setOnShareTargetSelectedListener(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        if(mTrailerUri!=null) {
            item.setVisible(true);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mTrailerUri);
            mShareActionProvider.setShareIntent(shareIntent);
        }else{
            item.setVisible(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ARG_ITEM_ID, mMovieId);
        outState.putString(ARG_ITEM_URI, mTrailerUri);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case(MOVIE_ITEM_LOADER):{
                Uri mUri = MovieContract.MovieEntry.buildMovieUri(mMovieId);
                if ( null != mUri ) {
                    return new CursorLoader(
                            getActivity(),
                            mUri,
                            MovieContract.MovieEntry.DETAIL_COLUMNS,
                            MovieContract.MovieEntry.COLUMN_MOVIE_IMDB_ID + " = ?",
                            new String[]{String.valueOf(mMovieId)},
                            null
                    );
                }
            }
            case(VIDEO_LIST_LOADER):{
                return new CursorLoader(
                        getActivity(),
                        MovieContract.VideoEntry.CONTENT_URI,
                        MovieContract.VideoEntry.VIDEO_COLUMNS,
                        MovieContract.VideoEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(mMovieId)},
                        null
                );

            }
            case(REVIEW_LIST_LOADER):{
                return new CursorLoader(
                        getActivity(),
                        MovieContract.ReviewEntry.CONTENT_URI,
                        MovieContract.ReviewEntry.REVIEW_COLUMNS,
                        MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(mMovieId)},
                        null
                );

            }
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case MOVIE_ITEM_LOADER:{
                if (data != null && data.moveToFirst()) {
                    mTitleTextView.setText(data.getString(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE_INDEX));
                    mReleaseDateTextView.setText(Utils.getFormattedDateString(data.getString(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE_INDEX)));
                    mSynopsisTextView.setText(data.getString(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS_INDEX));
                    mRatingTextView.setText(String.format("%.1f/10", data.getFloat(MovieContract.MovieEntry.COLUMN_MOVIE_RATING_INDEX)));
                    mPopularityTextView.setText(String.format("%.2f/100", data.getFloat(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY_INDEX)));
                    Utils.getPosterThumbnail(data.getString(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_INDEX), getContext(), mPosterImageView);
                    setFavoriteImageButton(data.getInt(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE_INDEX));
                }
                break;
            }
            case VIDEO_LIST_LOADER:{
                if (data != null && data.moveToFirst() && mVideoViewStub!=null) {
                    if(mExtraProgress!=null){
                        mExtraProgress.setVisibility(View.GONE);
                    }
                    View videoView = mVideoViewStub.inflate();
                    mVideoViewStub = null;
                    videoView.findViewById(R.id.first_trailer_view).setTag(R.string.tag_key, data.getString(2));
                    videoView.findViewById(R.id.first_trailer_view).setTag(R.string.tag_site, data.getString(3));
                    TextView title = (TextView)videoView.findViewById(R.id.video_title_1);
                    title.setText(data.getString(MovieContract.VideoEntry.COLUMN_VIDEO_NAME_INDEX));
                    ImageView image = (ImageView)videoView.findViewById(R.id.video_image_1);
                    String keyVideo = data.getString(MovieContract.VideoEntry.COLUMN_VIDEO_KEY_INDEX);
                    String siteString = data.getString(MovieContract.VideoEntry.COLUMN_VIDEO_SITE_INDEX);
                    Utils.getTrailerThumbnail(keyVideo, siteString, getContext(), image);
                    if (siteString!=null && siteString.equals(MovieAPIService.YOUTUBE_VIDEO_TYPE)) {
                        mTrailerUri = Utils.buildYoutubeUri(keyVideo);
                        if(mShareActionProvider!=null){
                            getActivity().invalidateOptionsMenu();
                        }
                        getActivity().invalidateOptionsMenu();
                    }//TODO: support other video sites
                    boolean secondTrailer = data.moveToNext();
                    if(secondTrailer){
                        boolean moreButton = true;
                        int numberTrailer = getResources().getInteger(R.integer.num_trailer_columns);
                        if(numberTrailer>1) {
                            videoView.findViewById(R.id.second_trailer_view).setTag(R.string.tag_key, data.getString(2));
                            videoView.findViewById(R.id.second_trailer_view).setTag(R.string.tag_site, data.getString(3));
                            TextView title2 = (TextView) videoView.findViewById(R.id.video_title_2);
                            title2.setText(data.getString(MovieContract.VideoEntry.COLUMN_VIDEO_NAME_INDEX));
                            ImageView image2 = (ImageView) videoView.findViewById(R.id.video_image_2);
                            Utils.getTrailerThumbnail(data.getString(MovieContract.VideoEntry.COLUMN_VIDEO_KEY_INDEX), data.getString(MovieContract.VideoEntry.COLUMN_VIDEO_SITE_INDEX), getContext(), image2);
                            moreButton = data.moveToNext();
                        }else{
                            videoView.findViewById(R.id.second_trailer_view).setVisibility(View.GONE);
                        }
                        toggleMoreButton(moreButton, videoView, true);
                    }else{
                        toggleMoreButton(false, videoView, true);
                        videoView.findViewById(R.id.second_trailer_view).setVisibility(View.GONE);
                    }
                }
                break;
            }
            case REVIEW_LIST_LOADER:{
                if (data != null && data.moveToFirst() && mReviewViewStub!=null) {
                    if(mExtraProgress!=null){
                        mExtraProgress.setVisibility(View.GONE);
                    }
                    View view = mReviewViewStub.inflate();
                    mReviewViewStub = null;
                    TextView author = (TextView)view.findViewById(R.id.review_author_1);
                    author.setText(data.getString(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR_INDEX));
                    TextView content = (TextView)view.findViewById(R.id.review_content_1);
                    content.setText(Utils.quote(data.getString(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT_INDEX)));
                    toggleMoreButton(true, view, false);
                    boolean secondReview = data.moveToNext();
                    if(secondReview){
                        int numberReview = getResources().getInteger(R.integer.num_review_rows);
                        if(numberReview>1) {
                            TextView author2 = (TextView)view.findViewById(R.id.review_author_2);
                            author2.setText(data.getString(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR_INDEX));
                            TextView content2 = (TextView)view.findViewById(R.id.review_content_2);
                            content2.setText(Utils.quote(data.getString(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT_INDEX)));

                        }else{
                            view.findViewById(R.id.review_second_view).setVisibility(View.GONE);
                        }
                    }else{
                        view.findViewById(R.id.review_second_view).setVisibility(View.GONE);
                    }
                }
                break;
            }
        }

    }

    private void toggleMoreButton(boolean show, View videoView, final boolean trailer){
        Button button = (Button)videoView.findViewById(R.id.more_button);
        if(show){
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent resultIntent;
                    if(trailer) {
                        resultIntent = new Intent(getContext(), VideoActivity.class);
                    }else{
                       resultIntent = new Intent(getContext(), ReviewActivity.class);
                    }
                    resultIntent.putExtra(VideoActivity.ARG_ITEM_ID, mMovieId);
                    startActivity(resultIntent);
                }
            });
        }else{
            button.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * OnClick method for the favorite button
     * to perform the action necessary
     */
    @Override
    public void onClick(View view) {
        int valueTag = view.isSelected()?0:1;
        ContentValues cValues = new ContentValues();
        cValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE, valueTag);
        Uri mUri = MovieContract.MovieEntry.buildMovieUri(mMovieId);
        String id = String.valueOf(mMovieId);
        int result = getActivity().getContentResolver().update(mUri, cValues, MovieContract.MovieEntry.COLUMN_MOVIE_IMDB_ID + " = ?", new String[]{id});

        if(result>0){
            setFavoriteImageButton(valueTag);
        }else{
            Toast.makeText(getContext(),R.string.message_not_saved, Toast.LENGTH_SHORT).show();
        }
    }

    public void setFavoriteImageButton(int value){
        if(value==1){
            mFavoriteImageButton.setSelected(true);
        }else{
            mFavoriteImageButton.setSelected(false);
        }
    }

    private void updateMovieExtras() {
        Intent alarmIntent = new Intent(getActivity(), MovieExtrasService.AlarmReceiver.class);
        alarmIntent.putExtra(MovieExtrasService.MOVIE_ID_EXTRA, mMovieId);

        //Wrap in a pending intent which only fires once.
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0,alarmIntent,PendingIntent.FLAG_ONE_SHOT);

        AlarmManager am=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

        //Set the AlarmManager to wake up the system.
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1, pi);
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
        startActivity(intent);
        return false;
    }

    // Broadcast receiver for receiving status updates from the IntentService
    private class MovieServiceReceiver extends BroadcastReceiver {

        // Prevents instantiation
        private MovieServiceReceiver() {
        }
        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override
        public void onReceive(Context context, Intent intent) {
            if(mExtraProgress!=null){
                mExtraProgress.setVisibility(View.GONE);
            }
        }
    }
}
