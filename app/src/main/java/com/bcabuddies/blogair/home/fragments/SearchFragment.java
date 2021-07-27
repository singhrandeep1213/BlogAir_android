package com.bcabuddies.blogair.home.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.adapter.SearchRecyclerAdapter;
import com.bcabuddies.blogair.model.SearchUser;
import com.bcabuddies.blogair.retrofit.APIInterface;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchFragment extends Fragment {


    private RecyclerView searchRecyclerView;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    TextInputEditText searchText;
    PreferenceManager preferenceManager;
    String token;
    private static final String TAG = "SearchFragment";

    private ArrayList<SearchUser.User> searchList;


    public SearchFragment() {
        // Required empty public constructor
    }


    // Inflate the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        preferenceManager = new PreferenceManager(getActivity());
        token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        searchList = new ArrayList<>();
        searchText = view.findViewById(R.id.search_textet);



        recyclerViewInit(view);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               //searchList.clear();
                if (s.toString() == "" || s.toString().equals("") ||s.toString() ==null || s.length() ==0 ){
                    Log.e(TAG, "onTextChanged: here: 5: "+ s.toString() );
                    searchList.clear();
                    searchRecyclerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count > 0) {
                    //    Log.e(TAG, "onTextChanged: here: 2" );
                    String name = s.toString();
                    //searchList.clear();
                    callSearchApi(name, token);


               }
            else if (count==0){
                    Log.e(TAG, "onTextChanged: here: 3: "+s.toString() );
                    searchList.clear();
                    searchRecyclerAdapter.notifyDataSetChanged();

               }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() == "" || s.toString().equals("") ||s.toString() ==null || s.length() ==0 ){
                    Log.e(TAG, "onTextChanged: here: 4: "+ s.toString() );
                    searchList.clear();
                    searchRecyclerAdapter.notifyDataSetChanged();
                }
              //  Log.e(TAG, "onTextChanged: here: 4: "+ s.toString() );
            }
        });

        return view;
    }

    private void recyclerViewInit(View view) {
        searchRecyclerAdapter = new SearchRecyclerAdapter(getActivity(), searchList);
        searchRecyclerView = view.findViewById(R.id.search_recyclerview);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchRecyclerView.setAdapter(searchRecyclerAdapter);

    }

    private void callSearchApi(String name, String token) {


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50,TimeUnit.SECONDS).build();

       Retrofit retrofit= new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build();

        APIInterface jsonHomeFeedApi = retrofit.create(APIInterface.class);
        //APIInterface jsonHomeFeedApi = RetrofitManager.getRetrofit().create(APIInterface.class);

        Call<SearchUser> listCall = jsonHomeFeedApi.searchUser("bearer " + token, name);
        listCall.enqueue(new Callback<SearchUser>() {
            @Override
            public void onResponse(Call<SearchUser> call, Response<SearchUser> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: error:  " + response.code());
                    Toast.makeText(getActivity(), "Some error occured1", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "onResponse: searchname: " + response.body().getUsers().get(0).getFull_name());
                    List<SearchUser.User> users = response.body().getUsers();
                    searchList.clear();
                    searchList.addAll(users);
                    searchRecyclerAdapter.notifyDataSetChanged();
                    Log.e(TAG, "onResponse: searchusers: " + searchList);

                }

            }

            @Override
            public void onFailure(Call<SearchUser> call, Throwable t) {
                searchList.clear();
                searchRecyclerAdapter.notifyDataSetChanged();
                Log.e(TAG, "onFailure: searchuser: " + t.toString());
                Toast.makeText(getActivity(), "Some error occured2", Toast.LENGTH_SHORT).show();
            }
        });


    }


    public static Fragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

}