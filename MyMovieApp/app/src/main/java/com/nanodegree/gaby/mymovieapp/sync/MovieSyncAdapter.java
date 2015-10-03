package com.nanodegree.gaby.mymovieapp.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.nanodegree.gaby.mymovieapp.R;
import com.nanodegree.gaby.mymovieapp.api.DiscoverMovieResponse;
import com.nanodegree.gaby.mymovieapp.api.MovieAPI;
import com.nanodegree.gaby.mymovieapp.api.MovieAPIService;
import com.nanodegree.gaby.mymovieapp.api.MovieResponse;
import com.nanodegree.gaby.mymovieapp.data.MovieContract;
import com.nanodegree.gaby.mymovieapp.utils.MySharedPreferences;

import java.util.List;
import java.util.Vector;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final int SYNC_INTERVAL = 60 * 720;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public static final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    private void fetchMovies(int order){
        String orderQuery;
        if(order == MySharedPreferences.ORDER_BY_POPULARITY){
            orderQuery = MovieAPIService.POPULARITY_QUERY_PARAM;
        }else{
            orderQuery = MovieAPIService.RATING_QUERY_PARAM;
        }
        MovieAPIService service = new MovieAPIService();
        DiscoverMovieResponse response = service.fetchMoviesWithOrder(orderQuery);
        if(response!=null && response.getResults()!=null){
            bulkInsert(response.getResults());
        }
    }

    private void bulkInsert(List<MovieResponse> movies){
        // Initialize the content values vector
        Vector<ContentValues> contentValuesVector = new Vector<>(movies.size());

        for (MovieResponse movie:movies){
            //is movie data ok?
            if(movie!=null && movie.getId()!=null && movie.getOriginalTitle()!=null) {
                //store movie data on content value
                ContentValues cValues = new ContentValues();
                cValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMDB_ID, movie.getId());
                cValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getOriginalTitle());
                cValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, movie.getPopularity());
                cValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, movie.getPosterPath());
                cValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, movie.getVoteAverage());
                cValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
                cValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, movie.getOverview());

                //add to vector
                contentValuesVector.add(cValues);
            }
        }

        // perform bulk insert
        if ( contentValuesVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[contentValuesVector.size()];
            contentValuesVector.toArray(cvArray);
            getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        }
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");
        MySharedPreferences mySharedPreferences = new MySharedPreferences(getContext());
        int prefOrder = mySharedPreferences.getPreferedMoviesOrder();
        fetchMovies(prefOrder);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}