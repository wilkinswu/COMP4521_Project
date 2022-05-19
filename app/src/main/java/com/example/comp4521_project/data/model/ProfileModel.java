package com.example.comp4521_project.data.model;

public class ProfileModel {

    public class Payload {
        public int uid;
        public String username;
        public String nickname;
        public int account_level;
        public int account_status;
        public String date_modified;
        public String date_creation;
        public String date_last_login;

    }

    public class Response {
        public String message;
        public Payload payload;

    }
}
