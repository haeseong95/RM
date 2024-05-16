package com.example.rm.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.R;

public class Community extends AppCompatActivity {
    // 레이아웃
    ImageView btnBack;
    EditText searchBar;     // 검색창
    LinearLayout btnWrite;  // 글쓰기 아이콘
    ListView listView;
    Button btnMorePost, test;     // 더보기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        test = findViewById(R.id.content_test);
        btnBack = findViewById(R.id.btn_back);
        btnWrite = findViewById(R.id.c_write);
        searchBar = findViewById(R.id.c_search);
        btnMorePost = findViewById(R.id.btn_morepost);
        btnBack.setOnClickListener(v -> finish());

        btnWrite.setOnClickListener(new View.OnClickListener() {    // 연필 아이콘 클릭 -> 글쓰기 화면
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community.this, CommunityEdit.class);
                startActivity(intent);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {    // 연필 아이콘 클릭 -> 글쓰기 화면
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community.this, CommunityContent.class);
                startActivity(intent);
            }
        });

        // edittext로 설정한 검색창에 입력값이랑 같은 게시글 제목을 입력하면 listview에 뜨게 함 (제목만 할거임)
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 더보기 버튼을 클릭하면 listview 목록이 10개씩 추가되게 하기
        btnMorePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }





}
