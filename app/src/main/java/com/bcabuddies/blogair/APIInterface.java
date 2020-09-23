package com.bcabuddies.blogair;

import com.bcabuddies.blogair.model.BlockedUsers;
import com.bcabuddies.blogair.model.HomeFeed;
import com.bcabuddies.blogair.model.LoginToken;
import com.bcabuddies.blogair.model.LoginUser;
import com.bcabuddies.blogair.model.ThumbImageResponse;
import com.bcabuddies.blogair.model.UserPosts;
import com.bcabuddies.blogair.utils.Constants;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    //Register a user
    @Headers(Constants.KEY_HEADER)
    @POST("/user/register")
    @FormUrlEncoded
    Call<LoginToken> registerUser(@Field("full_name") String fullName, @Field("email_id") String emailId, @Field("password") String password, @Field("uid") String uid );

    //Get home feed of logged in user
    @Headers(Constants.KEY_HEADER)
    @GET("/user/homeFeed/{page_no}")
    Call<HomeFeed> getHomeFeed(@Header("authorization") String token, @Path("page_no") int page_no);

    //get jwt token with email_id and password
    @Headers(Constants.KEY_HEADER)
    @POST("/user/login")
    @FormUrlEncoded
    Call<LoginToken> getToken(@Field("email_id") String email_id, @Field("password") String password);

    //get all posts of the current logged in user (user profile)
    @Headers(Constants.KEY_HEADER)
    @GET("/user/profile/posts/{uid}")
    Call<List<UserPosts>> getUserPosts(@Header("authorization") String token, @Path("uid") String uid);

    //add a new post
    @Headers(Constants.KEY_HEADER)
    @Multipart
    @POST("/user/post/addnew")
    Call<ResponseBody> addNewPost(@Header("authorization") String token, @Part("pid") RequestBody pid , @Part("post_desc") RequestBody postDesc, @Part MultipartBody.Part file, @Part("post_heading") RequestBody postHeading);

    //update user name and bio
    @Headers(Constants.KEY_HEADER)
    @POST("/user/update/nameandbio")
    @FormUrlEncoded
    Call<ResponseBody> updateNameAndBio(@Header("authorization") String token ,@Field("full_name") String full_name, @Field("bio") String bio);

    //update user profile image
    @Headers(Constants.KEY_HEADER)
    @Multipart
    @POST("/user/update/thumbimage")
    Call<ThumbImageResponse> updateThumbImage(@Header("authorization") String token, @Part MultipartBody.Part file);

    //change user password from app
    @Headers(Constants.KEY_HEADER)
    @FormUrlEncoded
    @POST("/user/update/password")
    Call<ResponseBody> changePassword(@Header("authorization") String token, @Field("old_password") String oldPassword, @Field("new_password") String newPassword);

    //get blocked users
    @Headers(Constants.KEY_HEADER)
    @GET("/user/blocked/users")
    Call<BlockedUsers> getBlockedUsers(@Header("authorization") String token );


/*
    //test jwt auth token
    @Headers("key: Pz6WbvhZAQGsUtAxRJK3vtXCrJDW6kb3yMwtnGKu2kpfT9RahulGaurqFWfvFptqftcF87mBbV7pJWmPCPR5fZentc3qQVTtGLbqbjvGquT5B8UT2Kvjk7BCUm7hqtkqmJ3yR6fMFdWkWwvRGjrtSZjs52TdKC5Xazvp6b22pKNQSybvNb4mAwwuzXQFLKM7Pq5htpNNg8ZJ9dZJUF8gqc3aFXywYvaFLMXWdNUfErL8GEgUR3sEpNajEXbUcL22")
    @GET("/user/login/test")
    Call<LoginUser> getUser(@Header("authorization" ) String token);
*/


}
