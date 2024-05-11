package com.example.rm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

// Retrofit의 인터페이스
public interface RetroService {

    // 카테고리 품목 리스트 (버튼 종류별로 다르게 나와야 함)
     @GET("/api/Trash")
    Call<RetroApi> setCategoryItem(@Query("category") String cat);



    // 카테고리 메인 설명
    @GET("/photos")
    Call<List<RetroWriting>> getCategoryMain(@Query("albumId") int sort);

    // 카테고리 쓰레기 품목
    // searchTrash는 쓰레기 종류 상관X 입력한 테스트 싹 다 출력 <-> categoryInfo는 버튼에 따라 출력될 리스트가 달라야 함 (일단 예시로 ambumId값으로 버튼 클릭 시 분류되게 하기, 실전에는 category의 입력받은 값으로 해야함 String)
    @GET("/photos")
    Call<List<RetroWriting>> getCategoryList(@Query("albumId") int sort);

    //searchTrash 검색어 입력
    @GET("/photos")
    Call<List<RetroWriting>> getSearchTrashData();

}
