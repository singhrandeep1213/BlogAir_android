package com.bcabuddies.blogair.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bcabuddies.blogair.R;

public class SettingsMain extends AppCompatActivity {

    ConstraintLayout accountSettingLayout ,changePasswordLayout, blockedUsersLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);

        accountSettingLayout=findViewById(R.id.settings_accountlaoyut);
        changePasswordLayout=findViewById(R.id.settings_changepasslayout);
        blockedUsersLayout=findViewById(R.id.settings_blockeduserlayout);

        accountSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsMain.this,SettingsAccount.class));
            }
        });

        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsMain.this,SettingsChangePassword.class));
            }
        });

        blockedUsersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsMain.this,SettingsBlockedUsers.class));
            }
        });
    }
}