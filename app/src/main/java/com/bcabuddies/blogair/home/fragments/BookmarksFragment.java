package com.bcabuddies.blogair.home.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.adapter.BookmarksRecyclerAdapter;
import com.bcabuddies.blogair.model.Bookmarks;
import com.bcabuddies.blogair.retrofit.APIInterface;
import com.bcabuddies.blogair.retrofit.RetrofitManager;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookmarksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarksFragment extends Fragment {

    private static final String TAG = "BookmarksFragment";
    RecyclerView recyclerView;
    List<Bookmarks.bookmarks> finalList;
    BookmarksRecyclerAdapter bookmarksRecyclerAdapter;
    PreferenceManager preferenceManager;
    String token;

    public BookmarksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        ;

        preferenceManager = new PreferenceManager(getActivity());
        token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        finalList = new ArrayList<>();
        callApi();
        recyclerViewInit(view);

        return view;

    }

    private void callApi() {
        APIInterface bookmarkedPostsApi = RetrofitManager.getRetrofit().create(APIInterface.class);

        Call<Bookmarks> bookmarksCall = bookmarkedPostsApi.getBookmarkedPosts("bearer " + token);

        bookmarksCall.enqueue(new Callback<Bookmarks>() {
            @Override
            public void onResponse(Call<Bookmarks> call, Response<Bookmarks> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: error:  " + response.code());
                    Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getBookmarked_posts() != null) {
                        List<Bookmarks.bookmarks> posts = response.body().getBookmarked_posts();
                        finalList.addAll(posts);
                        bookmarksRecyclerAdapter.notifyDataSetChanged();
                        Log.e(TAG, "onResponse: blockedusers: " + finalList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Bookmarks> call, Throwable t) {

            }
        });

    }

    private void recyclerViewInit(View view) {
        bookmarksRecyclerAdapter = new BookmarksRecyclerAdapter(getActivity(), finalList);
        recyclerView = view.findViewById(R.id.bookmark_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bookmarksRecyclerAdapter);

    }

    public static Fragment newInstance() {
        BookmarksFragment fragment = new BookmarksFragment();
        return fragment;
    }
}