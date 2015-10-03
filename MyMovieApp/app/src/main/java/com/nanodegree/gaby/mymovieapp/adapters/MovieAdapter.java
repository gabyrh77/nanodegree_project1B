package com.nanodegree.gaby.mymovieapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.nanodegree.gaby.mymovieapp.R;
import com.nanodegree.gaby.mymovieapp.api.MovieResponse;
import com.nanodegree.gaby.mymovieapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaby on 9/26/2015.
 */
public class MovieAdapter extends CursorAdapter {
    private int selectedPosition;
    private static final int TYPE_SELECTED = 1;
    private static final int TYPE_NORMAL = 0;

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        selectedPosition = -1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position==selectedPosition?TYPE_SELECTED:TYPE_NORMAL;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater =
                LayoutInflater.from(viewGroup.getContext());

        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case TYPE_NORMAL: {
                layoutId = R.layout.item_movie_poster;
                break;
            }
            case TYPE_SELECTED: {
                layoutId = R.layout.item_movie_poster_selected;
                break;
            }
        }
        View view = inflater.inflate(layoutId, viewGroup, false);
        // set up the ViewHolder
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imageView = (ImageView) view.findViewById(R.id.image_movie_poster);

        // store the holder with the view.
        view.setTag(viewHolder);
        return view;
    }

    public void setItemSelected(int position){
        selectedPosition = position;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if(cursor!=null) {
            Utils.getPosterThumbnail(cursor.getString(2), context, viewHolder.imageView);
        }

    }

    static class ViewHolder{
        ImageView imageView;
    }

}
