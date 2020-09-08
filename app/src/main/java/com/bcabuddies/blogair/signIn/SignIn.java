package com.bcabuddies.blogair.signIn;

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
import com.bcabuddies.blogair.model.LoginUser;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bcabuddies.blogair.welcome.Welcome;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignIn extends AppCompatActivity {


    ImageView backBtn;
    private static final String TAG = "SignIn";
    TextInputLayout emailLayout, passLayout;
    TextView forgetPass;
    Button signInBtn;
    List<LoginToken> tokenList;
    List<LoginUser> userList;
    String emailId, password;
    String loginToken = "", uid = "", fullName = "", thumbImage = "";
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        backBtn = findViewById(R.id.signin_backbtn);
        emailLayout = findViewById(R.id.signin_emaillayout);
        passLayout = findViewById(R.id.singin_passlayout);
        forgetPass = findViewById(R.id.signin_forgetpasstv);
        signInBtn = findViewById(R.id.signin_loginbtn);
        tokenList = new ArrayList<>();
        userList = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, Welcome.class));
                SignIn.this.finish();
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailId = emailLayout.getEditText().getText().toString();
                password = passLayout.getEditText().getText().toString();
                if (emailId.equals("") || emailId.equals(null)) {
                    Toast.makeText(SignIn.this, "PLease enter  Email", Toast.LENGTH_SHORT).show();
                } else if (password.equals("") || password.equals(null)) {
                    Toast.makeText(SignIn.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    callApi();
                }
                Log.e(TAG, "onCreate: " + emailId);

            }
        });

    }

    private void callApi() {
        Log.e(TAG, "callApi: " + emailId + "  " + password);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        APIInterface loginApi = retrofit.create(APIInterface.class);
        Call<LoginToken> tokenCallList = loginApi.getToken(emailId, password);

        tokenCallList.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SignIn.this, "Incorrect email or paasword", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onResponse: code: " + response.code());
                }

                fullName = response.body().getName();
                loginToken = response.body().getToken();
                thumbImage = response.body().getThumb_image();
                uid = response.body().getUid();

                //test values
/*                  Log.e(TAG, "onResponse: token:  " + loginToken);
                    Log.e(TAG, "onResponse: thumb image:  " + thumbImage);
                    Log.e(TAG, "onResponse: uid:  " + uid);
                    Log.e(TAG, "onResponse: full name:  " + fullName);*/


                preferenceManager.saveString(Constants.KEY_JWT_TOKEN, loginToken);
                preferenceManager.saveString(Constants.KEY_FUll_NAME, fullName);
                preferenceManager.saveString(Constants.KEY_UID, uid);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(new Intent(SignIn.this, MainActivity.class));

                SignIn.this.finish();


                // LoginToken tokenList = response.body();


            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {
                Log.e(TAG, "onFailure: error:  " + t.getMessage());
                Toast.makeText(SignIn.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
