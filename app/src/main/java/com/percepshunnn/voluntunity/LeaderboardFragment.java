package com.percepshunnn.voluntunity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.percepshunnn.voluntunity.leaderboardview.LeaderboardEntry;
import com.percepshunnn.voluntunity.leaderboardview.LeaderboardEntryAdapter;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends android.support.v4.app.Fragment{

    List<LeaderboardEntry> entries = new ArrayList<>();

    // Things for the list (RecyclerView)
    RecyclerView mRecyclerView;
    LeaderboardEntryAdapter mAdapter;

    // Loading bar
    ProgressBar mProgressBar;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_leaderboard, container, false);

        // Instructions taken from http://hmkcode.com/android-simple-recyclerview-widget-example/

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.leaderboard_loading_bar);

        // 1. Get a reference to the RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.leaderboard_parent);

        // 2. Set LayoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Getting users from firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users/");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // dataSnapshot is the whole encompassing list
                // We'll have to iterate through each child to get the appropriate object
                for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                    LeaderboardEntry user = userSnap.getValue(LeaderboardEntry.class);
                    Log.d("FirebaseCallback", "onChildAdded: " + user.getHours());
                    entries.add(user);

                    // Update the recycler view that it has new things to show
                    mAdapter.notifyDataSetChanged();

                    mRecyclerView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // 3. create an adapter
        mAdapter = new LeaderboardEntryAdapter(entries);
        // 4. Set adapter
        mRecyclerView.setAdapter(mAdapter);
        // 5. Set item animator (...?) to DefaultAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());






        return rootView;
    }



}
