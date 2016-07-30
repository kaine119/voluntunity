package com.percepshunnn.voluntunity;

import android.content.Intent;
import android.content.res.TypedArray;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Map Fragment
 */
public class MapScreenFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {


    private SlidingUpPanelLayout infoPanelParent;

    private TextView eventName;
    private TextView eventOrganization;
    private TextView eventDate;
    private TextView eventTime;
    private TextView eventDescription;
    private TextView eventAddress;
    private TextView eventRoles;
    private TextView eventSkills;

    String[][] mTestArray;

    ArrayList<LatLng> locations = new ArrayList();
    @Override
    public void onResume() {
        super.onResume();
        // get a map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_map, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TypedArray ta = getResources().obtainTypedArray(R.array.events);
        int n = ta.length();
        String[][] array = new String[n][];
        for (int i=0; i < n; i++) {
            int id = ta.getResourceId(i, 0);
            if (id > 0) {
                array[i] = getResources().getStringArray(id);
            }
        }
        ta.recycle();
        mTestArray = array;



        // get a map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        infoPanelParent = (SlidingUpPanelLayout) view.findViewById(R.id.info_panel_parent);
        infoPanelParent.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        eventName = (TextView) view.findViewById(R.id.eventName);
        eventOrganization = (TextView) view.findViewById(R.id.eventOrganization);
        eventDate = (TextView) view.findViewById(R.id.eventDate);
        eventTime = (TextView) view.findViewById(R.id.eventTime);
        eventDescription = (TextView) view.findViewById(R.id.eventDescription);
        eventAddress = (TextView) view.findViewById(R.id.eventAddress);
        eventRoles = (TextView) view.findViewById(R.id.eventRoles);
        eventSkills = (TextView) view.findViewById(R.id.eventSkills);
        Log.d("MapFragment", "onViewCreated");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        for(String[] event : mTestArray){
            LatLng location = new LatLng(Double.parseDouble(event[1]), Double.parseDouble(event[2]));
            googleMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(event[0]));
        }


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.379348, 103.849876), 10f));



        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                infoPanelParent.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                Log.d("MapFragment", "onMarkerClick: panel to open");
                displayEventDetails(marker.getTitle());
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

    public void displayEventDetails(String id) {
        String[] eventToDisplay = new String[0];
        for (String[] event : mTestArray) {
            if (event[0].equals(id)) {
                eventToDisplay = event;
                break;
            }
        }
        if (eventToDisplay != null) {
            eventName.setText(eventToDisplay[3]);
            eventOrganization.setText(eventToDisplay[4]);
            eventDate.setText(eventToDisplay[5]);
            eventTime.setText(eventToDisplay[6]);
            eventDescription.setText(eventToDisplay[7]);
            eventAddress.setText(eventToDisplay[8]);
            eventRoles.setText(eventToDisplay[9]);
            eventSkills.setText(eventToDisplay[10]);
        }

    }

}


