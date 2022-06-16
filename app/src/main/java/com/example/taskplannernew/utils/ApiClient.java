package com.example.taskplannernew.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static String BASE_URL = "http://abgo.in/api/";

    public static Retrofit getRecentTask() {
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }
}
