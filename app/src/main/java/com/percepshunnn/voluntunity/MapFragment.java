package com.percepshunnn.voluntunity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Map Fragment
 */
public class MapFragment extends android.support.v4.app.Fragment {

    private GoogleMap googleMap;
    private MapView mapView;
    LatLng mLocation = new LatLng(1.379348, 103.849876);

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_map, container, false);

        //Inicio el mapa
        mapView = (MapView)v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        googleMap = mapView.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 17));

        Marker mMarker = googleMap.addMarker(new MarkerOptions().position(mLocation).title("Nanyang Polytechinc").snippet("NYP")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));

        return v;
    }


    }

    // TODO: Add google maps code that actually works
