package com.nanodegree.gaby.mymovieapp.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.nanodegree.gaby.mymovieapp.R;
import com.nanodegree.gaby.mymovieapp.utils.MySharedPreferences;

/**
 * Created by Gaby on 9/26/2015.
 */
public class SettingsDialogFragment extends DialogFragment {
    private static final int NOTHING_SELECTED = -1;
    private MySharedPreferences mySharedPreferences;
    private OnSettingsChangedListener mListener;
    private int mInitialSelected;
    private int mNewSelected;

    public SettingsDialogFragment() {}

    public interface OnSettingsChangedListener{
        public void onSettingsChanged();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try{
            mListener = (OnSettingsChangedListener)getTargetFragment();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mySharedPreferences = new MySharedPreferences(getActivity());
        mInitialSelected = mySharedPreferences.getPreferedMoviesOrder();
        mNewSelected = NOTHING_SELECTED;

        // Set the dialog title
        builder.setTitle(R.string.title_order_settings)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(R.array.order_array, mInitialSelected,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mNewSelected = which;
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        //dialog.
                        if (mNewSelected != NOTHING_SELECTED && mNewSelected!=mInitialSelected) {
                            mySharedPreferences.setPreferedMoviesOrder(mNewSelected);
                            mListener.onSettingsChanged();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //nothing
                    }
                });

        return builder.create();
    }
}
