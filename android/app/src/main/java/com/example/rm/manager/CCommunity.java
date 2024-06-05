package com.example.rm.manager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import java.util.ArrayList;

public class CCommunity extends AppCompatActivity {
    // 레이아웃
    ImageView imageView;
    RecyclerView recyclerView;
    View vPost, vComment;
    Button btnPost, btnComment;
    //
    CAdapterPost postAdapter;
    CAdapterComment commentAdapter;
    ArrayList<CommentList> commentList = new ArrayList<>(); // 댓글 데이터 담음
    ArrayList<PostList> postList = new ArrayList<>();   // 게시글 데이터 담음
    private static final String tag = "관리자_게시글/댓글 관리 페이지";


    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.id.manager_community);

        imageView = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.m_com_recyclerView);
        vPost = findViewById(R.id.indicator_post);
        vComment = findViewById(R.id.indicator_comment);
        btnPost = findViewById(R.id.btn_m_post);
        btnComment = findViewById(R.id.btn_m_comment);

        // 게시글
        setPostRecycler();


        // 댓글
    }

    private void setPostRecycler(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CCommunity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        postAdapter = new CAdapterPost(postList, CCommunity.this);
        recyclerView.setAdapter(postAdapter);
    }



}
