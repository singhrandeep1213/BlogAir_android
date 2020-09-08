package com.bcabuddies.blogair.model;

public class HomeFeed {


    String full_name;
    String thumb_image;
    String pid;
    String desc;
    String post_image;
    int likes_count;
    String time_stamp;
    String uid;
    String post_heading;

    public HomeFeed(String full_name, String thumb_image, String pid, String desc, String post_image, int likes_count, String time_stamp, String uid, String post_heading) {
        this.full_name = full_name;
        this.thumb_image = thumb_image;
        this.pid = pid;
        this.desc = desc;
        this.post_image = post_image;
        this.likes_count = likes_count;
        this.time_stamp = time_stamp;
        this.uid = uid;
        this.post_heading = post_heading;
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

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
