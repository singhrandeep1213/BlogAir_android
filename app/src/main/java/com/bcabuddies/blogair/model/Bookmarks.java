package com.bcabuddies.blogair.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Bookmarks {

    @Expose
    @SerializedName("bookmarked_posts")
    private List<bookmarks> bookmarked_posts;
    @Expose
    @SerializedName("error")
    private boolean error;
    @Expose
    @SerializedName("message")
    private String message;


    public List<bookmarks> getBookmarked_posts() {
        return bookmarked_posts;
    }

    public void setBookmarked_posts(List<bookmarks> bookmarked_posts) {
        this.bookmarked_posts = bookmarked_posts;
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

    public class bookmarks{

        @Expose
        @SerializedName("bid")
        private String bid;
        @Expose
        @SerializedName("created_on")
        private Date created_on;
        @Expose
        @SerializedName("pid")
        private String pid;
        @Expose
        @SerializedName("uid")
        private String uid;
        @Expose
        @SerializedName("post_image")
        private String post_image;

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public Date getCreated_on() {
            return created_on;
        }

        public void setCreated_on(Date created_on) {
            this.created_on = created_on;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getPost_image() {
            return post_image;
        }

        public void setPost_image(String post_image) {
            this.post_image = post_image;
        }
    }

}
