package com.bcabuddies.blogair.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.home.SinglePost;
import com.bcabuddies.blogair.model.UserProfile;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.profileViewHolder> {

    private Context context;
    private List<UserProfile.Post> userPostList;
    private static final String TAG = "ProfileRecyclerAdapter";
    String userFullName, userThumbImage;
    String pid;
    Bundle bundle;

 /*   public ProfileRecyclerAdapter(Context context, List<UserProfile.Post> userPostList) {
        this.context = context;
        this.userPostList = userPostList;
    }
*/
    public ProfileRecyclerAdapter(Context context, List<UserProfile.Post> userPostList, String userFullName, String userThumbImage) {
        this.context = context;
        this.userPostList = userPostList;
        this.userFullName = userFullName;
        this.userThumbImage = userThumbImage;
    }

    @NonNull
    @Override
    public profileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new profileViewHolder((inflater.inflate(R.layout.profile_post_row,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull profileViewHolder holder, int position) {
        String postImageUrl= userPostList.get(position).getPost_image();
        Glide.with(context).load(postImageUrl).into(holder.profilePostImage);

        holder.profilePostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pid= userPostList.get(position).getPid();
                bundle=new Bundle();
                Log.e(TAG, "profilerecycler: userthumb: "+userThumbImage + "\n user fullname: "+userFullName );
                Log.e(TAG, "profilerecycler: "+ userPostList.get(position).getPost_image() + "  Pid: "+ userPostList.get(position).getPid() );
                bundle.putString("thumb_image",userThumbImage);
                bundle.putString("full_name", userFullName);
                bundle.putString("post_image_url",postImageUrl);
                bundle.putString("pid",pid);
                Intent intent = new Intent(context, SinglePost.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userPostList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class profileViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePostImage;
        public profileViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePostImage=itemView.findViewById(R.id.profile_post_image);

        }
    }
}
