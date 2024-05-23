package com.example.rm.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import java.util.ArrayList;

public class Community extends AppCompatActivity {
    // 레이아웃
    ImageView btnBack;
    EditText searchBar;     // 검색창
    LinearLayout btnWrite;  // 글쓰기 아이콘
    RecyclerView recyclerView;  // 게시글 목록
    Button btnMorePost, test;     // 더보기 버튼

    //
    private static final String tag = "Community 게시판 메인";
    ArrayList<CommunityData> arrayList = new ArrayList<>();     // 게시글 목록 데이터 저장
    CommunityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);
        recyclerView = findViewById(R.id.main_recyclerview);
        test = findViewById(R.id.content_test);
        btnBack = findViewById(R.id.btn_back);
        btnWrite = findViewById(R.id.c_write);
        searchBar = findViewById(R.id.c_search);
        btnMorePost = findViewById(R.id.btn_morepost);
        btnBack.setOnClickListener(v -> finish());

        // 게시글 목록
        getPostList();
        setRecyclerView();


        btnWrite.setOnClickListener(new View.OnClickListener() {    // 연필 아이콘 클릭 -> 글쓰기 화면
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community.this, CommunityEdit.class);
                startActivity(intent);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {    // 게시글 상세 페이지
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community.this, CommunityContent.class);
                intent.putExtra("postId", "post1");
                startActivity(intent);
            }
        });

    }

    // arrayList에서 게시글 목록 데이터 저장 (일단 더미 데이터로 recyclerview가 출력되는지 테스트)
    private void getPostList(){
        arrayList.add(new CommunityData("닉네임1 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new CommunityData("닉네임2 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new CommunityData("닉네임3 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new CommunityData("닉네임4 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new CommunityData("닉네임5 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new CommunityData("닉네임6 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new CommunityData("닉네임7 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new CommunityData("닉네임8 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new CommunityData("닉네임9 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new CommunityData("닉네임10 ", "등급 ", "2024-05-19", "제목 "));


    }


    // recyclerview 초기화
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Community.this);
        recyclerView.setLayoutManager(linearLayoutManager); // layoutManager 설정
        adapter = new CommunityAdapter(Community.this, arrayList);
        recyclerView.setAdapter(adapter);
    }


}
