package com.bcabuddies.blogair.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.home.fragments.PostUserProfile;
import com.bcabuddies.blogair.home.fragments.ProfileFragment;
import com.bcabuddies.blogair.model.BlockedUsers;
import com.bcabuddies.blogair.model.SearchUser;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.searchViewHlder> {

    Context context;
    List<SearchUser.User> searchUsersList;
    Bundle bundle;
    PreferenceManager preferenceManager;
    private static final String TAG = "SearchRecyclerAdapter";


    public SearchRecyclerAdapter(Context context, List<SearchUser.User> searchUsersList) {
        this.context = context;
        this.searchUsersList = searchUsersList;
    }



    @Override
    public searchViewHlder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        return new SearchRecyclerAdapter.searchViewHlder((inflater.inflate(R.layout.search_user_row,parent,false)));
    }

    @Override
    public void onBindViewHolder( SearchRecyclerAdapter.searchViewHlder holder, int position) {

        preferenceManager = new PreferenceManager(context);
        bundle = new Bundle();
        String token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        String searchUid = searchUsersList.get(position).getUid();
        String currentUid = preferenceManager.getString(Constants.KEY_UID);
        String thumbImageUrl= searchUsersList.get(position).getThumb_image();
        String fullName= searchUsersList.get(position).getFull_name();

        Log.e(TAG, "onBindViewHolder: searchname: "+ searchUsersList.get(0).getFull_name() );

            try{
                Glide.with(context).load(thumbImageUrl).into(holder.thumb_image_view);
            }catch (Exception e){
                Log.e(TAG, "onBindViewHolder: failedto load image:  "+e.getMessage() );
            }

            holder.full_name_tv.setText(fullName);



        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchUid.equals(currentUid)) {
                    Fragment fragment = ProfileFragment.newInstance();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                    ft.addToBackStack(null).replace(R.id.home_fragment, fragment).commit();

                    Log.e(TAG, "onClick: current user: ");
                } else {
                    bundle.putString("post_uid", searchUid);
                    bundle.putString("full_name", fullName);
                    bundle.putString("thumb_image", thumbImageUrl);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment fragment = PostUserProfile.newInstance();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                    ft.addToBackStack(null).replace(R.id.home_fragment, fragment).commit();

                    Log.e(TAG, "onClick: not current user" );
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchUsersList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class searchViewHlder extends  RecyclerView.ViewHolder {
        CircleImageView thumb_image_view;
        TextView full_name_tv;
        ConstraintLayout rowLayout;
        public searchViewHlder( View itemView) {
            super(itemView);
            thumb_image_view=itemView.findViewById(R.id.searchuser_thumbimage);
            full_name_tv=itemView.findViewById(R.id.searchuser_fullname);
            rowLayout=itemView.findViewById(R.id.searchuser_layout);
        }
    }
}
