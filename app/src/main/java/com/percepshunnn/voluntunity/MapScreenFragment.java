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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Map Fragment
 */
public class MapScreenFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {


    private SlidingUpPanelLayout infoPanelParent;

    private GoogleMap mGoogleMap;

    private TextView eventName;
    private TextView eventOrganization;
    private TextView eventDate;
    private TextView eventTime;
    private TextView eventDescription;
    private TextView eventAddress;
    private TextView eventRoles;
    private TextView eventSkills;
    private Button   signUp;

    private HashMap<Marker, EventInfo> mEvents = new HashMap<Marker, EventInfo>();

    // Database
    private DatabaseReference mEventsRef;


    @Override
    public void onResume() {
        super.onResume();
        // get a map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_map, container, false);
        getActivity().setTitle("Voluntunity");
        return v;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // get a map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        // View References
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

        mGoogleMap = googleMap;


        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.379348, 103.849876), 10f));


        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                infoPanelParent.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });


        Log.d("MapFragment", "onMapReady");

        // Go get the events available!
        mEventsRef = FirebaseDatabase.getInstance().getReference("tasks");
        mEventsRef.addListenerForSingleValueEvent(firebaseCallback);
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                EventInfo eventToDisplay = mEvents.get(marker);
                displayEventDetails(eventToDisplay);
                infoPanelParent.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                return true;
            }
        });
    }

    private ValueEventListener firebaseCallback = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot event : dataSnapshot.getChildren()) {
                EventInfo eventToAdd = event.getValue(EventInfo.class);
                Marker marker = mGoogleMap.addMarker(
                        new MarkerOptions()
                        .position(eventToAdd.getLatLng())
                );
                mEvents.put(marker, eventToAdd);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void displayEventDetails(final EventInfo event) {
        eventName.setText(event.getTitle());
        eventOrganization.setText(event.getOrg());
        eventDate.setText(event.getDate());
        eventTime.setText(event.getTime());
        eventDescription.setText(event.getDesc());
        eventAddress.setText(event.getAddress());
        eventRoles.setText(event.getRoles());
        eventSkills.setText(event.getSkills());

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse(event.getUrl());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mEventsRef.removeEventListener(firebaseCallback);
    }
}


