package com.bcabuddies.blogair.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bcabuddies.blogair.retrofit.APIInterface;
import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsChangePassword extends AppCompatActivity {

    TextInputEditText oldPassEt, newPassEt, cPassEt;
    String oldPassword, newPassword, confirmPassword;
    Button saveBtn;
    String token;
    PreferenceManager preferenceManager;
    ImageView backIcon;
    private static final String TAG = "SettingsChangePassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_change_password);

        preferenceManager = new PreferenceManager(this);
        token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        oldPassEt = findViewById(R.id.settingschangep_oldtv);
        newPassEt = findViewById(R.id.settingschangep_newtv);
        cPassEt = findViewById(R.id.settingschangep_cpasstv);
        backIcon = findViewById(R.id.settingschangep_backicon);
        saveBtn = findViewById(R.id.settingschangep_savebtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = oldPassEt.getText().toString();
                newPassword = newPassEt.getText().toString();
                confirmPassword = cPassEt.getText().toString();

                if (!(newPassword.equals(confirmPassword))) {
                    Toast.makeText(SettingsChangePassword.this, "Password does not match", Toast.LENGTH_SHORT).show();
                } else {
                    callAPi();
                }


            }
        });


    }

    private void callAPi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        APIInterface updatePasswordApi = retrofit.create(APIInterface.class);

        Call<ResponseBody> updateCall = updatePasswordApi.changePassword("bearer " + token, oldPassword, newPassword);
    
        updateCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                    Log.e(TAG, "onResponse: error while changing password:  "+ response.code() );
                    Toast.makeText(SettingsChangePassword.this, "Some error occured", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SettingsChangePassword.this, "Password Successfully updated", Toast.LENGTH_SHORT).show();
                    SettingsChangePassword.this.finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SettingsChangePassword.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        
    }
}