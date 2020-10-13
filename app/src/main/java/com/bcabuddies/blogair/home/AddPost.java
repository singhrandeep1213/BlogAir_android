package com.bcabuddies.blogair.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bcabuddies.blogair.retrofit.APIInterface;
import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.retrofit.RetrofitManager;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class AddPost extends AppCompatActivity {

    Uri postImageUri = null;
    ImageView postImageView, secondaryImageView, backIcon;
    ConstraintLayout primaryLayout, secondaryLayout;
    private static final String TAG = "AddPost";
    TextInputLayout headingTextLayout, descTextLayout;
    Button addPostButton;
    private Bitmap post_Bitmap = null;
    private byte[] thumb_byte;
    PreferenceManager preferenceManager;
    String token, pid;
    File thumb_filePathUri=null;


    String postHeading="", postDescription="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        preferenceManager = new PreferenceManager(this);
        token = preferenceManager.getString(Constants.KEY_JWT_TOKEN);
        postImageView = findViewById(R.id.addpost_imageview);
        secondaryImageView = findViewById(R.id.addpost_secondaryimage);
        primaryLayout = findViewById(R.id.addpost_primarylayout);
        secondaryLayout = findViewById(R.id.addpost_secondarylayout);
        headingTextLayout = findViewById(R.id.addpost_headlayout);
        descTextLayout = findViewById(R.id.addpost_captionlayout);
        addPostButton = findViewById(R.id.addpost_addbtn);
        backIcon=findViewById(R.id.addpost_backicon);

        imagePicker();

        postImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                primaryLayout.setVisibility(View.GONE);
                secondaryLayout.setVisibility(View.VISIBLE);
            }
        });
        secondaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                primaryLayout.setVisibility(View.VISIBLE);
                secondaryLayout.setVisibility(View.GONE);
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPost.this.finish();
            }
        });
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postHeading=headingTextLayout.getEditText().getText().toString().trim();
                postDescription=descTextLayout.getEditText().getText().toString().trim();
                pid = UUID.randomUUID().toString();
                callApi();
            }
        });

    }

    private void callApi() {

        RequestBody requestFile = RequestBody.create(okhttp3.MediaType.parse("multipart/form-data"), thumb_filePathUri);
        RequestBody pid= RequestBody.create(okhttp3.MediaType.parse("text/plain"), this.pid);
        RequestBody postDescription =RequestBody.create(okhttp3.MediaType.parse("text/plain"), this.postDescription);
        RequestBody postHeading =RequestBody.create(okhttp3.MediaType.parse("text/plain"), this.postHeading);

        MultipartBody.Part mBody =
                MultipartBody.Part.createFormData("post_image", thumb_filePathUri.getName(), requestFile);


        APIInterface jsonHomeFeedApi = RetrofitManager.getRetrofit().create(APIInterface.class);

        Call<ResponseBody> listCall = jsonHomeFeedApi.addNewPost("bearer " + token,pid,postDescription,mBody,postHeading );

        listCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                    Log.e(TAG, "onResponse: addpost error: "+response.code() );
                }
                else{
                    Log.e(TAG, "onResponse: addpost: "+response.body() );
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: addpost: "+t.getMessage().toString() );
            }
        });

    }

    private void imagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(100, 100)
                .setAspectRatio(16, 9)
                .start(AddPost.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Log.e(TAG, "onActivityResult: code crop image ");
            Log.e(TAG, "onActivityResult: code:: " + requestCode);
            if (requestCode == 203) {
                Log.e(TAG, "onActivityResult: " + postImageUri);
                try{
                    postImageUri = result.getUri();
                    postImageView.setImageURI(postImageUri);
                    secondaryImageView.setImageURI(postImageUri);
                    thumb_filePathUri = new File(postImageUri.getPath());
                    Log.e(TAG, "onActivityResult: thumb_filepath: "+thumb_filePathUri );
                }catch (Exception e){
                    AddPost.this.finish();
                    Log.e(TAG, "onActivityResult: exception:" );
                }

               /* try {
                    post_Bitmap = new Compressor(this).setMaxWidth(640).setMaxHeight(480).setQuality(50).compressToBitmap(thumb_filePathUri);
                    Log.e(TAG, "onActivityResult: successfully compressed:  "+post_Bitmap );
                } catch (IOException e) {
                    Log.e(TAG, "onActivityResult: error in compressing" );
                    e.printStackTrace();
                }
              ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                post_Bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                thumb_byte = byteArrayOutputStream.toByteArray();
                Log.e(TAG, "onActivityResult: thumb_byte "+thumb_byte.toString());*/
            } else {
                Log.e(TAG, "onActivityResult: crop failed error:  " + result.getError());
            }
        }

    }
}