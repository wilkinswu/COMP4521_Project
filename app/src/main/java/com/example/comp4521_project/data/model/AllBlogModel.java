package com.example.comp4521_project.data.model;

public class AllBlogModel {
    public class Response {
        public String message;
        public AllBlogModel.Payload payload;
    }

    public class Payload {
        public int item_count;
        public int current_page;
        public int max_page;
        public Blog[] content;
    }

    public class Blog{
        public int bid;
        public String comment_bid;
        public int author_uid;
        public String author_name;
        public String blog_info;
        public int blog_status;
        public String blog_location;
        public int blog_thumbs;
        public String thumb_author_list;
        public int blog_type;
        public String date_modified;
        public String date_creation;
    }
}
