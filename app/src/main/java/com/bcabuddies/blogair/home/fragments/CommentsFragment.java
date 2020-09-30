package com.bcabuddies.blogair.home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bcabuddies.blogair.R;


public class CommentsFragment extends Fragment {


    public CommentsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_comments, container, false);
        return view;
    }

    public static Fragment newInstance(){
        CommentsFragment fragment=new CommentsFragment();
        return  fragment;
    }

}