package com.example.rm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MypageModify extends AppCompatActivity {

    RecyclerView recyclerView;  // 게시글 목록
    Button btnMorePost;    // 더보기 버튼

    private static final String tag = "Mypage 내 게시글/댓글 수정";

    ArrayList<MypageModifyData> arrayList = new ArrayList<>();     // 게시글 목록 데이터 저장
    MypageModifyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_community_modify);
        recyclerView = findViewById(R.id.modify_recyclerview);
        ImageView btnBack = findViewById(R.id.btn_back);
        btnMorePost = findViewById(R.id.btn_morepost);
        btnBack.setOnClickListener(v -> finish());

        // 게시글 목록
        getPostList();
        setRecyclerView();


    }

    // arrayList에서 게시글 목록 데이터 저장 (일단 더미 데이터로 recyclerview가 출력되는지 테스트)
    private void getPostList(){
        arrayList.add(new MypageModifyData("닉네임1 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new MypageModifyData("닉네임2 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new MypageModifyData("닉네임3 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new MypageModifyData("닉네임4 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new MypageModifyData("닉네임5 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new MypageModifyData("닉네임6 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new MypageModifyData("닉네임7 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new MypageModifyData("닉네임8 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new MypageModifyData("닉네임9 ", "등급 ", "2024-05-19", "제목 "));
        arrayList.add(new MypageModifyData("닉네임10 ", "등급 ", "2024-05-19", "제목 "));


    }


    // recyclerview 초기화
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MypageModify.this);
        recyclerView.setLayoutManager(linearLayoutManager); // layoutManager 설정
        adapter = new MypageModifyAdapter(MypageModify.this, arrayList);
        recyclerView.setAdapter(adapter);
    }

}
