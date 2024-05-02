package com.example.rm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
    ArrayList<TrashListData> arrayList = new ArrayList<>();
    TrashAdapter trashAdapter = new TrashAdapter(CategoryInfo.this, arrayList); // 데이터-뷰 연결할 어댑터 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_info);

        btnBack = (ImageView)findViewById(R.id.btn_back);
        categoryTitle = (TextView)findViewById(R.id.category_title);
        trashInfo = (TextView)findViewById(R.id.trash_info);
        listView = (ListView)findViewById(R.id.category_listview);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        categoryTitle.setText(getIntent().getStringExtra("category"));      // 쓰레기 종류의 이름을 상단 툴바에 표시
        setListView();        // listview 설정
        clickListviewItem();    // listview 목록 클릭하면 상세 페이지로 이동


        /*

        // 정보 추가하기
        arrayList.add("List를 <문자열>로 선언했으니 무조건 문자열만 넣기");
        arrayList.add("잠온다");
        trashAdapter.notifyDataSetChanged();    // add로 추가한 윗 데이터를 저장함


        listView.setOnClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryInfo.this, TrashDetail.class);
                startActivity(intent);
            }
        });


         */

         /*
        Retrofit retrofit = new Retrofit.Builder().baseUrl("").build();
        final TrashRetroService retroService = retrofit.create(TrashRetroService.class);

        retroService.getTrashName(new Callback<List<TrashListData>>() {
            @Override
            public void onResponse(Response<List<TrashListData>> response, Retrofit retrofit1) {

                if(!response.isSuccessful()){
                    return;
                }

                List<TrashListData> data = response.body();

                ArrayAdapter adapter = new ArrayAdapter(CategoryInfo.this, data);
                listView.setAdapter(adapter);


            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
         */




    }

    // 리스트 뷰 설정
    public void setListView() {
        trashAdapter.add(R.drawable.ci_responsive_design, "쓰레기 이름1", "설명1");
        trashAdapter.add(R.drawable.ci_responsive_design, "쓰레기 이름2", "설명2");
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
