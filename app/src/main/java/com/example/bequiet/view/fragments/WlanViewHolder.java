package com.example.bequiet.view.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bequiet.R;

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder)
 */
public class WlanViewHolder extends RecyclerView.ViewHolder {
    private final TextView textViewData;
    private final View v;

    public WlanViewHolder(View view) {
        super(view);
        v =view;
        textViewData = (TextView) view.findViewById(R.id.selectableDataString);
    }

    public View getV() {
        return v;
    }

    public TextView getDataTV() {
        return textViewData;
    }
}