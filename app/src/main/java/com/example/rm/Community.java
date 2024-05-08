package com.example.rm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Community extends AppCompatActivity {

    ImageView btnBack;
    TextView testapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        btnBack = (ImageView)findViewById(R.id.btn_back);
        testapi = findViewById(R.id.test);

        // 뒤로 가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setUser();
    }

    private void setUser() {
        Call<RetroApi> call = RetroClient.getRetroService().getUser();
        call.enqueue(new Callback<RetroApi>() {
            @Override
            public void onResponse(Call<RetroApi> call, Response<RetroApi> response) {
                ArrayList<RetroUser> users = response.body().getUser();     // User 객체들의 리스트 반환

                if (response.isSuccessful()){
                    Log.i("json 연결 성공", users.get(0).getEmail());
                } else {
                    Log.e("json 연결 실패", users.get(0).getEmail());
                }
            }

            @Override
            public void onFailure(Call<RetroApi> call, Throwable t) {
                Log.e("네트워크 연결 실패", "망", t);
            }
        });
    }


}
