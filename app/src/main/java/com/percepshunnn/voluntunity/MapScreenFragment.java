package com.percepshunnn.voluntunity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
    private Button   signUp;

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
        signUp = (Button) view.findViewById(R.id.signUp);
        Log.d("MapFragment", "onViewCreated");


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            String strJson = getResources().getString(R.string.eventsJson);
            JSONObject jsonRootObject = new JSONObject(strJson);
            JSONArray jsonArray = jsonRootObject.optJSONArray("events");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Double lat = Double.parseDouble(jsonObject.optString("lat").toString());
                Double lng = Double.parseDouble(jsonObject.optString("lng").toString());
                String title = jsonObject.optString("title").toString();
                LatLng location = new LatLng(lat, lng);
                googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(title));
            }
        }catch (JSONException e) {
            e.printStackTrace();
            android.os.Process.killProcess(android.os.Process.myPid());
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

    public void displayEventDetails(final String id) {
        final String[] eventToDisplay = new String[9];
        try {
            String strJson = getResources().getString(R.string.eventsJson);
            JSONObject jsonRootObject = new JSONObject(strJson);
            JSONArray jsonArray = jsonRootObject.optJSONArray("events");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.optString("title").toString().equals(id)) {
                    eventToDisplay[0] = jsonObject.optString("title").toString();
                    eventToDisplay[1] = jsonObject.optString("org").toString();
                    eventToDisplay[2] = jsonObject.optString("date").toString();
                    eventToDisplay[3] = jsonObject.optString("time").toString();
                    eventToDisplay[4] = jsonObject.optString("desc").toString();
                    eventToDisplay[5] = jsonObject.optString("address").toString();
                    eventToDisplay[6] = jsonObject.optString("roles").toString();
                    eventToDisplay[7] = jsonObject.optString("skills").toString();
                    eventToDisplay[8] = jsonObject.optString("url").toString();
                    break;
                }
            }
            if (eventToDisplay != null) {
                eventName.setText(eventToDisplay[0]);
                eventOrganization.setText(eventToDisplay[1]);
                eventDate.setText(eventToDisplay[2]);
                eventTime.setText(eventToDisplay[3]);
                eventDescription.setText(eventToDisplay[4]);
                eventAddress.setText(eventToDisplay[5]);
                eventRoles.setText(eventToDisplay[6]);
                eventSkills.setText(eventToDisplay[7]);
            }
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uriUrl = Uri.parse(eventToDisplay[8]);
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);
                }
            });
        }catch (JSONException e) {
            e.printStackTrace();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


}


