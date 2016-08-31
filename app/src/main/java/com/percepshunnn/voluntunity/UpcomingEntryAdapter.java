package com.percepshunnn.voluntunity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

/**
 * Created by Mui Family on 31/08/2016.
 */
public class UpcomingEntryAdapter extends RecyclerView.Adapter<UpcomingEntryAdapter.ViewHolder> {
    private List<EventInfo> events;
    private View infoPanel;

    public UpcomingEntryAdapter(List<EventInfo> events, View infoPanel) {
        this.events = events;
        this.infoPanel = infoPanel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.upcoming_entry_title);
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View entryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_upcoming, parent, false);
        return new ViewHolder(entryView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        EventInfo event = events.get(position);
        holder.title.setText(event.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d("", "onClick: " + position);
                ((TextView) infoPanel.findViewById(R.id.eventName)).setText(events.get(position).getTitle());
                ((TextView) infoPanel.findViewById(R.id.eventOrganization)).setText(events.get(position).getOrg());
                ((TextView) infoPanel.findViewById(R.id.eventDate)).setText(events.get(position).getDate());
                ((TextView) infoPanel.findViewById(R.id.eventTime)).setText(events.get(position).getTime());
                ((TextView) infoPanel.findViewById(R.id.eventDescription)).setText("");
                ((TextView) infoPanel.findViewById(R.id.eventAddress)).setText(events.get(position).getAddress());
                ((TextView) infoPanel.findViewById(R.id.eventRoles)).setText(events.get(position).getRoles());
                ((TextView) infoPanel.findViewById(R.id.eventSkills)).setText(events.get(position).getSkills());
                infoPanel.findViewById(R.id.signUp).setVisibility(View.GONE);
                ((SlidingUpPanelLayout) infoPanel.findViewById(R.id.upcoming_info_panel_parent)).setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.events.size();
    }
}
