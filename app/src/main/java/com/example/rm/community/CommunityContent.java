package com.example.rm.community;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rm.R;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class CommunityContent extends AppCompatActivity {
    // 레이아웃
    ViewPager2 viewPager2;
    ViewPagerAdapter adapter;
    FragmentStateAdapter fragmentStateAdapter;
    CircleIndicator3 indicator3;
    ImageView btnBack;
    ImageView like;  // 상단 사진, 추천 따봉 이미지
    TextView cNickname, cLevel, cDate, cTitle, cContent, cCount;    // 닉네임, 등급, 생성날짜, 게시글 제목, 게시글 내용, 추천 개수
    ListView listView;  // 댓글
    EditText editText;  // 댓글 입력창

    // 값
    private static final String tag = "CommunityContent";
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();    // viewpager의 이미지를 담을 bitmap 리스트

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_content);

        btnBack = findViewById(R.id.btn_back);
        cNickname = findViewById(R.id.cc_nickname);
        cLevel = findViewById(R.id.cc_level);
        cDate = findViewById(R.id.cc_date);
        cTitle = findViewById(R.id.cc_title);
        cContent = findViewById(R.id.cc_content);
        cCount = findViewById(R.id.cc_count);
        listView = findViewById(R.id.cc_comment);
        editText = findViewById(R.id.cc_edit_comment);
        viewPager2 = findViewById(R.id.viewpager);
        indicator3 = findViewById(R.id.indicator);

        // 뷰페이저

        viewPager2.setAdapter(fragmentStateAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);   // 가로 슬라이드


        // 뷰페이저 어댑터


        // 인디케이터
        indicator3.setViewPager(viewPager2);
        indicator3.createIndicators(5, 0);


        btnBack.setOnClickListener(v -> finish());

    }

    // 뷰페이저에서 보여줄 이미지 얻음
    private void getViewPagerImage(){
        adapter = new ViewPagerAdapter(getApplicationContext(), bitmapArrayList); // 어댑터 생성




    }





}
