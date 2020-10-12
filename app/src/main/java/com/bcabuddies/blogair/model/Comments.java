package com.bcabuddies.blogair.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Comments {

    @Expose
    @SerializedName("comments")
    private List<Comments.comments> comments;
    @Expose
    @SerializedName("error")
    private boolean error;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("post_likes_count")
    private String post_likes_count;

    public String getPost_likes_count() {
        return post_likes_count;
    }

    public void setPost_likes_count(String post_likes_count) {
        this.post_likes_count = post_likes_count;
    }

    public List<Comments.comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments.comments> comments) {
        this.comments = comments;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class comments{

        @SerializedName("cid")
        private String cid;
        @SerializedName("comment_description")
        private String comment_description;
        @SerializedName("created_on")
        private String created_on;
        @SerializedName("thumb_image")
        private String thumb_image;
        @SerializedName("uid")
        private String uid;
        @SerializedName("full_name")
        private String full_name;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getComment_description() {
            return comment_description;
        }

        public void setComment_description(String comment_description) {
            this.comment_description = comment_description;
        }

        public String getCreated_on() {
            return created_on;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public String getThumb_image() {
            return thumb_image;
        }

        public void setThumb_image(String thumb_image) {
            this.thumb_image = thumb_image;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }
    }
}
