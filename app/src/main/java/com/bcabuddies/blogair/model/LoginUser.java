package com.bcabuddies.blogair.model;

public class LoginUser {

    String msg, uid, full_name, email_id;

    public LoginUser(String msg, String uid, String full_name, String email_id) {
        this.msg = msg;
        this.uid = uid;
        this.full_name = full_name;
        this.email_id = email_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }
}