package com.bcabuddies.blogair.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfile {



    @Expose
    @SerializedName("error")
    private boolean error;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("following_count")
    private String following_count;
    @Expose
    @SerializedName("followers_count")
    private String followers_count;
    @Expose
    @SerializedName("type")
    private  String type;
    @Expose
    @SerializedName("bio")
    private String bio;
    @Expose
    @SerializedName("post")
    private List<UserProfile.Post> Post;


    public boolean isError() {
        return error;
    }


    public void setError(boolean error) {
        this.error = error;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(String following_count) {
        this.following_count = following_count;
    }

    public String getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(String followers_count) {
        this.followers_count = followers_count;
    }

    public List<UserProfile.Post> getPost() {
        return Post;
    }

    public void setPost(List<UserProfile.Post> post) {
        Post = post;
    }

    public class Post{

        String pid;
        String  post_image;


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


}
