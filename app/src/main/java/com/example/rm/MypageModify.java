package com.example.rm;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MypageModify extends AppCompatActivity {
    // 레이아웃
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private CommentAdapter commentAdapter;
    private Button btnPosts, btnComments;
    View indicatorPosts, indicatorComments;
    //
    private ArrayList<MypageModifyData> postsData = new ArrayList<>();
    private ArrayList<MypageModifyData> commentsData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_community_modify);

        recyclerView = findViewById(R.id.modify_recyclerview);
        btnPosts = findViewById(R.id.btn_posts);
        btnComments = findViewById(R.id.btn_comments);
        indicatorPosts = findViewById(R.id.indicator_posts);
        indicatorComments = findViewById(R.id.indicator_comments);

        // 초기 데이터 설정 (예시 데이터 추가)
        initializeData();


        // 초기에는 게시글 목록을 표시
        postAdapter = new PostAdapter(this, postsData);
        commentAdapter = new CommentAdapter(this, commentsData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);

        btnPosts.setOnClickListener(v -> showPosts());
        btnComments.setOnClickListener(v -> showComments());
    }

    private void initializeData() {
        // 예시 데이터 추가 (실제 데이터로 대체)
        postsData.add(new MypageModifyData("2023-05-23", "첫 번째 게시글 제목"));
        commentsData.add(new MypageModifyData("2023-05-23", "첫 번째 댓글 내용"));
    }

    private void showPosts() {
        indicatorPosts.setVisibility(View.VISIBLE);
        indicatorComments.setVisibility(View.INVISIBLE);
        btnPosts.setTextColor(getResources().getColor(R.color.black));
        btnComments.setTextColor(getResources().getColor(R.color.gray));
        recyclerView.setAdapter(postAdapter);
    }

    private void showComments() {
        indicatorComments.setVisibility(View.VISIBLE);
        indicatorPosts.setVisibility(View.INVISIBLE);
        btnPosts.setTextColor(getResources().getColor(R.color.gray));
        btnComments.setTextColor(getResources().getColor(R.color.black));
        recyclerView.setAdapter(commentAdapter);
    }


}


