package com.bcabuddies.blogair.welcome;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.adapter.WelcomeViewPagerAdapter;

import com.bcabuddies.blogair.home.MainActivity;
import com.bcabuddies.blogair.model.WelcomeViewPagerModel;
import com.bcabuddies.blogair.signIn.SignIn;
import com.bcabuddies.blogair.signUp.SignUp;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Welcome extends AppCompatActivity {


    private List<WelcomeViewPagerModel> viewPagerImageList;
    ViewPager welcomeViewPager;
    TabLayout welcomeTabLayout;
    Button btnSignIn, btnSignUp;
    PreferenceManager preferenceManager;
    private static final String TAG = "Welcome";
    String initialToken;
    int page=0;
    private Handler handler;
    private int delay = 1500;
    WelcomeViewPagerAdapter welcomeViewPagerAdapter;

    Runnable runnable =new Runnable() {
        @Override
        public void run() {
            if(welcomeViewPagerAdapter.getCount()==page){
                page=0;
            }else{
                page++;
            }
            welcomeViewPager.setCurrentItem(page,true);
            handler.postDelayed(this,delay);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler = new Handler();
        preferenceManager = new PreferenceManager(this);
        initialToken=preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        Log.e(TAG, "onCreate: initial token:   "+initialToken  );
        if (!(initialToken.equals("") || initialToken.equals(null))){
            startActivity(new Intent(Welcome.this, MainActivity.class));
            this.finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        welcomeViewPager = findViewById(R.id.welcome_viewPager);
        welcomeTabLayout=findViewById(R.id.welcome_tabLayout);
        btnSignIn=findViewById(R.id.welcome_signInBtn);
        btnSignUp=findViewById(R.id.welcome_signUpBtn);

        //sign in button click
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this, SignIn.class));
                Welcome.this.finish();
            }
        });


        //sign up button click
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this, SignUp.class));
                Welcome.this.finish();
            }
        });

        //viewpager
        viewPagerImageList = new ArrayList<>();
        addImageData();
        welcomeViewPagerAdapter = new WelcomeViewPagerAdapter(viewPagerImageList, this);
        welcomeViewPager.setAdapter(welcomeViewPagerAdapter);
        welcomeTabLayout.setupWithViewPager(welcomeViewPager);

        welcomeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page=position;
                Log.e(TAG, "onPageSelected: "+position );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }




    private void addImageData() {
        //right now it is static, change it to dynamic
        viewPagerImageList.add(new WelcomeViewPagerModel(R.drawable.slider_image_1));
        viewPagerImageList.add(new WelcomeViewPagerModel(R.drawable.slider_image_2));
        viewPagerImageList.add(new WelcomeViewPagerModel(R.drawable.slider_image_3));
    }
}
