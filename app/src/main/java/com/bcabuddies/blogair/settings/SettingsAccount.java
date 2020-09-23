package com.bcabuddies.blogair.settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bcabuddies.blogair.APIInterface;
import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.home.AddPost;
import com.bcabuddies.blogair.model.ThumbImageResponse;
import com.bcabuddies.blogair.utils.Constants;
import com.bcabuddies.blogair.utils.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsAccount extends AppCompatActivity {

    Uri thumbImageUri = null;
    File thumb_filePathUri=null;
    private static final String TAG = "SettingsAccount";
    TextInputEditText userBioEt,fullNameEt;
    CircleImageView thumbImage;
    ImageView addImageIcon, backImageIcon;
    Button saveBtn;
    PreferenceManager preferenceManager;
    String prevBio;

    String thumbImageUrl, fullName,bio,token;
    int lastSpecialRequestsCursorPosition;
    String specialRequests = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_account);

        preferenceManager = new PreferenceManager(this);
        userBioEt = findViewById(R.id.settingsscc_biotv);
        thumbImage = findViewById(R.id.settingsscc_thumbimage);
        addImageIcon = findViewById(R.id.settingsscc_addphotoicon);
        saveBtn = findViewById(R.id.settingsscc_savebtn);
        fullNameEt=findViewById(R.id.settingsscc_nametv);
        backImageIcon=findViewById(R.id.settingsscc_backicon);

        thumbImageUrl = preferenceManager.getString(Constants.KEY_THUMB_IMAGE);
        prevBio=preferenceManager.getString(Constants.KEY_USER_BIO);
        fullName = preferenceManager.getString(Constants.KEY_FUll_NAME);
        token=preferenceManager.getString(Constants.KEY_JWT_TOKEN);


        //limit user bio input lines
        userBioEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastSpecialRequestsCursorPosition = userBioEt.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userBioEt.removeTextChangedListener(this);

                if (userBioEt.getLineCount() > 5) {
                    userBioEt.setText(specialRequests);
                    userBioEt.setSelection(lastSpecialRequestsCursorPosition);
                } else
                    specialRequests = userBioEt.getText().toString();

                userBioEt.addTextChangedListener(this);
            }
        });

        Glide.with(this).load(thumbImageUrl).into(thumbImage);
        fullNameEt.setText(fullName);
        userBioEt.setText(prevBio);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName=fullNameEt.getText().toString();
                bio=userBioEt.getText().toString();
                Log.e(TAG, "onClick: called" );
                callDetailsApi(token);
            }
        });


        addImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });

        backImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsAccount.this.finish();
            }
        });


    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setBorderCornerColor(Color.TRANSPARENT)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)
                .start(this);
    }

    private void callDetailsApi(String token) {
        Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        APIInterface updateDetailsApi= retrofit.create(APIInterface.class);
        Log.e(TAG, "onCreate: token: "+token );

        Call<ResponseBody> updateCall= updateDetailsApi.updateNameAndBio("bearer " + token,fullName,bio);

        updateCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                    Log.e(TAG, "onResponse: error while updating user details:   "+response.code() );
                }
                else{
                    Toast.makeText(SettingsAccount.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    preferenceManager.saveString(Constants.KEY_USER_BIO,bio);
                    SettingsAccount.this.finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: update error:  "+t.getMessage() );
                Toast.makeText(SettingsAccount.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Log.e(TAG, "onActivityResult: code crop image ");
            Log.e(TAG, "onActivityResult: code:: " + requestCode);

            if (requestCode == 203) {
                Log.e(TAG, "onActivityResult: " + thumbImageUri);
                try{
                    thumbImageUri = result.getUri();
                   // thumbImage.setImageURI(thumbImageUri);
                    thumb_filePathUri = new File(thumbImageUri.getPath());
                    callThumbImageApi(thumbImageUri);
                    Log.e(TAG, "onActivityResult: thumb_filepath: "+thumb_filePathUri );
                }catch (Exception e){
                    Log.e(TAG, "onActivityResult: exception:" +e.getMessage());
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

    private void callThumbImageApi(Uri thumbImageUri) {

        Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        APIInterface updateDetailsApi= retrofit.create(APIInterface.class);
        Log.e(TAG, "onCreate: token: "+token );

        RequestBody requestFile = RequestBody.create(okhttp3.MediaType.parse("multipart/form-data"), thumb_filePathUri);
        MultipartBody.Part mBody =
                MultipartBody.Part.createFormData("profile_image", thumb_filePathUri.getName(), requestFile);


        Call<ThumbImageResponse> updateCall= updateDetailsApi.updateThumbImage("bearer " + token,mBody);

        updateCall.enqueue(new Callback<ThumbImageResponse>() {
            @Override
            public void onResponse(Call<ThumbImageResponse> call, Response<ThumbImageResponse> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(SettingsAccount.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.e(TAG, "onResponse: response: "+response.body().getThumb_image() );
                    preferenceManager.saveString(Constants.KEY_THUMB_IMAGE,response.body().getThumb_image());
                    Toast.makeText(SettingsAccount.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    thumbImage.setImageURI(thumbImageUri);
                }
            }

            @Override
            public void onFailure(Call<ThumbImageResponse> call, Throwable t) {

            }
        });



    }
}