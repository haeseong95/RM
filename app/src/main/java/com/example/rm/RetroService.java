package com.example.rm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

// Retrofit의 인터페이스 (HTTP 작업 정의)
public interface RetroService {

    // 카테고리 메인 설명 (이것도 cateogory 값 받아서 다르게 출력되어야 함), 리스트뷰에 쓰레기 이름/설명/이미지 3개 가져오기)
    // 테스트
    @GET("/api/Writing")
    Call<RetroApi> setCategoryMain();

    // 카테고리 품목 리스트 (버튼 종류별로 다르게 나와야 함)
     @GET("/api/Trash")
    Call<RetroApi> setCategoryItem(@Query("category") String cat);









    //searchTrash에서 title, url을 listview를 출력 -> 검색창에서 title을 검색해서 겹치는 거 뽑음
    @GET("/photos")
    Call<List<RetroUser>> getSearchTrashData();

    // categoryInfo에선

    /*
    retrouser : 사용자 User 정의 / json의
    retrowriting : 쓰레기 정보 Wrting 정의


    listview
    1. searchview : 쓰레기 이름만 출력(태그는 나중에 생각) trashName -> trashNane,

     */







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
