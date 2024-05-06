package com.example.rm;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;

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


        RetroClient retroClient = new RetroClient();
        retroClient.getDa();    // 데이터 요청


    }

}
