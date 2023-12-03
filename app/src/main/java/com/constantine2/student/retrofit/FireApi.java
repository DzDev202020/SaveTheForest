package com.constantine2.student.retrofit;

import com.constantine2.student.model.Fire;
import com.constantine2.student.model.FireList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FireApi {


    @GET("/fire")
    Call<List<Fire>> getFires();

//    @GET("/fire")
//    Call<String> getFiresList();
//
    @GET("/fire")
    Call<FireList> getFiresList();

    @GET("fire/{fire_id}")
    Call<Fire> getFireById(@Path("fire_id") int fire_id);

    @POST("/fire")
    Call<Fire> createFire(@Body Fire fire);

}
