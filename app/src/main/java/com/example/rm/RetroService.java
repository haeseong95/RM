package com.example.rm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// Retrofit의 인터페이스 (HTTP 작업 정의)
public interface RetroService {

    @GET("trash/{category}")
    Call<List<RetroTrash>> getTrashInfo(@Path("category") String category);     //Path : 동적 데이터의 변수 선언

    @GET("/posts")  //~com/posts 라는 곳에 존재한 json 데이터 사용
    Call<List<RetroUser>> getData(@Query("userId") String id);

}
