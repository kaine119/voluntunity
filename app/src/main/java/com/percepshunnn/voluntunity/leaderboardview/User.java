package com.percepshunnn.voluntunity.leaderboardview;

import android.util.Log;

import java.util.List;

public class User {
    private String name;
    private int score;
    private List<String> skills;
    private long id;
    private int hours;
    private int reputation;

    public User(){}

    public User(String name, List<String> skills, long id, int reputation, int hours) {
        this.name = name;
        this.skills = skills;
        this.id = id;
        this.hours = hours;
        this.reputation = reputation;
        this.score = hours * reputation;
        Log.d("User made", "score: "+this.score);
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getScore(){
        return this.hours * this.reputation;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHours() {
        return this.hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getReputation() {
        return this.reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

}
