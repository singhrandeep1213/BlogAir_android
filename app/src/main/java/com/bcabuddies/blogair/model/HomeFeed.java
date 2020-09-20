package com.bcabuddies.blogair.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class HomeFeed {


    @Expose
    @SerializedName("post")
    private List<Post> Post;
    @Expose
    @SerializedName("error")
    private boolean error;
    @Expose
    @SerializedName("message")
    private String message;


    public List<Post> getPost() {
        return Post;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public  class Post{

        @Expose
        @SerializedName("full_name")
        String full_name;
        @Expose
        @SerializedName("thumb_image")
        String thumb_image;
        @Expose
        @SerializedName("pid")
        String pid;
        @Expose
        @SerializedName("desc")
        String desc;
        @Expose
        @SerializedName("post_image")
        String post_image;
        @Expose
        @SerializedName("likes_count")
        int likes_count;
        @Expose
        @SerializedName("time_stamp")
        Date time_stamp;
        @Expose
        @SerializedName("uid")
        String uid;
        @Expose
        @SerializedName("post_heading")
        String post_heading;
        @Expose
        @SerializedName("is_bookmarked")
        String is_bookmarked;

        public String getIs_bookmarked() {
            return is_bookmarked;
        }

        public void setIs_bookmarked(String is_bookmarked) {
            this.is_bookmarked = is_bookmarked;
        }

        public String getPost_heading() {
            return post_heading;
        }

        public void setPost_heading(String post_heading) {
            this.post_heading = post_heading;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getThumb_image() {
            return thumb_image;
        }

        public void setThumb_image(String thumb_image) {
            this.thumb_image = thumb_image;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPost_image() {
            return post_image;
        }

        public void setPost_image(String post_image) {
            this.post_image = post_image;
        }

        public int getLikes_count() {
            return likes_count;
        }

        public void setLikes_count(int likes_count) {
            this.likes_count = likes_count;
        }

        public Date getTime_stamp() {
            return time_stamp;
        }

        public void setTime_stamp(Date time_stamp) {
            this.time_stamp = time_stamp;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }


}
