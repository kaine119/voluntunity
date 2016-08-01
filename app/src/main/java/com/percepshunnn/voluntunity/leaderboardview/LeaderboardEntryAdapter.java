package com.percepshunnn.voluntunity.leaderboardview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.percepshunnn.voluntunity.R;

import java.util.List;


public class LeaderboardEntryAdapter extends RecyclerView.Adapter<LeaderboardEntryAdapter.ViewHolder> {
    private LeaderboardEntry[] entries;


    public LeaderboardEntryAdapter(LeaderboardEntry[] entries) {
        this.entries = entries;
    }

    @Override
    public LeaderboardEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_leaderboard, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView scoreText;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            nameText = (TextView) itemLayoutView.findViewById(R.id.leaderboard_item_name);
            scoreText = (TextView) itemLayoutView.findViewById(R.id.leaderboard_item_score);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameText.setText(entries[position].getName());
        holder.scoreText.setText(Integer.toString(entries[position].getScore()));
    }

    @Override
    public int getItemCount() {
        return entries.length;
    }
}
