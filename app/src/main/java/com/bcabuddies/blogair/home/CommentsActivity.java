package com.bcabuddies.blogair.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.adapter.CommentsRecyclerAdapter;
import com.bcabuddies.blogair.model.Comments;
import com.bcabuddies.blogair.retrofit.APIInterface;
import com.bcabuddies.blogair.retrofit.RetrofitManager;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity {

    Bundle bundle;
    String pid, postUid, post_likes_count, postHeading;
    RecyclerView recyclerView;
    TextView postHeadingTv, likesCountTv;
    ImageView refreshIcon, postIcon;
    EditText commentDescEt;
    ImageView backIcon;
    CommentsRecyclerAdapter commentsRecyclerAdapter;
    List<Comments.comments> finalList;
    PreferenceManager preferenceManager;
    String token;
    String commentDesc;
    boolean noMoreResults = false;
    int lastSpecialRequestsCursorPosition;
    String specialRequests = "";
    private static final String TAG = "CommentsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        bundle = this.getIntent().getExtras();
        pid = bundle.getString("pid");
        postUid = bundle.getString("post_uid");
        postHeading = bundle.getString("post_heading");
        preferenceManager = new PreferenceManager(this);
        token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        postHeadingTv = findViewById(R.id.comments_postheading);
        postHeadingTv.setText(postHeading);

        backIcon=findViewById(R.id.comments_backicon);
        refreshIcon = findViewById(R.id.comments_refreshicon);
        commentDescEt = findViewById(R.id.comments_desc);
        postIcon= findViewById(R.id.comments_posticon);
        likesCountTv=findViewById(R.id.comments_likescount);

        finalList = new ArrayList<>();
        callLoadApi();
        recyclerViewInit();


        //set comments input limit
        commentDescEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastSpecialRequestsCursorPosition = commentDescEt.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                commentDescEt.removeTextChangedListener(this);

                if (commentDescEt.getLineCount() > 3) {
                    commentDescEt.setText(specialRequests);
                    commentDescEt.setSelection(lastSpecialRequestsCursorPosition);
                } else
                    specialRequests = commentDescEt.getText().toString();

                commentDescEt.addTextChangedListener(this);
            }
        });

        //set refresh icon
        refreshIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalList.clear();
                callLoadApi();
            }
        });

        //set post icon
        postIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentDesc= commentDescEt.getText().toString();
                if (commentDesc.equals("")|| commentDesc.equals(null)){

                    Toast.makeText(CommentsActivity.this , "Please write a comment", Toast.LENGTH_SHORT).show();
                }
                else{
                    postIcon.setEnabled(false);
                    callPostApi();
                    finalList.clear();
                    callLoadApi();
                    commentDescEt.setText(null);
                }
            }
        });

        //set back icon
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentsActivity.this.finish();
            }
        });



    }

    private void callPostApi() {
        String cid= UUID.randomUUID().toString();
        APIInterface jsonHomeFeedApi = RetrofitManager.getRetrofit().create(APIInterface.class);

        Call<ResponseBody> listCall = jsonHomeFeedApi.addComment("bearer " + token, cid,pid, commentDesc);
        listCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(CommentsActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    postIcon.setEnabled(true);
                }
                else {
                    Toast.makeText(CommentsActivity.this, "comment posted", Toast.LENGTH_SHORT).show();
                    postIcon.setEnabled(true);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CommentsActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                postIcon.setEnabled(true);
            }
        });

    }

    private void recyclerViewInit() {
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(this, finalList, postUid);
        recyclerView = findViewById(R.id.comments_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentsRecyclerAdapter);
    }

    private void callLoadApi() {
        APIInterface jsonHomeFeedApi = RetrofitManager.getRetrofit().create(APIInterface.class);

        Call<Comments> listCall = jsonHomeFeedApi.getPostComments("bearer " + token, pid);

        listCall.enqueue(new Callback<Comments>() {
            @Override
            public void onResponse(Call<Comments> call, Response<Comments> response) {

                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: error:  " + response.code());
                    Toast.makeText(CommentsActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getComments() != null) {
                        List<Comments.comments> commentsList = response.body().getComments();
                        finalList.addAll(commentsList);
                        commentsRecyclerAdapter.notifyDataSetChanged();
                        post_likes_count=response.body().getPost_likes_count();
                        if (post_likes_count.equals("1")){
                            likesCountTv.setText(" "+ post_likes_count + " person liked this.");
                        }
                        else{
                            likesCountTv.setText(" "+ post_likes_count + " people liked this.");
                        }

                        Log.e(TAG, "onResponse: blockedusers: " + finalList);
                    } else {
                        noMoreResults = true;
                    }

                }
            }

            @Override
            public void onFailure(Call<Comments> call, Throwable t) {
                Toast.makeText(CommentsActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

}