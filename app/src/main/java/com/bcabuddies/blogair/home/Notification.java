package com.bcabuddies.blogair.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.adapter.BlockedUsersRecyclerAdapter;
import com.bcabuddies.blogair.adapter.FollowRequestRecyclerAdapter;
import com.bcabuddies.blogair.model.BlockedUsers;
import com.bcabuddies.blogair.model.FollowRequestUsers;
import com.bcabuddies.blogair.retrofit.APIInterface;
import com.bcabuddies.blogair.retrofit.RetrofitManager;
import com.bcabuddies.blogair.settings.SettingsBlockedUsers;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification extends AppCompatActivity {

    ImageView backIcon;
    RecyclerView recyclerView;

    FollowRequestRecyclerAdapter followRequestRecyclerAdapter;
    List<FollowRequestUsers.User> finalList;
    PreferenceManager preferenceManager;
    String token;
    private static final String TAG = "Notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        preferenceManager = new PreferenceManager(this);
        token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        finalList = new ArrayList<>();
        callLoadApi(token);
        recyclerViewInit();

    }

    private void recyclerViewInit() {
        followRequestRecyclerAdapter = new FollowRequestRecyclerAdapter( finalList, this);
        recyclerView = findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(followRequestRecyclerAdapter);

    }

    private void callLoadApi(String token) {

        APIInterface blockedUsersApi = RetrofitManager.getRetrofit().create(APIInterface.class);
        Call<FollowRequestUsers> listCall = blockedUsersApi.followRequests("bearer " + token);
        listCall.enqueue(new Callback<FollowRequestUsers>() {
            @Override
            public void onResponse(Call<FollowRequestUsers> call, Response<FollowRequestUsers> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: error:  " + response.code());
                    Toast.makeText(Notification.this, "Some error occured", Toast.LENGTH_SHORT).show();
                }
                else {
                        finalList.clear();
                        List<FollowRequestUsers.User> users = response.body().getUser();
                        finalList.addAll(users);
                        followRequestRecyclerAdapter.notifyDataSetChanged();
                        Log.e(TAG, "onResponse: followrequests: " + finalList);
                }
            }

            @Override
            public void onFailure(Call<FollowRequestUsers> call, Throwable t) {

            }
        });

    }
}