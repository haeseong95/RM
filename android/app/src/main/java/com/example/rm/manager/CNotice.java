package com.example.rm.manager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import java.util.ArrayList;

public class CNotice extends AppCompatActivity {

    private EditText noticeSearch;
    private ImageView btnBack, searchIcon;
    private LinearLayout writeIcon;
    private RecyclerView recyclerView;
    private TextView categoryTitle;
    private CNoticeAdapter cNoticeAdapter;
    private ArrayList<CNoticeList> noticeList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_notice); // XML 파일명에 맞게 수정

        // XML 뷰 요소 초기화
        btnBack = findViewById(R.id.btn_back);
        noticeSearch = findViewById(R.id.m_notice_search);
        writeIcon = findViewById(R.id.m_notice_write);
        recyclerView = findViewById(R.id.main_recyclerview);
        categoryTitle = findViewById(R.id.category_title);

        // 뒤로가기 버튼 클릭 이벤트 설정
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 현재 액티비티 종료
            }
        });

        // RecyclerView 설정
        setNoticeRecycler();

        // Search 기능 추가 (예: 검색 아이콘 클릭 시 검색 실행)
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = noticeSearch.getText().toString();
                searchNotices(query);
            }
        });

        // Write Icon 클릭 이벤트 설정 (예: 공지사항 작성 화면으로 이동)
        writeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle write icon click event
                // Intent intent = new Intent(CNotice.this, WriteNoticeActivity.class);
                // startActivity(intent);
            }
        });
    }

    private void setNoticeRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CNotice.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        cNoticeAdapter = new CNoticeAdapter(noticeList, CNotice.this);
        recyclerView.setAdapter(cNoticeAdapter);
    }

    // 공지사항 검색 함수 (검색 기능 예시)
    private void searchNotices(String query) {
        // Perform search and update noticeList
        // cNoticeAdapter.notifyDataSetChanged();
    }
}





