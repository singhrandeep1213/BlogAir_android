package com.bcabuddies.blogair.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bcabuddies.blogair.APIInterface;
import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.adapter.BlockedUsersRecyclerAdapter;
import com.bcabuddies.blogair.model.BlockedUsers;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsBlockedUsers extends AppCompatActivity {

    private static final String TAG = "SettingsBlockedUsers";

    ImageView backIcon;
    Button closeBtn;
    RecyclerView recyclerView;
    BlockedUsersRecyclerAdapter blockedUsersRecyclerAdapter;
    List<BlockedUsers.User> finalList;
    PreferenceManager preferenceManager;
    String token;

    boolean noMoreResults = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_blocked_users);

        preferenceManager = new PreferenceManager(this);
        backIcon = findViewById(R.id.settingsbu_backicon);
        closeBtn = findViewById(R.id.settingsbu_closebtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsBlockedUsers.this.finish();
            }
        });
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsBlockedUsers.this.finish();
            }
        });

        token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        finalList = new ArrayList<>();
        callApi();
        recyclerViewInit();


    }

    private void callApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        APIInterface blockedUsersApi = retrofit.create(APIInterface.class);
        Call<BlockedUsers> blockedUsersCall = blockedUsersApi.getBlockedUsers("bearer " + token);

        blockedUsersCall.enqueue(new Callback<BlockedUsers>() {
            @Override
            public void onResponse(Call<BlockedUsers> call, Response<BlockedUsers> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: error:  " + response.code());
                    Toast.makeText(SettingsBlockedUsers.this, "Some error occured", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getBlocked_users()!=null){
                        List<BlockedUsers.User> users = response.body().getBlocked_users();
                        finalList.addAll(users);
                        blockedUsersRecyclerAdapter.notifyDataSetChanged();
                        Log.e(TAG, "onResponse: blockedusers: " + finalList);
                    }
                    else{
                        noMoreResults=true;
                    }

                }
            }

            @Override
            public void onFailure(Call<BlockedUsers> call, Throwable t) {

            }
        });

    }

    private void recyclerViewInit() {
        blockedUsersRecyclerAdapter = new BlockedUsersRecyclerAdapter(this, finalList);
        recyclerView = findViewById(R.id.settingsbu_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(blockedUsersRecyclerAdapter);
    }
}