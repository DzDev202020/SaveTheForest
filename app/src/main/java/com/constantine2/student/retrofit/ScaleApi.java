package com.constantine2.student.retrofit;

import com.constantine2.student.model.Scale;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ScaleApi {


    @POST("/scale")
    Call<Scale> createScale(@Body Scale scale);
}
