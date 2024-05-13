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
    ImageView btnBack;
    EditText searchBar;     // 검색창
    LinearLayout btnWrite;  // 글쓰기 아이콘
    ListView listView;
    Button btnMorePost;     // 더보기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        btnBack = findViewById(R.id.btn_back);
        btnWrite = findViewById(R.id.c_write);
        searchBar = findViewById(R.id.c_search);
        listView = findViewById(R.id.com_listview);
        btnMorePost = findViewById(R.id.btn_morepost);
        btnBack.setOnClickListener(v -> finish());

        btnWrite.setOnClickListener(new View.OnClickListener() {    // 연필 아이콘을 클릭하면 글쓰기 화면으로 넘어감
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community.this, CommunityEdit.class);
                startActivity(intent);
            }
        });

        // 1. edittext로 설정한 검색창에 입력값이랑 같은 게시글 제목을 입력하면 listview에 뜨게 함 (제목만 할거임)
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 2. 더보기 버튼을 클릭하면 listview 목록이 10개씩 추가되게 하기
        btnMorePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    // 3. listview엔 사용자들이 작성한 게시물들이 최신순으로 올라오는데 기본 10개




}
