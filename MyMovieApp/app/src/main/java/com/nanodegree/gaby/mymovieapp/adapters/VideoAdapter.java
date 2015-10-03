package com.nanodegree.gaby.mymovieapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nanodegree.gaby.mymovieapp.R;
import com.nanodegree.gaby.mymovieapp.data.MovieContract;
import com.nanodegree.gaby.mymovieapp.utils.Utils;

/**
 * Created by Gaby on 9/26/2015.
 */
public class VideoAdapter extends CursorAdapter {

    public VideoAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater =
                LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.item_movie_trailer, viewGroup, false);
        // set up the ViewHolder
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imageView = (ImageView) view.findViewById(R.id.item_video_image);
        viewHolder.titleView = (TextView) view.findViewById(R.id.item_video_title);
        viewHolder.wrapView = (LinearLayout) view.findViewById(R.id.wrap_trailer_view);
        // store the holder with the view.
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if(cursor!=null) {
            viewHolder.titleView.setText(cursor.getString(MovieContract.VideoEntry.COLUMN_VIDEO_NAME_INDEX));
            Utils.getTrailerThumbnail(cursor.getString(MovieContract.VideoEntry.COLUMN_VIDEO_KEY_INDEX), cursor.getString(MovieContract.VideoEntry.COLUMN_VIDEO_SITE_INDEX), context, viewHolder.imageView);
            viewHolder.wrapView.setTag(R.string.tag_key, cursor.getString(2));
            viewHolder.wrapView.setTag(R.string.tag_site, cursor.getString(3));
        }

    }

    static class ViewHolder{
        ImageView imageView;
        TextView titleView;
        LinearLayout wrapView;
    }

}
