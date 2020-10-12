package com.bcabuddies.blogair.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.bcabuddies.blogair.APIInterface;
import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.home.CommentsActivity;
import com.bcabuddies.blogair.home.fragments.PostUserProfile;
import com.bcabuddies.blogair.home.fragments.ProfileFragment;
import com.bcabuddies.blogair.model.Comments;
import com.bcabuddies.blogair.model.HomeFeed;
import com.bcabuddies.blogair.retrofit.RetrofitManager;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bcabuddies.blogair.utils.TimeAgo;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


        preferenceManager = new PreferenceManager(context);
        String token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        String pid = homeFeedList.get(position).getPid();
        String postUid = homeFeedList.get(position).getUid();
        String fullName = homeFeedList.get(position).getFull_name();
        String thumbImage = homeFeedList.get(position).getThumb_image();
        String currentUid = preferenceManager.getString(Constants.KEY_UID);
        String is_bookmarked = homeFeedList.get(position).getIs_bookmarked();
        String postHeading = homeFeedList.get(position).getPost_heading();
        int is_liked_by_current_user = homeFeedList.get(position).getIs_liked_by_current_user();
        int likes_count = homeFeedList.get(position).getLikes_count();
        Date timeStamp = homeFeedList.get(position).getTime_stamp();

        bundle = new Bundle();


        Glide.with(context)
                .load(thumbImage)
                .into(holder.userThumb);
        Glide.with(context)
                .load(homeFeedList.get(position).getPost_image())
                .into(holder.postImage);
        holder.fullName.setText(homeFeedList.get(position).getFull_name());
        holder.postDesc.setText(homeFeedList.get(position).getDesc());
        holder.postHeading.setText(postHeading);

        Log.e(TAG, "onBindViewHolder: pid: "+pid + "  is_bookmarked:  "+is_bookmarked);

        //set time stamp
        long timeInMili = timeStamp.getTime();
        String timeAgo = TimeAgo.getTimeAgo(timeInMili);
        if (timeAgo == "ADD_DATE") {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd MMMM" + ", " + "EEE");
            final StringBuilder nowMMDDYYYY = new StringBuilder(dateformatMMDDYYYY.format(timeStamp));
            holder.timeStamp.setText(nowMMDDYYYY.toString());
        } else {
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

        //set like icon
        if  (is_liked_by_current_user==1){
            holder.likeSelectedIcon.setVisibility(View.VISIBLE);
            holder.likeUnselectedIcon.setVisibility(View.GONE);
        }else if(is_liked_by_current_user==0){
            holder.likeUnselectedIcon.setVisibility(View.VISIBLE);
            holder.likeSelectedIcon.setVisibility(View.GONE);
        }

        //open user profile fragment
        holder.fullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postUid.equals(currentUid)) {
                    Fragment fragment = ProfileFragment.newInstance();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                    ft.addToBackStack(null).replace(R.id.home_fragment, fragment).commit();

                    Log.e(TAG, "onClick: current user: ");
                } else {
                    bundle.putString("post_uid", postUid);
                    bundle.putString("full_name", fullName);
                    bundle.putString("thumb_image", thumbImage);
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

        holder.commentsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              bundle.putString("post_uid", postUid);
                bundle.putString("pid",pid);
                bundle.putString("likes_count", String.valueOf(likes_count));
                bundle.putString("post_heading",postHeading);
                Intent  intent= new Intent(context,CommentsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });

        //like a post
        holder.likeUnselectedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeUnselectedIcon.setEnabled(false);
                String lid= UUID.randomUUID().toString();
                APIInterface postLikeApi= RetrofitManager.getRetrofit().create(APIInterface.class);
                Call<ResponseBody> likeCall =postLikeApi.likePost("bearer " + token,lid,pid);

                likeCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                            holder.likeUnselectedIcon.setEnabled(true);

                        } else {
                            Toast.makeText(context, "liked", Toast.LENGTH_SHORT).show();
                            holder.likeUnselectedIcon.setVisibility(View.GONE);
                            holder.likeSelectedIcon.setVisibility(View.VISIBLE);
                            holder.likeUnselectedIcon.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                        holder.likeUnselectedIcon.setEnabled(true);
                    }
                });

            }
        });

        //unlike a post
        holder.likeSelectedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeSelectedIcon.setEnabled(false);

                APIInterface postUnlikeApi= RetrofitManager.getRetrofit().create(APIInterface.class);
                Call<ResponseBody> likeCall =postUnlikeApi.unlikePost("bearer " + token,pid);

                likeCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                            holder.likeSelectedIcon.setEnabled(true);

                        } else {
                            Toast.makeText(context, "unliked", Toast.LENGTH_SHORT).show();
                            holder.likeSelectedIcon.setVisibility(View.GONE);
                            holder.likeUnselectedIcon.setVisibility(View.VISIBLE);
                            holder.likeSelectedIcon.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                        holder.likeSelectedIcon.setEnabled(true);
                    }
                });
            }
        });


        //bookmark a post
        holder.bookmarkUnselectedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bookmarkUnselectedIcon.setEnabled(false);
                String bid= UUID.randomUUID().toString();
                APIInterface postLikeApi= RetrofitManager.getRetrofit().create(APIInterface.class);
                Call<ResponseBody> likeCall =postLikeApi.bookmarkPost("bearer " + token,bid,pid);

                likeCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                            holder.bookmarkUnselectedIcon.setEnabled(true);
                        } else {
                            Toast.makeText(context, "bookmarked", Toast.LENGTH_SHORT).show();
                            holder.bookmarkUnselectedIcon.setVisibility(View.GONE);
                            holder.bookmarkSelectedIcon.setVisibility(View.VISIBLE);
                            holder.bookmarkUnselectedIcon.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                        holder.bookmarkUnselectedIcon.setEnabled(true);
                    }
                });

            }
        });

        //remove bookmark
        holder.bookmarkSelectedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bookmarkSelectedIcon.setEnabled(false);

                APIInterface postUnlikeApi= RetrofitManager.getRetrofit().create(APIInterface.class);
                Call<ResponseBody> likeCall =postUnlikeApi.unBookmarkPost("bearer " + token,pid);

                likeCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                            holder.bookmarkSelectedIcon.setEnabled(true);

                        } else {
                            Toast.makeText(context, "bookmark removed", Toast.LENGTH_SHORT).show();
                            holder.bookmarkSelectedIcon.setVisibility(View.GONE);
                            holder.bookmarkUnselectedIcon.setVisibility(View.VISIBLE);
                            holder.bookmarkSelectedIcon.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                        holder.bookmarkSelectedIcon.setEnabled(true);
                    }
                });
            }
        });

        //popup menu
        holder.dotsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: dots clicked");
                final PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.setForceShowIcon(true);
                MenuInflater inflater = popupMenu.getMenuInflater();
                if (postUid.equals(currentUid)) {
                    Log.e(TAG, "onClick: dots if");
                    inflater.inflate(R.menu.delete_menu, popupMenu.getMenu());
                } else {
                    Log.e(TAG, "onClick: dots else");
                    inflater.inflate(R.menu.report_menu, popupMenu.getMenu());
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_menu_btn:
                                final AlertDialog alertDialog;
                                alertDialog = new Builder(context)
                                        .setMessage("Delete this post?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.e(TAG, "onClick: pid: " + pid);
                                                callRemovePostApi(token, pid,position);
                                                Toast.makeText(context, "Yes clicked", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(context, "No clicked", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .show();
                                Toast.makeText(context, "delete clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.report_menu_btn:
                                Toast.makeText(context, "Report clicked", Toast.LENGTH_SHORT).show();
                                break;

                        }
                        return false;
                    }
                });

                popupMenu.show();

            }
        });

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: pid: " + pid);
            }
        });


    }

    private void callRemovePostApi(String token, String pid, int position) {
        Log.e(TAG, "callRemovePostApi: pid: " + pid);
        APIInterface jsonHomeFeedApi = RetrofitManager.getRetrofit().create(APIInterface.class);

        Call<ResponseBody> removePost = jsonHomeFeedApi.removePost("bearer " + token, pid);

        removePost.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show();
                    homeFeedList.remove(position);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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

    public class homeFeedViewHolder extends RecyclerView.ViewHolder {

        public ImageView dotsMenu, postImage, likeUnselectedIcon,likeSelectedIcon ,commentsIcon,bookmarkSelectedIcon, bookmarkUnselectedIcon;
        CircleImageView userThumb;
        TextView fullName,  postHeading, postDesc,timeStamp,moreTv,lessTv;

        public homeFeedViewHolder(@NonNull View itemView) {
            super(itemView);

            userThumb = itemView.findViewById(R.id.home_row_thumb_icon);
            dotsMenu = itemView.findViewById(R.id.homerow_dots_menu);
            postImage = itemView.findViewById(R.id.homerow_post_image);
            likeUnselectedIcon = itemView.findViewById(R.id.homerow_like_icon);
            likeSelectedIcon= itemView.findViewById(R.id.homerow_likeselesctedicon);
            commentsIcon = itemView.findViewById(R.id.homerow_comments_icon);
            fullName = itemView.findViewById(R.id.homerow_full_name);

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


