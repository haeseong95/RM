package com.example.rm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// Retrofit의 인터페이스 (HTTP 작업 정의)
public interface RetroService {

    // 쓰레기 종류에 따라 정보 분류해서 가져옴
    @GET("/trash/category")
    Call<List<RetroTrash>> getTrashInfo(@Query("category") String category);     // RetroTrash?category=plastic

    @GET("/posts")  //~com/posts 라는 곳에 존재한 json 데이터 사용
    Call<List<RetroUser>> getData(@Query("userId") String id);

    @GET("/posts")  // 모든 객체의 필드값 가져옴
    Call<List<RetroUser>> getUserId(@Query("userId") int userId);


}
