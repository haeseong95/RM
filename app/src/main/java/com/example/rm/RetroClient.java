package com.example.rm;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    private static Retrofit retrofit = null;
    public static final String BASE_URL = "http://jsonplaceholder.typicode.com";    // 로컬 호스트 자리에 서버를 돌리고 있는 서버의 ip 주소를 넣어야 함
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

    public void getDa() {

        // 인터페이스에서 getData는 retrouser를 list형태로 타입을 반환하니 call 써줘야 함, getData() 메소드만 사용X
        //Call<List<RetroUser>> exer = retroService.getData("1");     // id를 1로 수정함
        Call<List<RetroUser>> exer = getRetroService().getData("1");

        exer.enqueue(new Callback<List<RetroUser>>() {      // enqeue 비동기 통신 방식
            @Override
            public void onResponse(Call<List<RetroUser>> call, Response<List<RetroUser>> response) {
                if(response.isSuccessful()) {
                    List<RetroUser> user = response.body();


                    // 배열을 리턴해서 커뮤니티에서 settext해보기

                    Log.d("데이터 받기 성공!", user.get(0).getuTitle());
                }
            }

            @Override
            public void onFailure(Call<List<RetroUser>> call, Throwable t) {
                Log.e("RetroClient.getData", "ㅇ");
                t.printStackTrace();
            }
        });
    }

    /*
    public List<RetroUser> getDataUserId(){
        Call<List<RetroUser>> data = getRetroService().getUserId(1);
        data.enqueue(new Callback<List<RetroUser>>() {
            @Override
            public void onResponse(Call<List<RetroUser>> call, Response<List<RetroUser>> response) {

                if (response.isSuccessful()){


                    int user_id, id;
                    String title;

                    List<RetroUser> user = response.body();
                    List<RetroUser> userInfo = new ArrayList<>();

                    for(RetroUser u : user){
                        userInfo.add(u);
                    }


                }


            }

            @Override
            public void onFailure(Call<List<RetroUser>> call, Throwable t) {

            }
        });

    }


     */



}
