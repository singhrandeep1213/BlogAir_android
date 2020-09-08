package com.bcabuddies.blogair.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.APIInterface;
import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.adapter.homeRecyclerAdapter;
import com.bcabuddies.blogair.model.HomeFeed;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private homeRecyclerAdapter homeRecyclerAdapter;
    private static final String TAG = "MainActivity";
    List<HomeFeed> finalList;
    PreferenceManager preferenceManager;
    String token, uid, fullName;
    int pageCount = 1;
    boolean noMoreResults = false;
    boolean listEnd = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceManager = new PreferenceManager(this);

        token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        uid = preferenceManager.getString(Constants.KEY_UID);
        fullName = preferenceManager.getString(Constants.KEY_FUll_NAME);

        //test for values
        Log.e(TAG, "onCreate: full name:  " + fullName);
        Log.e(TAG, "onCreate: uid: " + uid);
        Log.e(TAG, "onCreate: token: " + token);

        finalList = new ArrayList<>();
        callApi(pageCount);
        // Log.e(TAG, "onResponse: finalList" + finalList);
        recyclerViewInit();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    //Toast.makeText(MainActivity.this, "end of the list", Toast.LENGTH_SHORT).show();
                    pageCount = pageCount + 1;
                    if (!noMoreResults) {
                        callApi(pageCount);
                    } else {
                        if (!listEnd) {
                            Toast.makeText(MainActivity.this, "No more posts", Toast.LENGTH_SHORT).show();
                            listEnd = true;
                        }
                    }
                }
            }
        });
    }

    private void callApi(int pageNo) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        APIInterface jsonHomeFeedApi = retrofit.create(APIInterface.class);

        Call<List<HomeFeed>> listCall = jsonHomeFeedApi.getHomeFeed("bearer " + token, pageNo);

        listCall.enqueue(new Callback<List<HomeFeed>>() {
            @Override
            public void onResponse(Call<List<HomeFeed>> call, Response<List<HomeFeed>> response) {
              /*  if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: code " + response.code());
                }*/

                if (response.body() == null) {
                    Log.e(TAG, "onResponse: errr:  " + response.body());
                    //Log.e(TAG, "onResponse: errr: + "+response.body() );
                    noMoreResults = true;
                } else {
                    List<HomeFeed> homeFeeds = response.body();
                    finalList.addAll(homeFeeds);
                    homeRecyclerAdapter.notifyDataSetChanged();
                    Log.e(TAG, "onResponse: final" + finalList);
                }
            }

            @Override
            public void onFailure(Call<List<HomeFeed>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Check Your Internet Connection ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recyclerViewInit() {
        homeRecyclerAdapter = new homeRecyclerAdapter(this, finalList);
        recyclerView = findViewById(R.id.home_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(homeRecyclerAdapter);
    }
}
