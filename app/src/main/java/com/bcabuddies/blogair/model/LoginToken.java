package com.bcabuddies.blogair.model;

public class LoginToken {


    String message,error,token,name,thumb_image,uid,bio;

    public LoginToken(String message, String error, String token, String name, String thumb_image, String uid, String bio) {
        this.message = message;
        this.error = error;
        this.token = token;
        this.name = name;
        this.thumb_image = thumb_image;
        this.uid = uid;
        this.bio = bio;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
