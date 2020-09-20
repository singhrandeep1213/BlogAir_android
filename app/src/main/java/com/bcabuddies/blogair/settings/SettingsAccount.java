package com.bcabuddies.blogair.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import com.bcabuddies.blogair.R;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsAccount extends AppCompatActivity {

    TextInputEditText userBioEt;
    int lastSpecialRequestsCursorPosition;
    String specialRequests="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_account);

        userBioEt=findViewById(R.id.settingsscc_biotv);

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
                }
                else
                    specialRequests = userBioEt.getText().toString();

                userBioEt.addTextChangedListener(this);
            }
        });


    }
}