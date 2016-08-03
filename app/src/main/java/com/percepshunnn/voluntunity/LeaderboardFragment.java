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
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.percepshunnn.voluntunity.leaderboardview.LeaderboardEntry;
import com.percepshunnn.voluntunity.leaderboardview.LeaderboardEntryAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends android.support.v4.app.Fragment{

    List<LeaderboardEntry> entries = new ArrayList<>();
    List<Long> friendsIdList = new ArrayList<>();

    // Things for the list (RecyclerView)
    RecyclerView mRecyclerView;
    LeaderboardEntryAdapter mAdapter;

    // Views
    ProgressBar mProgressBar;
    TextView mLoggedOutText;

    // Database stuff
    DatabaseReference usersRef;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_leaderboard, container, false);


        // Instructions taken from http://hmkcode.com/android-simple-recyclerview-widget-example/

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());


        // <editor-fold desc="Initialise leaderboard screen and view references">
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.leaderboard_loading_bar);
        mLoggedOutText = (TextView) rootView.findViewById(R.id.logged_out_text);

        // 1. Get a reference to the RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.leaderboard_parent);

        // 2. Set LayoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        // 3. create an adapter
        mAdapter = new LeaderboardEntryAdapter(entries);
        // 4. Set adapter
        mRecyclerView.setAdapter(mAdapter);
        // 5. Set item animator (...?) to DefaultAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // </editor-fold>
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        
        // Initialise loading bar so it shows up upon resuming activity
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        if (Profile.getCurrentProfile() != null) {
            // <editor-fold desc="Facebook get friends id and display ">

            // Sometimes the database call comes back faster than the facebook call.
            // This results in nothing being shown, because there's nothing in friendsIdList.
            // Thus we have to chain one after the other.
            // It's probably inefficient as all hell, but it works.

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            usersRef = database.getReference("users/");
            GraphRequest request = GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONArrayCallback() {
                        @Override
                        public void onCompleted(JSONArray objects, GraphResponse response) {
                            String TAG = "FacebookCallback";

                            // If you uncomment this it should work, but I can't test it.
//                        for (int i = 0; i < objects.length(); i++) {
//                            try {
//                                JSONObject user = objects.getJSONObject(i);
//                            } catch (JSONException e) {
//                                Log.e("FacebookCallback", "onCompleted: something bad happened while trying to get leaderboard friends", e);
//                            }
//                        }

                            // I have no friends, to I have to use a dummy array.
                            // http://imgur.com/92Q62wf
                            // If @kaine119 checks (his) db, this maps to a "Tan Bee Hong".
                            friendsIdList.add(1101559985101051L);

                            // <editor-fold desc="Getting users from firebase">
                            usersRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // dataSnapshot is the whole encompassing list
                                    // We'll have to iterate through each child to get the appropriate object
                                    for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                        LeaderboardEntry user = userSnap.getValue(LeaderboardEntry.class);
                                        // only include the entry if he's in the user's friends
                                        if (friendsIdList.contains(user.getId())) {
                                            entries.add(user);
                                        }

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
                            //</editor-fold>
                        }
                    });

            Bundle params = new Bundle();
            params.putString("fields", "name, id");
            request.setParameters(params);
            request.executeAsync();
            // </editor-fold>
        } else {
            mProgressBar.setVisibility(View.GONE);
            mLoggedOutText.setVisibility(View.VISIBLE);
        }
    }


}
