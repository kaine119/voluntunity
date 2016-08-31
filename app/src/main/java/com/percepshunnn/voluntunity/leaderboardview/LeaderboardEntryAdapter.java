package com.percepshunnn.voluntunity.leaderboardview;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.percepshunnn.voluntunity.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LeaderboardEntryAdapter extends RecyclerView.Adapter<LeaderboardEntryAdapter.ViewHolder> {
    private List<User> entries;


    public LeaderboardEntryAdapter(List<User> entries) {
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
        public ImageView trophyIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            nameText = (TextView) itemLayoutView.findViewById(R.id.leaderboard_item_name);
            scoreText = (TextView) itemLayoutView.findViewById(R.id.leaderboard_item_score);
            trophyIcon = (ImageView) itemLayoutView.findViewById(R.id.leaderboard_item_trophy);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameText.setText(entries.get(position).getName());
        holder.scoreText.setText(Integer.toString(entries.get(position).getScore()));
        switch (position) {
            case 0:
                holder.trophyIcon.setColorFilter(Color.argb(255, 220, 185, 60));
                break;
            case 1:
                holder.trophyIcon.setColorFilter(Color.argb(255, 183, 189, 192));
                break;
            case 2:
                holder.trophyIcon.setColorFilter(Color.argb(255, 200, 128, 59));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.entries.size();
    }
}
