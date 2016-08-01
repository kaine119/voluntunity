package com.percepshunnn.voluntunity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.percepshunnn.voluntunity.leaderboardview.LeaderboardEntry;
import com.percepshunnn.voluntunity.leaderboardview.LeaderboardEntryAdapter;

public class LeaderboardFragment extends android.support.v4.app.Fragment{


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_leaderboard, container, false);

        // Instructions taken from http://hmkcode.com/android-simple-recyclerview-widget-example/

        // 1. Get a reference to the RecyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.leaderboard_parent);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Data for recycler view
        // TODO: fetch this from a xml value file
        LeaderboardEntry[] entries = {
                new LeaderboardEntry("Tan Ah Seng", 1000),
                new LeaderboardEntry("Me", 900)
        };

        // 2. Set LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 3. create an adapter
        LeaderboardEntryAdapter adapter = new LeaderboardEntryAdapter(entries);
        // 4. Set adapter
        recyclerView.setAdapter(adapter);
        // 5. Set item animator (...?) to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());




        return rootView;
    }



}
