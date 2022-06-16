package com.example.taskplannernew.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anandsingh on 12/31/2016.
 */

public class RetrofitInterface {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();


    public static <S> S createService(Class<S> serviceClass, String baseUrl) {

        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return builder.create(serviceClass);
    }
}
