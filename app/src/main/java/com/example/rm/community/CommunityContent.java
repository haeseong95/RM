package com.example.rm.community;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.R;

public class CommunityContent extends AppCompatActivity {
    ImageView btnBack;
    ImageView showImage, like;  // 상단 사진, 추천 따봉 이미지
    TextView cNickname, cLevel, cDate, cTitle, cContent, cCount;    // 닉네임, 등급, 생성날짜, 게시글 제목, 게시글 내용, 추천 개수
    ListView listView;  // 댓글
    EditText editText;  // 댓글 입력창

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_content);

        btnBack = findViewById(R.id.btn_back);
        showImage = findViewById(R.id.cc_images);
        like = findViewById(R.id.cc_thumbs);
        cNickname = findViewById(R.id.cc_nickname);
        cLevel = findViewById(R.id.cc_level);
        cDate = findViewById(R.id.cc_date);
        cTitle = findViewById(R.id.cc_title);
        cContent = findViewById(R.id.cc_content);
        cCount = findViewById(R.id.cc_count);
        listView = findViewById(R.id.cc_comment);
        editText = findViewById(R.id.cc_edit_comment);
        btnBack.setOnClickListener(v -> finish());





    }





}
