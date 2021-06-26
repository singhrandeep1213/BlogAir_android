package com.bcabuddies.blogair.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.home.fragments.PostUserProfile;
import com.bcabuddies.blogair.home.fragments.ProfileFragment;
import com.bcabuddies.blogair.model.Comments;
import com.bcabuddies.blogair.retrofit.APIInterface;
import com.bcabuddies.blogair.retrofit.RetrofitManager;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        preferenceManager = new PreferenceManager(context);
        String token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        String cid = commentsList.get(position).getCid();
        String thumbImageUrl=commentsList.get(position).getThumb_image();
        String fullName=commentsList.get(position).getFull_name();
        String  commentDescription=commentsList.get(position).getComment_description();
        String  commentUid=commentsList.get(position).getUid();

        try {
            Glide.with(context).load(thumbImageUrl).into(holder.thumb_image_view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.full_name_tv.setText(fullName);
        holder.comment_desc.setText(commentDescription);

        //open user profile
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

        //set dots menu
        holder.dots_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: dots clicked");
                final PopupMenu popupMenu = new PopupMenu(context, v);
               // popupMenu.setForceShowIcon(true);
                MenuInflater inflater = popupMenu.getMenuInflater();
                if (commentUid.equals(currentUid)) {
                    Log.e(TAG, "onClick: dots if");
                    inflater.inflate(R.menu.delete_menu, popupMenu.getMenu());
                } else {
                    Log.e(TAG, "onClick: dots else");
                    inflater.inflate(R.menu.report_menu, popupMenu.getMenu());
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete_menu_btn:
                                final AlertDialog alertDialog;
                                alertDialog = new AlertDialog.Builder(context)
                                        .setMessage("Delete this comment?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.e(TAG, "onClick: cid: " + cid);
                                                callRemoveCommentApi(token, cid,position);
                                               // Toast.makeText(context, "Yes clicked", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(context, "No clicked", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .show();
                        }

                        return false;
                    }
                });

                popupMenu.show();
            }
        });

    }

    private void callRemoveCommentApi(String token, String cid, int position) {
        Log.e(TAG, "callRemovePostApi: cid: " + cid);
        APIInterface jsonHomeFeedApi = RetrofitManager.getRetrofit().create(APIInterface.class);
        Call<ResponseBody> removeComment = jsonHomeFeedApi.removeComment("bearer " + token, cid);

        removeComment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Comment Deleted Successfully", Toast.LENGTH_SHORT).show();
                    commentsList.remove(position);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
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
