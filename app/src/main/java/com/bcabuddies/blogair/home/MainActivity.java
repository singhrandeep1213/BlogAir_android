package com.bcabuddies.blogair.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.home.fragments.BookmarksFragment;
import com.bcabuddies.blogair.home.fragments.HomeFeedFragment;
import com.bcabuddies.blogair.home.fragments.ProfileFragment;
import com.bcabuddies.blogair.home.fragments.SearchFragment;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment = null;
    private static final String TAG = "MainActivity";
    TextView topLayoutTv;
    PreferenceManager preferenceManager;
    String thumb_image;
    ImageView homeIcon, searchIcon, addIcon, bookmarkIcon;
    CircleImageView profileIcon, profileCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(this);
        thumb_image = preferenceManager.getString(Constants.KEY_THUMB_IMAGE);
        Log.e(TAG, "onCreate: thumb_image: " + thumb_image);
        fragment = HomeFeedFragment.newInstance();
        topLayoutTv = findViewById(R.id.home_toplayouttv);
        homeIcon = findViewById(R.id.home_homeicon);
        searchIcon = findViewById(R.id.home_searchicon);
        addIcon = findViewById(R.id.home_addicon);
        bookmarkIcon = findViewById(R.id.home_bookmarkicon);
        profileIcon = findViewById(R.id.home_accounticon);
        profileCircle = findViewById(R.id.home_circleoutline);

        profileCircle.setVisibility(View.GONE);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_fragment, fragment)
                .commit();
        homeIcon.setColorFilter(Color.BLACK);

        Glide.with(this).load(thumb_image).into(profileIcon);

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddPost.class));
            }
        });

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topLayoutTv.setText("Home Feed");
                profileCircle.setVisibility(View.GONE);
                homeIcon.setColorFilter(Color.BLACK);
                searchIcon.clearColorFilter();
                //profileIcon.clearColorFilter();
                bookmarkIcon.clearColorFilter();

                fragment = HomeFeedFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment, fragment)
                        .commit();
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topLayoutTv.setText("Explore");
                profileCircle.setVisibility(View.GONE);
                searchIcon.setColorFilter(Color.BLACK);
                homeIcon.clearColorFilter();
                bookmarkIcon.clearColorFilter();

                fragment = SearchFragment.newInstance();
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.home_fragment, fragment)
                        .commit();
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topLayoutTv.setText("Account");
                profileCircle.setVisibility(View.VISIBLE);
                searchIcon.clearColorFilter();
                homeIcon.clearColorFilter();
                bookmarkIcon.clearColorFilter();

                fragment = ProfileFragment.newInstance();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.home_fragment, fragment).commit();
            }
        });

        bookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topLayoutTv.setText("Bookmarks");
                profileCircle.setVisibility(View.GONE);
                bookmarkIcon.setColorFilter(Color.BLACK);
                searchIcon.clearColorFilter();
                homeIcon.clearColorFilter();

                fragment = BookmarksFragment.newInstance();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.home_fragment, fragment).commit();

            }
        });

    }
}