package com.example.bequiet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectAreaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectAreaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private MapView map = null;

    public SelectAreaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SelectAreaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectAreaFragment newInstance(String param1) {
        SelectAreaFragment fragment = new SelectAreaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_area, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        map = (MapView) view.findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);


        CircleOverlay mCircleOverlay;
        mCircleOverlay = new CircleOverlay(map.getContext(), 40, map.getProjection().metersToPixels(40, 47.8127457112777, 20), map.getProjection().metersToPixels(1, 47.8127457112777, 20), 47.8127457112777, 9.656508679012063);
        map.getOverlayManager().add(mCircleOverlay);

        map.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                mCircleOverlay.setReferenceScale(map.getProjection().metersToPixels(1, 47.8127457112777, map.getZoomLevel()));
                Log.i("Meter", "" + mCircleOverlay.getRadiusInMeter()); //radius scales
                return false;
            }
        });

        Log.i("GeoCoordinate in rage", ""+CoordinateCalculator.isCoordinateInRange(new GeoPoint(map.getMapCenter().getLatitude(),map.getMapCenter().getLongitude()),mCircleOverlay.getRadiusInMeter(),new GeoPoint(47.8127457112777,9.656508679012063)));

        IMapController mapController = map.getController();
        mapController.setZoom(20);
        mapController.setCenter(new GeoPoint(47.8127457112777, 9.656508679012063)); //geographic point of Hochschule Ravensbrug-Weingarten
        super.onViewCreated(view, savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}