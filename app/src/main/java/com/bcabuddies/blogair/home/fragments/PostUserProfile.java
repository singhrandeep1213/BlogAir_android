package com.bcabuddies.blogair.home.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.APIInterface;
import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.adapter.ProfileRecyclerAdapter;
import com.bcabuddies.blogair.model.UserProfile;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;


public class PostUserProfile extends Fragment {

    Bundle bundle;
    String postUid;
    List<UserProfile.Post> finallist;
    String token;
    String thumbImage, followersCount, followingCount, fullName;
    TextView fullNameTv, followerCountTv, followingCountTv, bio;
    ProfileRecyclerAdapter profileRecyclerAdapter;
    RecyclerView postRecyclerview;
    PreferenceManager preferenceManager;

    private static final String TAG = "PostUserProfile";


    public PostUserProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_post_user_profile, container, false);
    bundle=this.getArguments();
    postUid = bundle.get("post_uid").toString();
    preferenceManager=new PreferenceManager(getActivity());
    token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
    finallist=new ArrayList<>();
    followerCountTv=view.findViewById(R.id.userprof_followercount);
    followingCountTv=view.findViewById(R.id.userprof_followingcount);


    Log.e(TAG, "onCreateView: postUid: "+postUid );

        callAPi();
        recyclerviewInit(view);

    return  view;
    }

    private void recyclerviewInit(View view) {

        profileRecyclerAdapter= new ProfileRecyclerAdapter(getActivity(),finallist);
        postRecyclerview= view.findViewById(R.id.userprof_recyclerview);
        postRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
        postRecyclerview.setAdapter(profileRecyclerAdapter);

    }


    private void callAPi() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        Log.e(TAG, "callAPi: token: "+token );
        APIInterface userProfileApi= retrofit.create(APIInterface.class);
        Call<UserProfile> listCall= userProfileApi.getUserProfile("bearer " + token, postUid);
        listCall.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (!response.isSuccessful()){
                    Log.e(TAG, "onResponse: error: "+response.code() );
                    Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                }
                else{
                    List<UserProfile.Post> posts= response.body().getPost();
                    Log.e(TAG, "onResponse: posts: " +posts);
                    finallist.addAll(posts);
                    profileRecyclerAdapter.notifyDataSetChanged();
                    followersCount = response.body().getFollowers_count();
                    followingCount=response.body().getFollowing_count();
                    followerCountTv.setText(followersCount);
                    followingCountTv.setText(followingCount);


                }

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });


    }

    public static Fragment newInstance(){
        PostUserProfile fragment=new PostUserProfile();
        return  fragment;
    }
}