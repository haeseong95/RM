package com.example.rm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

// Retrofit의 인터페이스 (HTTP 작업 정의)
public interface RetroService {

    // 카테고리 버튼 클릭할 때 쓰레기 종류에 따라 분류해서 가져옴 (categoryInfo 메인 설명, 쓰레기 이름 -> 쓰레기 종류명, 이미지, 쓰레기 분리수거 방법)
    @GET("/trash/category")
    Call<List<RetroTrash>> getTrashInfo(@Query("category") String category);     // RetroTrash?category=plastic

    // multipart로 일단 이미지만 가져오는 거 테스트
    @Multipart
    @GET("/trash/image")
    Call<List<RetroUser>> getImage(@Part)

    // 중간 끝나고 할 거
    // 커뮤니티 게시판에 사용자가 글을 올릴 때
    @POST("/community")
    Call<List<RetroUser>> addPost(@Body RetroUser user);


    // 로그인
    @GET("login")
    Call<RetroUser> login(@Query("username") String username, @Query("password") String password);




    @GET("/posts")
    Call<List<RetroUser>> getData(@Query("userId") String id);

    @GET("/posts")  // 모든 객체의 필드값 가져옴
    Call<List<RetroUser>> getUserId(@Query("userId") int userid);


}
