package com.nanodegree.gaby.mymovieapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.nanodegree.gaby.mymovieapp.R;

/**
 * Created by Gaby on 9/26/2015.
 */
public class MySharedPreferences {
    public static final int ORDER_BY_RATING = 1;
    public static final int ORDER_BY_POPULARITY = 0;
    private Context mContext;
    private SharedPreferences mSharedPref;

    public MySharedPreferences(Context context) {
        mContext = context;
        mSharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_file_key), Context.MODE_PRIVATE);
    }

    public void setPreferedMoviesOrder(int order){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(mContext.getString(R.string.pref_order_key), order);
        editor.commit();
    }
    public int getPreferedMoviesOrder(){
        return  mSharedPref.getInt(
                mContext.getString(R.string.pref_order_key),
                mContext.getResources().getInteger(R.integer.pref_order_default));
    }

    public void setPreferedFavorites(boolean filterFavorites){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(mContext.getString(R.string.pref_favorites_key), filterFavorites);
        editor.commit();
    }

    public boolean getPreferedFavorites(){
        return  mSharedPref.getBoolean(
                mContext.getString(R.string.pref_favorites_key),
                false);
    }
}
