package com.bcabuddies.blogair.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.model.BlockedUsers;
import com.bcabuddies.blogair.model.SearchUser;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.searchViewHlder> {

    Context context;
    List<SearchUser.User> searchUsersList;
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

        try{
            String thumbImageUrl= searchUsersList.get(position).getThumb_image();
            String uid= searchUsersList.get(position).getUid();
            String fullName= searchUsersList.get(position).getFull_name();

            Log.e(TAG, "onBindViewHolder: searchname: "+ searchUsersList.get(0).getFull_name() );

            try{
                Glide.with(context).load(thumbImageUrl).into(holder.thumb_image_view);
            }catch (Exception e){
                Log.e(TAG, "onBindViewHolder: failedto load image:  "+e.getMessage() );
            }

            holder.full_name_tv.setText(fullName);

        }catch (Exception e){
            Log.e(TAG, "onBindViewHolder: exception in try:  " );
        }

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
        public searchViewHlder( View itemView) {
            super(itemView);
            thumb_image_view=itemView.findViewById(R.id.searchuser_thumbimage);
            full_name_tv=itemView.findViewById(R.id.searchuser_fullname);
        }
    }
}
