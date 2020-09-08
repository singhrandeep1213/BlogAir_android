package com.bcabuddies.blogair.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.model.HomeFeed;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class homeRecyclerAdapter extends RecyclerView.Adapter<homeRecyclerAdapter.homeFeedViewHolder> {

    private Context context;
    private List<HomeFeed> homeFeedList = new ArrayList<>();
    private static final String TAG = "homeRecyclerAdapter";

    public homeRecyclerAdapter(Context context, List<HomeFeed> homeFeedList) {
        Log.e(TAG, "homeRecyclerAdapter: homefeedList" + homeFeedList);
        this.context = context;
        this.homeFeedList = homeFeedList;
    }

    @NonNull
    @Override
    public homeFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new homeFeedViewHolder(inflater.inflate(R.layout.home_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull homeFeedViewHolder holder, int position) {

        Glide.with(context)
                .load(homeFeedList.get(position).getThumb_image())
                .into(holder.userThumb);
        Glide.with(context)
                .load(homeFeedList.get(position).getPost_image())
                .into(holder.postImage);
        holder.fullName.setText(homeFeedList.get(position).getFull_name());
        holder.likesCount.setText(String.valueOf(homeFeedList.get(position).getLikes_count()));
        holder.postDesc.setText(homeFeedList.get(position).getDesc());
        holder.postHeading.setText(homeFeedList.get(position).getPost_heading());

    }

    @Override
    public int getItemCount() {
        return homeFeedList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class homeFeedViewHolder extends RecyclerView.ViewHolder {

        ImageView dotsMenu, postImage, likeIcon, commentsIcon;
        CircleImageView userThumb;
        TextView fullName, likesCount, postHeading, postDesc;

        public homeFeedViewHolder(@NonNull View itemView) {
            super(itemView);

            userThumb = itemView.findViewById(R.id.home_row_thumb_icon);
            dotsMenu = itemView.findViewById(R.id.homerow_dots_menu);
            postImage = itemView.findViewById(R.id.homerow_post_image);
            likeIcon = itemView.findViewById(R.id.homerow_like_icon);
            commentsIcon = itemView.findViewById(R.id.homerow_comments_icon);
            fullName = itemView.findViewById(R.id.homerow_full_name);
            likesCount = itemView.findViewById(R.id.homerow_like_count);
            postHeading = itemView.findViewById(R.id.homerow_postheading);
            postDesc = itemView.findViewById(R.id.homerow_post_desc);
        }
    }

}


