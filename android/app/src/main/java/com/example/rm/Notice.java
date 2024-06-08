package com.example.rm;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class Notice extends AppCompatActivity {
    ImageView btnBack;
    TextView textView;
    static final String tag = "공지사항 상세 페이지";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);
        textView = findViewById(R.id.notice_cotent);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
        String content = getIntent().getStringExtra("user_notice_content"); // 공지사항 내용 가져옴
        textView.setText(content);
    }
}