package com.bcabuddies.blogair.model;

public class UserPosts {

    String pid;
    String  post_image;

    public UserPosts(String pid, String post_image) {
        this.pid = pid;
        this.post_image = post_image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }
}
