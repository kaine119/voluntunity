package com.percepshunnn.voluntunity.leaderboardview;

public class LeaderboardEntry {
    private String name;
    private int score;

    public LeaderboardEntry (String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
