package com.example.rm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryInfo extends AppCompatActivity {

    TextView categoryTitle, trashInfo;     // 쓰레기 종류
    ImageView btnBack;
    ListView listView;
    ArrayList<TrashListData> arrayList = new ArrayList<>();;     // TrashListData 클래스 사용
    TrashAdapter trashAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_info);

        btnBack = (ImageView)findViewById(R.id.btn_back);
        categoryTitle = (TextView)findViewById(R.id.category_title);
        trashInfo = (TextView)findViewById(R.id.trash_info);
        listView = (ListView)findViewById(R.id.category_listview);

        categoryTitle.setText(getIntent().getStringExtra("category"));      // 쓰레기 종류의 이름을 상단 툴바에 표시



        trashAdapter = new TrashAdapter(this, arrayList); // 어댑터 생성, arrayList는 비어있는 상태
        setListView();        // listview 설정
        clickListviewItem();    // listview 목록 클릭하면 상세 페이지로 이동


        // 리스트뷰의 높이를 계산에서 layout 크기를 설정
        int totalHeight = 0;
        for (int i = 0; i < trashAdapter.getCount(); i++){
            View listItem = trashAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (trashAdapter.getCount() - 1));
        listView.setLayoutParams(params);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    // 리스트 뷰 설정
    public void setListView() {

        // arrayList에 항목 추가
        arrayList.add(new TrashListData("쓰레기 이름1", "설명2", R.drawable.ci_responsive_design));
        arrayList.add(new TrashListData("이름2", "설명2", R.drawable.cat_battery));
        arrayList.add(new TrashListData("쓰레기 이름1", "설명2", R.drawable.ci_responsive_design));
        arrayList.add(new TrashListData("이름2", "설명2", R.drawable.cat_battery));
        arrayList.add(new TrashListData("쓰레기 이름1", "설명2", R.drawable.ci_responsive_design));
        arrayList.add(new TrashListData("이름2", "설명2", R.drawable.cat_battery));
        listView.setAdapter(trashAdapter);      // 리스트 뷰에 어댑터 붙임
        listView.setClickable(true);        // 목록 클릭O
    }


    // listview 목록 클릭하면 쓰레기 상세 페이지로 데이터 전달
    public void clickListviewItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long I) {
                Intent intent = new Intent(CategoryInfo.this, TrashDetail.class);
                intent.putExtra("trashName", arrayList.get(position).trashName);
                intent.putExtra("trashImage", Integer.toString(arrayList.get(position).trashImage));
                intent.putExtra("trashInfo", arrayList.get(position).trashInfo);
                startActivity(intent);
            }
        });
    }




}
