package com.bcabuddies.blogair.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.model.BlockedUsers;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockedUsersRecyclerAdapter extends RecyclerView.Adapter<BlockedUsersRecyclerAdapter.blockedUsersViewHolder> {

    Context context;
    List<BlockedUsers.User> blockedUsersList;
    private static final String TAG = "BlockedUsersRecyclerAda";

    public BlockedUsersRecyclerAdapter(Context context, List<BlockedUsers.User> blockedUsersList) {
        this.context = context;
        this.blockedUsersList = blockedUsersList;
    }

    @NonNull
    @Override
    public BlockedUsersRecyclerAdapter.blockedUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        return new blockedUsersViewHolder((inflater.inflate(R.layout.blocked_user_row,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull BlockedUsersRecyclerAdapter.blockedUsersViewHolder holder, int position) {

       try{
           String thumbImageUrl= blockedUsersList.get(position).getThumb_image();
           String uid= blockedUsersList.get(position).getUid();
           String fullName= blockedUsersList.get(position).getFull_name();

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
        return blockedUsersList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class blockedUsersViewHolder extends  RecyclerView.ViewHolder{
        CircleImageView thumb_image_view;
        TextView full_name_tv;
        Button unblock_btn;
        public blockedUsersViewHolder(@NonNull View itemView) {
            super(itemView);

            thumb_image_view=itemView.findViewById(R.id.blockeduser_thumbimage);
            full_name_tv=itemView.findViewById(R.id.blockeduser_fullnane);
            unblock_btn=itemView.findViewById(R.id.blockeduser_unblockbtn);


        }
    }
}
