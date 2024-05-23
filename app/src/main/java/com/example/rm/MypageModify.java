package com.example.rm;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MypageModify extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private CommentAdapter commentAdapter;
    private ArrayList<MypageModifyData> postsData = new ArrayList<>();
    private ArrayList<MypageModifyData> commentsData = new ArrayList<>();
    private Button btnPosts, btnComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_community_modify);

        recyclerView = findViewById(R.id.modify_recyclerview);
        btnPosts = findViewById(R.id.btn_posts);
        btnComments = findViewById(R.id.btn_comments);

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
        btnPosts.setBackgroundTintList(getResources().getColorStateList(R.color.selected_button_color));
        btnComments.setBackgroundTintList(getResources().getColorStateList(R.color.unselected_button_color));
        btnPosts.setTextColor(getResources().getColor(R.color.black));
        btnComments.setTextColor(getResources().getColor(R.color.gray));
        recyclerView.setAdapter(postAdapter);
    }

    private void showComments() {
        btnPosts.setBackgroundTintList(getResources().getColorStateList(R.color.unselected_button_color));
        btnComments.setBackgroundTintList(getResources().getColorStateList(R.color.selected_button_color));
        btnPosts.setTextColor(getResources().getColor(R.color.gray));
        btnComments.setTextColor(getResources().getColor(R.color.black));
        recyclerView.setAdapter(commentAdapter);
    }
}


