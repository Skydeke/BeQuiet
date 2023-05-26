package com.example.bequiet.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.bequiet.R;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectWifiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectWifiFragment extends Fragment implements AdapterView.OnItemClickListener {

    private WifiSelectedListener wifiSelectedListener;

    public SelectWifiFragment() {
        // Required empty public constructor
    }


    public static SelectWifiFragment newInstance() {
        SelectWifiFragment fragment = new SelectWifiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_wifi, container, false);
        ListView listView = view.findViewById(R.id.list_view_wifi);
        List<String> wifis = Objects.requireNonNull(getAvailableWifiNetworks()).stream().map(x -> x.SSID).collect(Collectors.toList());
        //add test wifis to list more
        wifis.add("Test1");
        wifis.add("Test2");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, wifis);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getAvailableWifiNetworks();
    }

    public void setWifiSelectedListener(WifiSelectedListener wifiSelectedListener) {
        this.wifiSelectedListener = wifiSelectedListener;
    }

    private List<ScanResult> getAvailableWifiNetworks() {
        WifiManager wifiManager = (WifiManager) requireContext().getSystemService(Context.WIFI_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //get permission for Wifi-Access
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{android.Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return null;
        }
        List<ScanResult> wifiList = wifiManager.getScanResults();
        Log.i("Wifi", wifiList.toString());
        return wifiList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String selectedWifi = (String) adapterView.getItemAtPosition(position);
        this.wifiSelectedListener.onWifiSelected(selectedWifi);

        // Set background color of other items to white
        for (int i = 0; i < adapterView.getChildCount(); i++) {
            if (i != position) {
                View child = adapterView.getChildAt(i);
                child.setBackgroundColor(Color.WHITE);
            }
        }

        view.setBackgroundColor(getResources().getColor(R.color.blue_gray_600));
    }

    public interface WifiSelectedListener {
        void onWifiSelected(String name);
    }
}