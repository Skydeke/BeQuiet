package com.example.bequiet.view.fragments;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bequiet.R;
import com.example.bequiet.model.dataclasses.SelectableString;

import java.util.List;

public class SelectableStringAdapter extends RecyclerView.Adapter<WlanViewHolder> {

    private Context mContext;

    private List<SelectableString> items;

    private SelectWifiFragment.WifiSelectedListener wifiSelectedListener;

    public SelectableStringAdapter(Context context, List<SelectableString> items, SelectWifiFragment.WifiSelectedListener wifiSelectedListener) {
        this.mContext = context;
        this.items = items;
        this.wifiSelectedListener = wifiSelectedListener;
    }

    @NonNull
    @Override
    public WlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Create a new view, which defines the UI of the list item
        View view = inflater.inflate(R.layout.selectable_string, parent, false);
        return new WlanViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull WlanViewHolder holder, int position) {

        SelectableString p = (SelectableString) items.get(position);

        holder.getDataTV().setText(p.getData());
        holder.getV().setOnClickListener(view -> {
            for (SelectableString s:items) {
                s.setSelected(false);
            }
            p.setSelected(true);
            wifiSelectedListener.onWifiSelected(p.getData());
            this.notifyDataSetChanged();
        });

        if (p.isSelected()) {
            holder.getDataTV().setBackgroundColor(mContext.getResources().getColor(R.color.blue_gray_600));
        } else {
            holder.getDataTV().setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}