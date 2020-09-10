package com.bcabuddies.blogair.home.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcabuddies.blogair.R;


public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

        // Inflate the layout for this fragment
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view =inflater.inflate(R.layout.fragment_search, container, false);
        return view;
        }


    public static Fragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

}