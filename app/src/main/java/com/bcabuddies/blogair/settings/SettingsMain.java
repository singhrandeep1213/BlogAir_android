package com.bcabuddies.blogair.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsMain extends AppCompatActivity {

    ConstraintLayout accountSettingLayout, changePasswordLayout, blockedUsersLayout;
    CircleImageView thumbImageView;
    String fullName,thumbImageUrl;
    PreferenceManager preferenceManager;
    TextView fullNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);

        preferenceManager = new PreferenceManager(this);
        accountSettingLayout = findViewById(R.id.settings_accountlaoyut);
        changePasswordLayout = findViewById(R.id.settings_changepasslayout);
        blockedUsersLayout = findViewById(R.id.settings_blockeduserlayout);
        thumbImageView = findViewById(R.id.settings_thumbicon);
        fullNameTv=findViewById(R.id.settings_fullnameTv);

        thumbImageUrl = preferenceManager.getString(Constants.KEY_THUMB_IMAGE);
        fullName = preferenceManager.getString(Constants.KEY_FUll_NAME);

        Glide.with(this).load(thumbImageUrl).into(thumbImageView);
        fullNameTv.setText(fullName);

        //click listeners
        accountSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsMain.this, SettingsAccount.class));
            }
        });
        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsMain.this, SettingsChangePassword.class));
            }
        });
        blockedUsersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsMain.this, SettingsBlockedUsers.class));
            }
        });
    }
}