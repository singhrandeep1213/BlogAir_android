package com.bcabuddies.blogair.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int mode = 0;
    private static final String REFNAME = "JWTTOKENSESSION";



    private Context context;

    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(REFNAME, mode);
        editor = sharedPreferences.edit();
        editor.apply();

    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key){
        return sharedPreferences.getString(key, "");
    }

    public void clearPrefrenceManager(){
        editor.clear();
        editor.commit();
    }

   /* public void createSession(String emailId,String fullName, String tokenValue, String thumbImage,String uid) {
        editor.putString(KEY_EMAIL_ID,emailId);
        editor.putString(KEY_USER_NAME, fullName);
        editor.putString(KEY_JWT_TOKEN, tokenValue);
        editor.putString(KEY_THUMB_IMAGE, thumbImage);
        editor.putString(KEY_UID,uid);
        editor.commit();

    }*/


}
