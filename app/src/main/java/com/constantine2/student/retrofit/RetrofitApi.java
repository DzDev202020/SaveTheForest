package com.constantine2.student.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {

    public static Retrofit retrofit;

    public static void initApi() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://172.16.9.2:8080")
//                    .baseUrl("http://172.0.0.0:8080")
//                    .baseUrl("http://10/.0.3.2:8080")
//                    .baseUrl("http://localhost:8080")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }


    }

    public static ClientApi getClientApi() {
        initApi();
        return retrofit.create(ClientApi.class);
    }

    public static FireApi getFireApi() {
        initApi();
        return retrofit.create(FireApi.class);
    }

    public static PostApi getPostApi() {
        initApi();
        return retrofit.create(PostApi.class);
    }

    public static ScaleApi getScaleApi() {
        initApi();
        return retrofit.create(ScaleApi.class);
    }

    public static String getPostPath() {
        return "http://172.16.9.2:8080/post/";
    }
}
