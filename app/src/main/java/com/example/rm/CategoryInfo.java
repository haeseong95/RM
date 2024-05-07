package com.example.rm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryInfo extends AppCompatActivity {

    TextView categoryTitle, trashInfo;
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        trashAdapter = new TrashAdapter(this, arrayList); // 어댑터 생성, arrayList는 비어있는 상태
        setRetrofit();  // listivew에 json 데이터 추가
        listView.setAdapter(trashAdapter);      // 리스트 뷰에 어댑터 붙임
        listView.setClickable(true);        // 목록 클릭O
        clickListviewItem();    // listview 목록 클릭하면 상세 페이지로 이동

    }

    // retrofit, 네트워크 요청
    private void setRetrofit() {
        // getroservice() 반환값인 인터페이스 구현체 -> 인터페이스의 getUserId() 메소드에 구현된 API 호출함
        Call<List<RetroUser>> call = RetroClient.getRetroService().getUserId(1);

        call.enqueue(new Callback<List<RetroUser>>() {      // enqueue 비동기 통신, 큐에 삽입
            @Override
            public void onResponse(Call<List<RetroUser>> call, Response<List<RetroUser>> response) {

                // 쓰레기 종류별로 구분되어야 함 태그
                String tag = getIntent().getStringExtra("category");

                // 전체 데이터 중 태그를 이용해 값 구별해서 가져오기



                if (response.isSuccessful()) {  // HTTP 응답의 StatCode가 200~299인 경우 true 반환
                    String title, body = null;
                    List<RetroUser> retroUser = response.body();    // body()는 서버로부터 받은 응답 형식

                    for (RetroUser user : retroUser){   // user는 retroUser 리스트의 각 RetroUser 객체를 순차적으로 참조
                        title = user.getuTitle();
                        body = user.getuBody();
                        arrayList.add(new TrashListData(title, body));  // arrayList에 항목 추가
                        Log.i("값 전달", "title : " + title + ", body : "+ body);
                    }

                    trashAdapter.notifyDataSetChanged();    // listview 리스트의 크기+아이템 둘 다 변경될 때 사용 (=리스트 업데이트)

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

                }
            }

            @Override
            public void onFailure(Call<List<RetroUser>> call, Throwable t) {
                Log.e("리스트뷰 망", "ㅇ");
            }
        });
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
