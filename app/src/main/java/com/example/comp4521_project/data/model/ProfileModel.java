package com.example.comp4521_project.data.model;

public class ProfileModel {

    public class Payload {
        int uid;
        String username;
        String nickname;
        int account_level;
        int account_status;
        String date_modified;
        String date_creation;
        String date_last_login;

    }

    public class Response {
        public String message;
        public Payload payload;

    }
}
