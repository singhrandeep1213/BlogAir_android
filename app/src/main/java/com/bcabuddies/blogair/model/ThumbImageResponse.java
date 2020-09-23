package com.bcabuddies.blogair.model;

import com.google.gson.annotations.SerializedName;

public class ThumbImageResponse {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    String message;
    @SerializedName("thumb_image")
    String thumb_image;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }
}
