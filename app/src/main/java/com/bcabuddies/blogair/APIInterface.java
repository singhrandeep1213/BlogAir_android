package com.bcabuddies.blogair;

import com.bcabuddies.blogair.model.HomeFeed;
import com.bcabuddies.blogair.model.LoginToken;
import com.bcabuddies.blogair.model.LoginUser;
import com.bcabuddies.blogair.model.UserPosts;
import com.bcabuddies.blogair.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
    Call<List<HomeFeed>> getHomeFeed(@Header("authorization") String token, @Path("page_no") int page_no);

    //get jwt token with email_id and password
    @Headers(Constants.KEY_HEADER)
    @POST("/user/login")
    @FormUrlEncoded
    Call<LoginToken> getToken(@Field("email_id") String email_id, @Field("password") String password);

    //get all posts of the current logged in user (user profile)
    @Headers(Constants.KEY_HEADER)
    @GET("/user/profile/posts/{uid}")
    Call<List<UserPosts>> getUserPosts(@Header("authorization") String token, @Path("uid") String uid);

/*
    //test jwt auth token
    @Headers("key: Pz6WbvhZAQGsUtAxRJK3vtXCrJDW6kb3yMwtnGKu2kpfT9RahulGaurqFWfvFptqftcF87mBbV7pJWmPCPR5fZentc3qQVTtGLbqbjvGquT5B8UT2Kvjk7BCUm7hqtkqmJ3yR6fMFdWkWwvRGjrtSZjs52TdKC5Xazvp6b22pKNQSybvNb4mAwwuzXQFLKM7Pq5htpNNg8ZJ9dZJUF8gqc3aFXywYvaFLMXWdNUfErL8GEgUR3sEpNajEXbUcL22")
    @GET("/user/login/test")
    Call<LoginUser> getUser(@Header("authorization" ) String token);
*/


}
