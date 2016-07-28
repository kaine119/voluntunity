package com.percepshunnn.voluntunity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Text;

/**
 * Map Fragment
 */
public class MapScreenFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;
    LatLng mLocation = new LatLng(1.379348, 103.849876);

    private SlidingUpPanelLayout infoPanelParent;

    public MapScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        // get a map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_map, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get a map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        infoPanelParent = (SlidingUpPanelLayout) view.findViewById(R.id.info_panel_parent);
        infoPanelParent.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        Log.d("MapFragment", "onViewCreated");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 10f));
        googleMap.addMarker(new MarkerOptions()
                .position(mLocation)
                .title("Marker"));
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                infoPanelParent.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                Log.d("MapFragment", "onMarkerClick: panel to open");
                return true;
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                infoPanelParent.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });
        Log.d("MapFragment", "onMapReady");
    }
}


