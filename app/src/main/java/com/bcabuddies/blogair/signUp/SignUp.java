package com.bcabuddies.blogair.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.blogair.APIInterface;
import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.home.MainActivity;
import com.bcabuddies.blogair.model.LoginToken;
import com.bcabuddies.blogair.signIn.SignIn;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bcabuddies.blogair.welcome.Welcome;
import com.google.android.material.textfield.TextInputLayout;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {

    TextView signInBtn;
    TextInputLayout name, email, pass, cpass;
    Button createAccountButton;
    String fullName, emailId, password, confirmPassword, uid;
    private static final String TAG = "SignUp";
    String loginToken = "", thumb_image="";
    PreferenceManager preferenceManager;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        signInBtn = findViewById(R.id.signup_signintv);
        name = findViewById(R.id.signup_namelayout);
        email = findViewById(R.id.signup_emaillayout);
        pass = findViewById(R.id.signup_passlayout);
        cpass = findViewById(R.id.signup_cpasslayout);
        createAccountButton = findViewById(R.id.signup_createbtn);
        backBtn=findViewById(R.id.signup_backbtn);
        preferenceManager=new PreferenceManager(getApplicationContext());

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, SignIn.class));
                SignUp.this.finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,Welcome.class));
                SignUp.this.finish();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName = name.getEditText().getText().toString();
                emailId = email.getEditText().getText().toString();
                password = pass.getEditText().getText().toString();
                confirmPassword = cpass.getEditText().getText().toString();
                uid = UUID.randomUUID().toString();

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUp.this, "Password does not match", Toast.LENGTH_SHORT).show();
                } else {
                    callApi();
                }
            }
        });
    }

    private void callApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        APIInterface apiInterface = retrofit.create(APIInterface.class);

        Call<LoginToken> tokenCall = apiInterface.registerUser(fullName, emailId, password, uid);

        tokenCall.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Failed to register", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onResponse: register  error code :  " + response.code());
                }

                loginToken=response.body().getToken();
                thumb_image=response.body().getThumb_image();
                preferenceManager.saveString(Constants.KEY_JWT_TOKEN, loginToken);
                preferenceManager.saveString(Constants.KEY_FUll_NAME, fullName);
                preferenceManager.saveString(Constants.KEY_UID, uid);
                Log.e(TAG, "onResponse: reg token:  "+loginToken);
                startActivity(new Intent(SignUp.this, MainActivity.class));
                SignUp.this.finish();
            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {

            }
        });


    }


}
