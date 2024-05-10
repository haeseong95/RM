package com.example.rm;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private static Retrofit retrofit = null;

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    //public static final String BASE_URL = "http://192.168.0.36:5901";
    public static Gson gson = new GsonBuilder().setLenient().create();

    // retrofit 객체 생성
    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    // getInstance로 생성된 retrofit 클라이언트로 http API 명세가 담긴 인터페이스 구현체 생성
    public static RetroService getRetroService(){
        return getInstance().create(RetroService.class);
    }
}
