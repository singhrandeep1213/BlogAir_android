package com.bcabuddies.blogair.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.model.SinglePostData;
import com.bcabuddies.blogair.retrofit.APIInterface;
import com.bcabuddies.blogair.retrofit.RetrofitManager;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bcabuddies.blogair.utils.TimeAgo;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import de.hdodenhof.circleimageview.CircleImageView;

public class SinglePost extends AppCompatActivity {

    CircleImageView userThumbImageView;
    TextView postHeadingTv, fullNameTv, timeStampTv, postDescTv;
    TextView moreTv,lessTv;
    ImageView postImageView, commentsImageView, likeSelectedImageView, likeUnSelectedImageView, dotsMenuImageView, bookmarkSelectedImageView, bookmarkUnSelectedImageView;
    String fullName,thumbImage, postImageUrl, pid;
    String token;
    String postUid;
    Date timeStamp;
    String postHeading="", postDescription="";
    int likesCount=0;
    Bundle bundle;
    PreferenceManager preferenceManager;
    private static final String TAG = "SinglePost";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        bundle = this.getIntent().getExtras();
        fullName=bundle.getString("full_name");
        thumbImage= bundle.getString("thumb_image");
        postImageUrl=bundle.getString("post_image_url");
        pid= bundle.getString("pid");

        preferenceManager=new PreferenceManager(this);
        token=preferenceManager.getString(Constants.KEY_JWT_TOKEN);

        userThumbImageView = findViewById(R.id.singlepost_thumbimage);
        postHeadingTv = findViewById(R.id.singlepost_postheading);
        fullNameTv = findViewById(R.id.singlepost_fullname);
        timeStampTv = findViewById(R.id.singlepost_timestamp);
        postDescTv=findViewById(R.id.singlepost_postdesc);
        moreTv=findViewById(R.id.singlepost_moretv);
        lessTv=findViewById(R.id.singlepost_lesstv);
        postImageView = findViewById(R.id.singlepost_postimage);
        commentsImageView = findViewById(R.id.singlepost_comments_icon);
        likeSelectedImageView = findViewById(R.id.singlepost_likeselesctedicon);
        likeUnSelectedImageView = findViewById(R.id.singlepost_likeunselesctedicon);
        dotsMenuImageView = findViewById(R.id.singlepost_dotsmenu);
        bookmarkSelectedImageView=findViewById(R.id.singlepost_bookmarkselected);
        bookmarkUnSelectedImageView=findViewById(R.id.singlepost_bookmarkunselected);



        fullNameTv.setText(fullName);
        Glide.with(this).load(thumbImage).into(userThumbImageView);
        Glide.with(this).load(postImageUrl).into(postImageView);

        callLoadApi();


        likeSelectedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeSelectedImageView.setEnabled(false);
                callUnlikeApi();
            }
        });

        likeUnSelectedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeUnSelectedImageView.setEnabled(false);
                 callLikeApi();

            }
        });

        commentsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bookmarkSelectedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkSelectedImageView.setEnabled(false);
                callRemoveBookmarkApi();
            }
        });

        bookmarkUnSelectedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkUnSelectedImageView.setEnabled(false);
                callAddBookmarkApi();

            }
        });

        commentsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("post_uid",postUid);
                bundle.putString("pid",pid);
                bundle.putString("likes_count", String.valueOf(likesCount));
                bundle.putString("post_heading",postHeading);
                Intent intent=new Intent(SinglePost.this,CommentsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });




    }

    private void callAddBookmarkApi() {
        String bid= UUID.randomUUID().toString();
        APIInterface postLikeApi= RetrofitManager.getRetrofit().create(APIInterface.class);
        Call<ResponseBody> likeCall =postLikeApi.bookmarkPost("bearer " + token,bid,pid);

        likeCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SinglePost.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    bookmarkUnSelectedImageView.setEnabled(true);
                } else {
                    Toast.makeText(SinglePost.this, "bookmarked", Toast.LENGTH_SHORT).show();
                    bookmarkUnSelectedImageView.setVisibility(View.GONE);
                    bookmarkSelectedImageView.setVisibility(View.VISIBLE);
                    bookmarkUnSelectedImageView.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SinglePost.this, "Some error occured", Toast.LENGTH_SHORT).show();
                bookmarkUnSelectedImageView.setEnabled(true);
            }
        });
    }

    private void callRemoveBookmarkApi() {

        APIInterface postUnlikeApi= RetrofitManager.getRetrofit().create(APIInterface.class);
        Call<ResponseBody> likeCall =postUnlikeApi.unBookmarkPost("bearer " + token,pid);

        likeCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SinglePost.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    bookmarkSelectedImageView.setEnabled(true);

                } else {
                    Toast.makeText(SinglePost.this, "bookmark removed", Toast.LENGTH_SHORT).show();
                    bookmarkSelectedImageView.setVisibility(View.GONE);
                    bookmarkUnSelectedImageView.setVisibility(View.VISIBLE);
                    bookmarkSelectedImageView.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SinglePost.this, "Some error occured", Toast.LENGTH_SHORT).show();
                bookmarkSelectedImageView.setEnabled(true);
            }
        });

    }

    private void callLikeApi() {
        String lid= UUID.randomUUID().toString();
        APIInterface postLikeApi= RetrofitManager.getRetrofit().create(APIInterface.class);
        Call<ResponseBody> likeCall =postLikeApi.likePost("bearer " + token,lid,pid);

        likeCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SinglePost.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    likeUnSelectedImageView.setEnabled(true);

                } else {
                    Toast.makeText(SinglePost.this, "liked", Toast.LENGTH_SHORT).show();
                    likeUnSelectedImageView.setVisibility(View.GONE);
                    likeSelectedImageView.setVisibility(View.VISIBLE);
                    likeUnSelectedImageView.setEnabled(true);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SinglePost.this, "Some error occured", Toast.LENGTH_SHORT).show();
                likeUnSelectedImageView.setEnabled(true);
            }
        });
    }

    private void callUnlikeApi() {
        APIInterface postUnlikeApi= RetrofitManager.getRetrofit().create(APIInterface.class);
        Call<ResponseBody> likeCall =postUnlikeApi.unlikePost("bearer " + token,pid);


        likeCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SinglePost.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    likeSelectedImageView.setEnabled(true);

                } else {
                    Toast.makeText(SinglePost.this, "unliked", Toast.LENGTH_SHORT).show();
                    likeSelectedImageView.setVisibility(View.GONE);
                    likeUnSelectedImageView.setVisibility(View.VISIBLE);
                    likeSelectedImageView.setEnabled(true);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SinglePost.this, "Some error occured", Toast.LENGTH_SHORT).show();
                likeSelectedImageView.setEnabled(true);
            }
        });
    }

    private void callLoadApi() {
        APIInterface singlePostDataApi = RetrofitManager.getRetrofit().create(APIInterface.class);
        Call<SinglePostData> listCall = singlePostDataApi.getSinglePostData("bearer " + token, pid);
        Log.e(TAG, "callApi: here" );
        listCall.enqueue(new Callback<SinglePostData>() {
            @Override
            public void onResponse(Call<SinglePostData> call, Response<SinglePostData> response) {
                if (!response.isSuccessful()){
                    Log.e(TAG, "onResponse: singlepost error:  " + response.code());
                }
                else{
                    //List<SinglePostData.postData> singlePostData= response.body().getPostData();
                    setViews(response);
                  //  Log.e(TAG, "onResponse: heading: " + response.body().getPostData().get(0).getPost_heading() );
                }

            }

            @Override
            public void onFailure(Call<SinglePostData> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage().toString() );
            }
        });
    }

    private void setViews(Response<SinglePostData> response) {
        int isLikedByCurrentUser, isBookmarked;
        postHeading=response.body().getPostData().get(0).getPost_heading().toString();
        postDescription=response.body().getPostData().get(0).getDesc();
        likesCount=response.body().getLikes_count();
        timeStamp= response.body().getPostData().get(0).getTime_stamp();
        postUid=response.body().getPostData().get(0).getUid();
        isLikedByCurrentUser= response.body().getIs_liked_by_current_user();
        isBookmarked=response.body().getIs_bookmarked();
        postHeadingTv.setText(postHeading);
        postDescTv.setText(postDescription);

        //set time stamp
        long timeInMili = timeStamp.getTime();
        String timeAgo = TimeAgo.getTimeAgo(timeInMili);
        if (timeAgo == "ADD_DATE") {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd MMMM" + ", " + "EEE");
            final StringBuilder nowMMDDYYYY = new StringBuilder(dateformatMMDDYYYY.format(timeStamp));
            timeStampTv.setText(nowMMDDYYYY.toString());
        } else {
            timeStampTv.setText(timeAgo);
        }

        //set moreTv, lessTv
        postDescTv.post(new Runnable() {
            @Override
            public void run() {
                int num=postDescTv.getLineCount();
                if (num > 4 ){
                    moreTv.setVisibility(View.VISIBLE);
                }

            }
        });
        moreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreTv.setVisibility(View.GONE);
                lessTv.setVisibility(View.VISIBLE);
                postDescTv.setMaxLines(Integer.MAX_VALUE);
            }
        });
       lessTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lessTv.setVisibility(View.GONE);
                moreTv.setVisibility(View.VISIBLE);
                postDescTv.setMaxLines(4);
            }
        });

       //set like icon
        if (isLikedByCurrentUser == 1){
            likeSelectedImageView.setVisibility(View.VISIBLE);
            likeUnSelectedImageView.setVisibility(View.GONE);
        }
        else if (isLikedByCurrentUser == 0){
            likeUnSelectedImageView.setVisibility(View.VISIBLE);
            likeSelectedImageView.setVisibility(View.GONE);
        }

        //set bookmark icon
        if (isBookmarked == 1){
            bookmarkSelectedImageView.setVisibility(View.VISIBLE);
            bookmarkUnSelectedImageView.setVisibility(View.GONE);
        }
        else if(isBookmarked == 0){
            bookmarkUnSelectedImageView.setVisibility(View.VISIBLE);
            bookmarkSelectedImageView.setVisibility(View.GONE);
        }





        Log.e(TAG, "setViews: liked: "+isLikedByCurrentUser );


    }
}