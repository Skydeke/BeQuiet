package com.example.bequiet.view.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bequiet.R;
import com.example.bequiet.model.dataclasses.SelectableString;

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder)
 */
public class WlanViewHolder extends RecyclerView.ViewHolder {
    private final TextView textViewData;
    private final View v;

    public WlanViewHolder(View view, Context context) {
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