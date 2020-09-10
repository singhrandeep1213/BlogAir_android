package com.bcabuddies.blogair.home.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.APIInterface;
import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.adapter.homeRecyclerAdapter;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private com.bcabuddies.blogair.adapter.homeRecyclerAdapter homeRecyclerAdapter;
    private static final String TAG = "HomeFeed";
    List<com.bcabuddies.blogair.model.HomeFeed> finalList;
    PreferenceManager preferenceManager;
    String token, uid, fullName;
    int pageCount = 1;
    boolean noMoreResults = false;
    boolean listEnd = false;

    public HomeFeedFragment() {
        //required empty constructure
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: called");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_feed, container, false);


        preferenceManager = new PreferenceManager(getActivity());

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
        recyclerViewInit(view);

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
                            Toast.makeText(getActivity().getApplicationContext(), "No more posts", Toast.LENGTH_SHORT).show();
                            listEnd = true;
                        }
                    }
                }
            }
        });


        return view;
    }

    private void callApi(int pageNo) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        APIInterface jsonHomeFeedApi = retrofit.create(APIInterface.class);

        Call<List<com.bcabuddies.blogair.model.HomeFeed>> listCall = jsonHomeFeedApi.getHomeFeed("bearer " + token, pageNo);

        listCall.enqueue(new Callback<List<com.bcabuddies.blogair.model.HomeFeed>>() {
            @Override
            public void onResponse(Call<List<com.bcabuddies.blogair.model.HomeFeed>> call, Response<List<com.bcabuddies.blogair.model.HomeFeed>> response) {
               /*if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: code " + response.code());
                }
*/
                //List<HomeFeed> homeFeeds = response.body();
               /* if (homeFeeds!=null){
                    finalList.addAll(homeFeeds);
                    homeRecyclerAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MainActivity.this, "no post", Toast.LENGTH_SHORT).show();
                }
*/
                Log.e(TAG, "onResponse: final" + finalList);

                if (response.body() == null) {
                    Log.e(TAG, "onResponse: errr:  " + response.body());
                    //Log.e(TAG, "onResponse: errr: + "+response.body() );
                    noMoreResults = true;
                } else {
                    List<com.bcabuddies.blogair.model.HomeFeed> homeFeeds = response.body();
                    finalList.addAll(homeFeeds);
                    homeRecyclerAdapter.notifyDataSetChanged();
                    Log.e(TAG, "onResponse: final" + finalList);
                }
            }

            @Override
            public void onFailure(Call<List<com.bcabuddies.blogair.model.HomeFeed>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Check Your Internet Connection ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recyclerViewInit(View mView) {
        homeRecyclerAdapter = new homeRecyclerAdapter(getActivity(), finalList);
        recyclerView = mView.findViewById(R.id.homefrag_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(homeRecyclerAdapter);
    }

    public static Fragment newInstance() {
        HomeFeedFragment fragment = new HomeFeedFragment();
        return fragment;
    }

}