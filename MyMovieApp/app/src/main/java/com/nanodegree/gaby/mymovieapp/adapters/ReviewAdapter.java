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
public class ReviewAdapter extends CursorAdapter {

    public ReviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater =
                LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.item_movie_review, viewGroup, false);
        // set up the ViewHolder
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.contentView = (TextView) view.findViewById(R.id.item_review_content);
        viewHolder.authorView = (TextView) view.findViewById(R.id.item_review_author);
        // store the holder with the view.
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if(cursor!=null) {
            viewHolder.contentView.setText(Utils.quote(cursor.getString(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT_INDEX)));
            viewHolder.authorView.setText(cursor.getString(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR_INDEX));
        }
    }

    static class ViewHolder{
        TextView contentView;
        TextView authorView;
    }

}
