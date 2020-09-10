package com.bcabuddies.blogair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.model.UserPosts;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.profileViewHolder> {

    private Context context;
    private List<UserPosts> userPostList;

    public ProfileRecyclerAdapter(Context context, List<UserPosts> userPostList) {
        this.context = context;
        this.userPostList = userPostList;
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
