package com.constantine2.student.retrofit;

import com.constantine2.student.model.Post;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostApi {

//    @Multipart
//    @POST("/post")
//    Call<Post> createPost(@Body Post post, @Part MultipartBody.Part picture);

    @POST("/post/only")
    Call<Post> createPostOnly(@Body Post post);

    @Multipart
    @POST("/post/picture")
    Call<String> createPostPicture(@Part MultipartBody.Part picture);


//    @Multipart
//    @POST("/post/{fire_id}")
//    Call<List<Post>> getPostsOfFire(@Path("fire_id") String fire_id);
//
//    @GET("/post/{pictureId}")
//    Call<ResponseBody> getPostPicture(@Path("pictureId") String pictureId);
}
