package com.constantine2.student.retrofit;

import com.constantine2.student.model.Client;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClientApi {

    @GET("/client")
    Call<Client> getClient(@Query("email") String email, @Query("password") String password);


    @POST("/client")
    Call<Client> createClient(@Body Client client);



//
//    @GET("users/list?sort=desc")
//
//
//    @GET("group/{id}/users")
//    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);
//
//    @GET("group/{id}/users")
//    Call<List<User>> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);
//
//    @POST("users/new")
//    Call<User> createUser(@Body User user);

}
