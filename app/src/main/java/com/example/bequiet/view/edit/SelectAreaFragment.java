package com.example.bequiet.view.edit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bequiet.model.CoordinateCalculator;
import com.example.bequiet.R;
import com.example.bequiet.view.CircleOverlay;
import com.example.bequiet.view.GPSCoordinateSelectedListener;

import org.osmdroid.api.IGeoPoint;
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
public class SelectAreaFragment extends Fragment implements MapListener {

    private static final String KEY_LATITUDE = "LATITUDE";
    private static final String KEY_LONGITUDE = "LONGITUDE";
    private static final String KEY_ZOOM = "ZOOM";
    private static final String KEY_DISABLE_MOVEMENT = "DISABLE_MVMNT";

    private MapView map;
    private CircleOverlay mCircleOverlay;
    private double lat = 47.8127457112777;
    private double lon = 9.656508679012063;
    private int zoom = 20;
    private boolean disableControlls = false;

    public SelectAreaFragment() {
        // Required empty public constructor
    }

    private GPSCoordinateSelectedListener gpsCoordinateSelectedListener;

    public static SelectAreaFragment newInstance(double lat, double longitude, int zoom, boolean disableControlls) {
        SelectAreaFragment fragment = new SelectAreaFragment();
        Bundle args = new Bundle();
        args.putDouble(KEY_LATITUDE, lat);
        args.putDouble(KEY_LONGITUDE, longitude);
        args.putInt(KEY_ZOOM, zoom);
        args.putBoolean(KEY_DISABLE_MOVEMENT, disableControlls);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getDouble(KEY_LATITUDE);
            lon = getArguments().getDouble(KEY_LONGITUDE);
            zoom = getArguments().getInt(KEY_ZOOM);
            disableControlls = getArguments().getBoolean(KEY_DISABLE_MOVEMENT);
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
        map.setMultiTouchControls(!disableControlls);

        if (disableControlls) {
            // Disable all touch interaction
            map.setOnTouchListener((v, event) -> true);
        }


        mCircleOverlay = new CircleOverlay(
                map.getContext(),
                40, map.getProjection().metersToPixels(40, lat, zoom),
                map.getProjection().metersToPixels(1, lat, zoom),
                lat,
                lon);
        map.getOverlayManager().add(mCircleOverlay);
        map.addMapListener(this);

        Log.i("GeoCoordinate in rage", "" +
                CoordinateCalculator.isCoordinateInRange(
                        new GeoPoint(
                                map.getMapCenter().getLatitude(),
                                map.getMapCenter().getLongitude()),
                        mCircleOverlay.getRadiusInMeter(),
                        new GeoPoint(lat, lon)));

        IMapController mapController = map.getController();
        mapController.setZoom(zoom);
        mapController.setCenter(new GeoPoint(lat, lon)); //geographic point of Hochschule Ravensbrug-Weingarten
        super.onViewCreated(view, savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    public void setGpsCoordinateSelectedListener(GPSCoordinateSelectedListener gpsCoordinateSelectedListener) {
        this.gpsCoordinateSelectedListener = gpsCoordinateSelectedListener;
    }

    @Override
    public boolean onScroll(ScrollEvent event) {
        IGeoPoint center = map.getMapCenter();
        if (this.gpsCoordinateSelectedListener != null)
            this.gpsCoordinateSelectedListener.onGPSCoordinateSelected(center.getLatitude(), center.getLongitude());
        return false;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        mCircleOverlay.setReferenceScale(map.getProjection().metersToPixels(1, lat, map.getZoomLevel()));
        Log.i("Meter", "" + mCircleOverlay.getRadiusInMeter()); //radius scales
        return false;
    }
}

