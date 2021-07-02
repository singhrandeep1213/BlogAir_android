package com.bcabuddies.blogair.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.model.FollowRequestUsers;
import com.bcabuddies.blogair.retrofit.APIInterface;
import com.bcabuddies.blogair.retrofit.RetrofitManager;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowRequestRecyclerAdapter extends RecyclerView.Adapter<FollowRequestRecyclerAdapter.followRequestViewHolder> {

    List<FollowRequestUsers.User> followRequestList;
    Context context;

    PreferenceManager preferenceManager;

    private static final String TAG = "FollowRequestRecyclerAd";


    public FollowRequestRecyclerAdapter(List<FollowRequestUsers.User> followRequestList, Context context) {
        this.followRequestList = followRequestList;
        this.context = context;
    }

    @Override
    public followRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new FollowRequestRecyclerAdapter.followRequestViewHolder((inflater.inflate(R.layout.follow_request_row, parent, false)));
    }

    @Override
    public void onBindViewHolder(FollowRequestRecyclerAdapter.followRequestViewHolder holder, int position) {

        preferenceManager = new PreferenceManager(context);
        String token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        String thumbImageUrl = followRequestList.get(position).getThumb_image();
        String uid = followRequestList.get(position).getUid();
        String fullName = followRequestList.get(position).getFull_name();

        try {
            Glide.with(context).load(thumbImageUrl).into(holder.thumb_image_view);
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: failedto load image:  " + e.getMessage());
        }

        holder.full_name_tv.setText(fullName);

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acceptResponse = "Accept";
                String fid = UUID.randomUUID().toString();
                callFollowResponseApi(token, acceptResponse, uid, fid, position);
            }
        });
        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rejectResponse = "Reject";
                String fid = UUID.randomUUID().toString();
                callFollowResponseApi(token, rejectResponse, uid, fid, position);
            }
        });


    }

    private void callFollowResponseApi(String token, String responseType, String uid, String fid , int position) {
        APIInterface followResponse = RetrofitManager.getRetrofit().create(APIInterface.class);
        Call<ResponseBody> listCall = followResponse.followResponse("bearer " + token, responseType, uid, fid);
        listCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();

                } else {
                    followRequestList.remove(position);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();

            }
        });


    }

    /*private void callFollowResponseApi(String token, String responseType) {
        APIInterface blockedUsersApi = RetrofitManager.getRetrofit().create(APIInterface.class);
        Call<FollowRequestUsers> listCall = blockedUsersApi.followResponse("bearer " + token, responseType);
    }*/

    @Override
    public int getItemCount() {
        return followRequestList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    class followRequestViewHolder extends RecyclerView.ViewHolder {
        CircleImageView thumb_image_view;
        TextView full_name_tv;
        Button acceptBtn, rejectBtn;
        //Button unblock_btn;

        public followRequestViewHolder(View itemView) {
            super(itemView);
            thumb_image_view = itemView.findViewById(R.id.followrequest_thumbimage);
            full_name_tv = itemView.findViewById(R.id.followrequest_fullnane);
            acceptBtn = itemView.findViewById(R.id.followrequest_acceptbtn);
            rejectBtn = itemView.findViewById(R.id.followrequest_rejectbtn);
        }
    }
}
