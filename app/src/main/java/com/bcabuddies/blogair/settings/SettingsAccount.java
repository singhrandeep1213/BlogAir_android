package com.bcabuddies.blogair.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsAccount extends AppCompatActivity {


    TextInputEditText userBioEt,fullNameEt;
    CircleImageView thumbImage;
    ImageView addImageIcon;
    Button saveBtn;
    PreferenceManager preferenceManager;

    String thumbImageUrl, fullName;
    int lastSpecialRequestsCursorPosition;
    String specialRequests = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_account);

        preferenceManager = new PreferenceManager(this);
        userBioEt = findViewById(R.id.settingsscc_biotv);
        thumbImage = findViewById(R.id.settingsscc_thumbimage);
        addImageIcon = findViewById(R.id.settingsscc_addphotoicon);
        saveBtn = findViewById(R.id.settingsscc_savebtn);
        fullNameEt=findViewById(R.id.settingsscc_nametv);

        thumbImageUrl = preferenceManager.getString(Constants.KEY_THUMB_IMAGE);
        fullName = preferenceManager.getString(Constants.KEY_FUll_NAME);

        //limit user bio input lines
        userBioEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastSpecialRequestsCursorPosition = userBioEt.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userBioEt.removeTextChangedListener(this);

                if (userBioEt.getLineCount() > 5) {
                    userBioEt.setText(specialRequests);
                    userBioEt.setSelection(lastSpecialRequestsCursorPosition);
                } else
                    specialRequests = userBioEt.getText().toString();

                userBioEt.addTextChangedListener(this);
            }
        });

        Glide.with(this).load(thumbImageUrl).into(thumbImage);
        fullNameEt.setText(fullName);



    }
}