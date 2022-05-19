package com.example.comp4521_project.ui.dashboard;

import android.location.Location;

public class Comment {
    String name;
    String content;
    Location location;
    String Type;
    public  Comment(){

    }

    public Comment(String name, Location loc, String type, String content){
        this.name = name;
        this.location = loc;
        this.content = content;
        this.Type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {return location;}

    public void setLocation(Location location) {this.location = location;}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
