package com.example.rm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryInfo extends AppCompatActivity {

    TextView categoryTitle;     // 쓰레기 종류
    ImageView btnBack;     // 뒤로 가기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_info);

        btnBack = (ImageView)findViewById(R.id.btn_back);
        categoryTitle = (TextView)findViewById(R.id.category_title);

        categoryTitle.setText(getIntent().getStringExtra("category"));      // 쓰레기 종류의 이름을 상단 툴바에 표시

        // 뒤로 가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
