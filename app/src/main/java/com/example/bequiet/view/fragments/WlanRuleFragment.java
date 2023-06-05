package com.example.bequiet.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bequiet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WlanRuleFragment extends Fragment {

    private static final String WLAN_NAME_KEY = "NAME";

    private String name;

    public WlanRuleFragment() {
        // Required empty public constructor
    }

    public WlanRuleFragment(String name) {
        this.name = name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            name = getArguments().getString(WLAN_NAME_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wlan_rule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView textViewName = view.findViewById(R.id.textViewWlanName);
        textViewName.setText(name);
        ImageView imageViewWlan = view.findViewById(R.id.imageViewWlan);
        imageViewWlan.setImageResource(R.mipmap.ic_wlan_foreground);
        super.onViewCreated(view, savedInstanceState);
    }

}