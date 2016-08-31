
package com.percepshunnn.voluntunity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UpcomingFragment extends android.support.v4.app.Fragment {
    private RecyclerView mRecyclerView;
    private UpcomingEntryAdapter mAdapter;
    private List<EventInfo> mEvents = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_upcoming, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.upcoming_list_parent);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new UpcomingEntryAdapter(mEvents, v);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());



        mEvents.add(new EventInfo("", "", "5, LORONG NAPIRI, BRIGHT VISION HOSPITAL Level 2 Reception, Singapore 547530",
                "Listening Hearts", "Bright Vision Hospital", "9:30 AM to 11:30 AM", "13 Aug 2016", "", "Volunteer", "NA", "",
                new ArrayList<String>()));
        mEvents.add(new EventInfo("", "", "20, SEMBAWANG CRESCENT,Singapore 757092", "Social Activity with the elderly residents",
                "", "9:30 AM to 11:30 AM", "13 Aug 2016", "", "Volunteer", "NA", "",
                new ArrayList<String>()));
        mEvents.add(new EventInfo("", "", "Bishan Bus Interchange 514 Bishan Street 13 Singapore 570514", "Habitat Flag Day",
                "Habitat for Humanity Singapore Ltd", "9 am to 1 pm", "06-Aug-16", "", "Collect donations from public.", "NA", "",
                new ArrayList<String>()));
        mEvents.add(new EventInfo("", "", "957 Hougang St 91 #01-280", "BCARE Tuition Programme for Low-Income Families",
                "Bethesda Community Assistance And Relationship Enrichment Centre", "7pm to 9.45pm", "08-Aug-16", "", "To provide small-group tuition to children from low-income families (Upper Primary and lower secondary students) for the subjects English and Mathematics", "- 18 years old and above - Achieved satisfactory results in O/ A levels/ University - Able to teach/ mentor primary and secondary school students in their studies - Experience working with children and youths is a bonus", "",
                new ArrayList<String>()));
        mEvents.add(new EventInfo("", "", "Ren Ci Community Hospital 71 Irrawaddy Road Singapore 329562",
                "National Day Celebration with the Elderly@Ren Ci Community Hospital", "SG Cares VL Group", "10.00am to 12.00pm",
                "09-Aug-16", "", "Volunteers will interact and befriend our elderly, help them organise their thoughts and write them onto the DIY card.",
                "Able to communicate with the elderly. Knowledge of one or more local languages and/or dialects is a bonus. Volunteers with pleasant disposition and enjoy working with people.", "",
                new ArrayList<String>()));
        mEvents.add(new EventInfo("", "", "10 Jalan Batu #01-06", "Game Sessions with Elderly",
                "St. Hildas Community Services Centre", "1.00pm - 3.00pm", "12-Aug-16", "", "Befriending. Preparing and distributing refreshment. Assist in the conduct of games and activities.",
                "Ability to communicate in Mandarin. Dialect would be a bonus. To be able to commit at least 2 times a month.", "",
                new ArrayList<String>()));
        mAdapter.notifyDataSetChanged();
        return v;
    }


}