package com.bcabuddies.blogair.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class SinglePostData {
    @Expose
    @SerializedName("error")
    private boolean error;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("is_liked_by_current_user")
    private int is_liked_by_current_user;
    @Expose
    @SerializedName("is_bookmarked")
    private int is_bookmarked;
    @Expose
    @SerializedName("likes_count")
    private int likes_count;
    @SerializedName("post_data")
    private List<SinglePostData.postData> postData;

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

    public int getIs_liked_by_current_user() {
        return is_liked_by_current_user;
    }

    public void setIs_liked_by_current_user(int is_liked_by_current_user) {
        this.is_liked_by_current_user = is_liked_by_current_user;
    }

    public int getIs_bookmarked() {
        return is_bookmarked;
    }

    public void setIs_bookmarked(int is_bookmarked) {
        this.is_bookmarked = is_bookmarked;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public List<SinglePostData.postData> getPostData() {
        return postData;
    }

    public void setPostData(List<SinglePostData.postData> postData) {
        this.postData = postData;
    }

    public class postData{
        String post_heading;
        String desc;
        String pid;
        Date time_stamp;
        String uid;


        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPost_heading() {
            return post_heading;
        }

        public void setPost_heading(String post_heading) {
            this.post_heading = post_heading;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
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
