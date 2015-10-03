package com.nanodegree.gaby.mymovieapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nanodegree.gaby.mymovieapp.R;
import com.nanodegree.gaby.mymovieapp.api.MovieAPIService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gaby on 9/26/2015.
 */
public class Utils {
    public static final String YOUTUBE_PREFIX = "https://www.youtube.com/watch?v=";
    private static final String POSTER_URL = "http://image.tmdb.org/t/p/w342/";
    public static final String YOUTUBE_URL_PREFIX = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_URL_SUFIX = "/0.jpg";

    public static void getPosterThumbnail(String posterString, Context context, ImageView imageView){
        Picasso.with(context).load(POSTER_URL + posterString).placeholder(R.drawable.movie_placeholder).into(imageView);
    }

    public static void getReviewThumbnail(String reviewUrl, Context context, ImageView imageView){
        Picasso.with(context).load(reviewUrl).into(imageView);
    }

    public static void getTrailerThumbnail(String keyString, String siteString, Context context, ImageView imageView) {
        //TODO: support different video sites
        if (siteString!=null &&siteString.equals(MovieAPIService.YOUTUBE_VIDEO_TYPE)) {
            Picasso.with(context).load(YOUTUBE_URL_PREFIX + keyString + YOUTUBE_URL_SUFIX).placeholder(R.drawable.movie_placeholder_square).into(imageView);
        }
    }

    public static String getFormattedDateString(String dateString){
        String resultDateString = "";
        if(dateString!=null && !dateString.isEmpty()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                resultDateString = new SimpleDateFormat("MMMM yyyy").format(date);
                if (!resultDateString.isEmpty()){
                    resultDateString = resultDateString.substring(0, 1).toUpperCase() + resultDateString.substring(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultDateString;
    }

    public static void showContentDescription(Context context, View view){
        String title = view.getContentDescription().toString();
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
    }

    public static void openTrailer(Context context, View view){
        String key = (String)view.getTag(R.string.tag_key);
        String site = (String)view.getTag(R.string.tag_site);
        if (site!=null && site.equals(MovieAPIService.YOUTUBE_VIDEO_TYPE)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(buildYoutubeUri(key)));
            context.startActivity(intent);
        } //TODO: support other video types
    }

    public static String buildYoutubeUri(String key){
        return YOUTUBE_PREFIX + key;
    }

    public static String quote(String string){
        return '"' + string +'"';
    }
}
