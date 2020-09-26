package com.bcabuddies.blogair.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.home.fragments.PostUserProfile;
import com.bcabuddies.blogair.home.fragments.ProfileFragment;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bcabuddies.blogair.utils.TimeAgo;
import com.bcabuddies.blogair.model.HomeFeed;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class homeRecyclerAdapter extends RecyclerView.Adapter<homeRecyclerAdapter.homeFeedViewHolder> {

    private Context context;
    private List<HomeFeed.Post> homeFeedList = new ArrayList<>();
    private static final String TAG = "homeRecyclerAdapter";
    Bundle bundle;
    PreferenceManager preferenceManager;


    public homeRecyclerAdapter(Context context, List<HomeFeed.Post> homeFeedList) {
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

        preferenceManager=new PreferenceManager(context);
        String pid= homeFeedList.get(position).getPid();
        String postUid=homeFeedList.get(position).getUid();
        String fullName= homeFeedList.get(position).getFull_name();

        String currentUid=preferenceManager.getString(Constants.KEY_UID);
        String is_bookmarked=homeFeedList.get(position).getIs_bookmarked();
        Date timeStamp = homeFeedList.get(position).getTime_stamp();
        bundle=new Bundle();


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

        Log.e(TAG, "onBindViewHolder: pid: "+pid + "  is_bookmarked:  "+is_bookmarked);

        //set time stamp
        long timeInMili = timeStamp.getTime();
        String timeAgo = TimeAgo.getTimeAgo(timeInMili);
        if (timeAgo == "ADD_DATE"){
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd MMMM" + ", "+"EEE");
            final StringBuilder nowMMDDYYYY = new StringBuilder(dateformatMMDDYYYY.format(timeStamp));
            holder.timeStamp.setText(nowMMDDYYYY.toString());
        }
        else{
            holder.timeStamp.setText(timeAgo);
        }

        //set more and less view
        holder.postDesc.post(new Runnable() {
            @Override
            public void run() {
                int num=holder.postDesc.getLineCount();
                if (num > 4 ){
                    holder.moreTv.setVisibility(View.VISIBLE);
                }

            }
        });
        holder.moreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.moreTv.setVisibility(View.GONE);
                holder.lessTv.setVisibility(View.VISIBLE);
                holder.postDesc.setMaxLines(Integer.MAX_VALUE);
            }
        });
        holder.lessTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.lessTv.setVisibility(View.GONE);
                holder.moreTv.setVisibility(View.VISIBLE);
                holder.postDesc.setMaxLines(4);
            }
        });

        //set bookmark icon
        if (is_bookmarked.equals("1")){
            holder.bookmarkSelectedIcon.setVisibility(View.VISIBLE);
            holder.bookmarkUnselectedIcon.setVisibility(View.GONE);
        }else if (is_bookmarked.equals("0")){
            holder.bookmarkUnselectedIcon.setVisibility(View.VISIBLE);
            holder.bookmarkSelectedIcon.setVisibility(View.GONE);
        }

        //open user profile fragment
        holder.fullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (postUid.equals(currentUid)){
                    Fragment fragment= ProfileFragment.newInstance();
                    AppCompatActivity activity= (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.home_fragment, fragment).commit();

                    Log.e(TAG, "onClick: current user: " );
                }
                else{
                    bundle.putString("post_uid",postUid);
                    bundle.putString("full_name",fullName);
                    AppCompatActivity activity= (AppCompatActivity) v.getContext();
                    Fragment fragment= PostUserProfile.newInstance();
                    fragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.home_fragment, fragment).commit();


                    Log.e(TAG, "onClick: not current user" );
                }

            }
        });



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

        ImageView dotsMenu, postImage, likeIcon, commentsIcon,bookmarkSelectedIcon, bookmarkUnselectedIcon;
        CircleImageView userThumb;
        TextView fullName, likesCount, postHeading, postDesc,timeStamp,moreTv,lessTv;

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
            timeStamp=itemView.findViewById(R.id.homerow_timestamp);
            moreTv=itemView.findViewById(R.id.homerow_moretv);
            lessTv=itemView.findViewById(R.id.homerow_lesstv);
            bookmarkUnselectedIcon=itemView.findViewById(R.id.homerow_bookmarkunselected);
            bookmarkSelectedIcon=itemView.findViewById(R.id.homerow_bookmarkselected);
        }
    }

}


