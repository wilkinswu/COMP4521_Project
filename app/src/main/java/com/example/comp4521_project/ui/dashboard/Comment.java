package com.example.comp4521_project.ui.dashboard;

import android.location.Location;

public class Comment {
    int id;
    String name;
    String content;
    String location;
    String Type;
    public  Comment(){

    }

    public Comment(String name, String loc, String type, String content){
        this.name = name;
        this.location = loc;
        this.content = content;
        this.Type = type;
    }

    public Comment(String name, String loc, String type, String content, int id){
        this.name = name;
        this.location = loc;
        this.content = content;
        this.Type = type;
        this.id = id;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {return location;}

    public void setLocation(String location) {this.location = location;}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {return Type;}

    public void setType(String type) {Type = type;}
}
