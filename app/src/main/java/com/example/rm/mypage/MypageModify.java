package com.example.rm.mypage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import java.util.ArrayList;

public class MypageModify extends AppCompatActivity {
    // 레이아웃
    private RecyclerView recyclerView;
    private AdapterPost adapterPost;
    private AdapterComment adapterComment;
    private Button btnPosts, btnComments;
    View indicatorPosts, indicatorComments;
    //
    private ArrayList<PostData> postsData = new ArrayList<>();
    private ArrayList<CommentData> commentsData = new ArrayList<>();


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
        adapterPost = new AdapterPost(this, postsData);
        adapterComment = new AdapterComment(this, commentsData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterPost);

        btnPosts.setOnClickListener(v -> showPosts());
        btnComments.setOnClickListener(v -> showComments());
    }

    private void initializeData() {
        // 예시 데이터 추가 (실제 데이터로 대체)
        postsData.add(new PostData("2023-05-23", "첫 번째 게시글 제목", "d"));
        commentsData.add(new CommentData("2023-05-23", "첫 번째 댓글 내용", "dd"));
    }

    private void showPosts() {
        indicatorPosts.setVisibility(View.VISIBLE);
        indicatorComments.setVisibility(View.INVISIBLE);
        btnPosts.setTextColor(getResources().getColor(R.color.black));
        btnComments.setTextColor(getResources().getColor(R.color.gray));
        recyclerView.setAdapter(adapterPost);
    }

    private void showComments() {
        indicatorComments.setVisibility(View.VISIBLE);
        indicatorPosts.setVisibility(View.INVISIBLE);
        btnPosts.setTextColor(getResources().getColor(R.color.gray));
        btnComments.setTextColor(getResources().getColor(R.color.black));
        recyclerView.setAdapter(adapterComment);
    }


}


