package com.example.rm.category;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import java.util.ArrayList;

// 메인화면에서 쓰레기 종류 버튼 클릭 시 쓰레기 메인 설명+품목 보여줌
public class CategoryInfo extends AppCompatActivity {
    // 레이아웃
    TextView categoryTitle;
    ImageView btnBack;
    RecyclerView recyclerView;
    //
    private static final String tag = "카테고리 쓰레기 목록 화면";
    ArrayList<TrashData> trashData = new ArrayList<>(); // 쓰레기 목록 데이터 담음
    TrashAdapter trashAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_info);

        btnBack = (ImageView)findViewById(R.id.btn_back);
        categoryTitle = (TextView)findViewById(R.id.category_title);
        recyclerView = findViewById(R.id.trash_recyclerview);
        btnBack.setOnClickListener(v -> finish());
        categoryTitle.setText(getIntent().getStringExtra("category"));      // 쓰레기 종류의 이름을 상단 툴바에 표시

        // recyclerView
        setRecyclerView();
        getTrashData();
    }

    // recyclerView 초기화
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CategoryInfo.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        trashAdapter = new TrashAdapter(trashData, CategoryInfo.this);
        recyclerView.setAdapter(trashAdapter);
    }

    // 클릭한 카테고리 버튼에 해당하는 쓰레기 데이터 가져오기
    private void getTrashData(){
        trashData.add(new TrashData("Plastic Bottle", "Recycle as plastic", "hash001"));
        trashData.add(new TrashData("Glass Bottle", "Recycle as glass", "hash002"));
        trashData.add(new TrashData("Newspaper", "Recycle as paper", "hash003"));
        trashData.add(new TrashData("Aluminum Can", "Recycle as metal", "hash004"));
        trashAdapter.notifyDataSetChanged();
    }
}
