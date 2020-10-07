package com.bcabuddies.blogair.adapter;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.home.fragments.PostUserProfile;
import com.bcabuddies.blogair.home.fragments.ProfileFragment;
import com.bcabuddies.blogair.model.Comments;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.commentsViewHolder> {

    Context context;
    List<Comments.comments> commentsList;
    String postUid;
    Bundle bundle;
    PreferenceManager preferenceManager;
    private static final String TAG = "CommentsRecyclerAdapter";

    public CommentsRecyclerAdapter(Context context, List<Comments.comments> commentsList, String postUid) {
        this.context = context;
        this.commentsList = commentsList;
        this.postUid = postUid;
    }

    @NonNull
    @Override
    public CommentsRecyclerAdapter.commentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        return new CommentsRecyclerAdapter.commentsViewHolder((inflater.inflate(R.layout.comments_row,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsRecyclerAdapter.commentsViewHolder holder, int position) {

        preferenceManager=new PreferenceManager(context);
        String currentUid=preferenceManager.getString(Constants.KEY_UID);
        bundle=new Bundle();

        String thumbImageUrl, fullName, commentDescription, commentUid;
        thumbImageUrl=commentsList.get(position).getThumb_image();
        fullName=commentsList.get(position).getFull_name();
        commentDescription=commentsList.get(position).getComment_description();
        commentUid=commentsList.get(position).getUid();

        try {
            Glide.with(context).load(thumbImageUrl).into(holder.thumb_image_view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.full_name_tv.setText(fullName);
        holder.comment_desc.setText(commentDescription);

        holder.full_name_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (commentUid.equals(currentUid)){
                    Fragment fragment= ProfileFragment.newInstance();
                    AppCompatActivity activity= (AppCompatActivity) v.getContext();
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right);
                    ft.addToBackStack(null).replace(R.id.home_fragment, fragment).commit();

                    Log.e(TAG, "onClick: current user: " );
                }
                else{
                    bundle.putString("post_uid",commentUid);
                    bundle.putString("full_name",fullName);
                    bundle.putString("thumb_image",thumbImageUrl);
                    AppCompatActivity activity= (AppCompatActivity) v.getContext();
                    Fragment fragment= PostUserProfile.newInstance();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right);
                    ft.addToBackStack(null).replace(R.id.home_fragment, fragment).commit();

                    Log.e(TAG, "onClick: not current user" );
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class commentsViewHolder extends RecyclerView.ViewHolder{
        CircleImageView thumb_image_view;
        TextView full_name_tv , comment_desc;
        ImageView dots_menu;


        public commentsViewHolder(@NonNull View itemView) {
            super(itemView);

            thumb_image_view=itemView.findViewById(R.id.commentsrow_thumbimage);
            full_name_tv=itemView.findViewById(R.id.commentsrow_fullname);
            comment_desc=itemView.findViewById(R.id.commentsrow_desc);
            dots_menu=itemView.findViewById(R.id.commentsrow_dotsmenu);

        }
    }
}
