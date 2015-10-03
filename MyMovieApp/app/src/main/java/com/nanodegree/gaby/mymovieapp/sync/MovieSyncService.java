package com.nanodegree.gaby.mymovieapp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MovieSyncService extends Service {
    private final String LOG_TAG = MovieSyncService.class.getSimpleName();
    private static final Object sSyncAdapterLock = new Object();
    private static MovieSyncAdapter sSunshineSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate called.");
        synchronized (sSyncAdapterLock) {
            if (sSunshineSyncAdapter == null) {
                sSunshineSyncAdapter = new MovieSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSunshineSyncAdapter.getSyncAdapterBinder();
    }
}