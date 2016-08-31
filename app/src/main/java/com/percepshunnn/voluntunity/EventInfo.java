package com.percepshunnn.voluntunity;


import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/***
A class describing an event, shown on the map.
 Contains a lat and longidude, address, title of event, organisation hosting event,
 date & time of event, a description, roles available and skills required, and url to signup page.
 Also contains a list of tags, as well as an enum describing tags.
 */

public class EventInfo {

    public enum Tag {
        ELDERLY,
        SOCIAL_SERVICE,
        HEALTH,
        COMMUNITY,
        HUMANITARIAN,
        CHILDREN_AND_YOUTH,
        EDUCATION,
        FUNDRAISING
    }

    public String lat;
    public String lng;
    public String address;
    public String title;
    public String org;
    public String date;
    public String time;
    public String desc;
    public String roles;
    public String skills;
    public String url;
    public List<Tag> tags = new ArrayList<>();

    // Empty constructor, required by Firebase
    public EventInfo() {}

    // Constructor
    public EventInfo(String lat, String lng, String addresss, String title, String org, String date,
                     String time, String desc, String roles, String skills, String url, List<String> tags) {
        this.lat = lat;
        this.lng = lng;
        this.address = addresss;
        this.title = title;
        this.org = org;
        this.date = date;
        this.time = time;
        this.desc = desc;
        this.roles = roles;
        this.skills = skills;
        this.url = url;
        for (String tag : tags) {
            if (tag != null) {
                this.tags.add(Tag.valueOf(tag));
            }
        }
    }

    // Specials
    // This gets a googleMaps LatLng.
    public LatLng getLatLng(){
        return new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
    }

    // This gets a List<String> of tags of this event.
    // All the tags in the list must be converted to strings for firebase to understand it,
    // because it's saved in firebase as an array of strings.
    public List<String> getTags(){
        List<String> toReturn = new ArrayList<>();
        for (Tag tag : this.tags) {
            toReturn.add(tag.name());
            Log.d("", "getTags: toReturn: " + toReturn);
        }

        return toReturn;
    }

    // This sets this.tags according to a List<String> that's passed in.
    // As explained earlier, this conversion is necessary because of how the tags are stored.
    public void setTags(List<String> tagsIn) {
        for (String tag : tagsIn) {
            this.tags.add(Tag.valueOf(tag));
        }
    }

    @Exclude
    public List<Tag> getTagObjects() {
        return this.tags;
    }

    // The rest of the getter/setters.
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
