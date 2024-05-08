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

    TextView categoryTitle;
    ImageView btnBack;
    ListView listView, listView2;
    ArrayList<TrashListData> arrayList = new ArrayList<>();;     // TrashListData 클래스 사용
    TrashAdapter trashAdapter, trashAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_info);

        btnBack = (ImageView)findViewById(R.id.btn_back);
        categoryTitle = (TextView)findViewById(R.id.category_title);
        listView = (ListView)findViewById(R.id.category_listview);
        listView = findViewById(R.id.category_main_listview);
        categoryTitle.setText(getIntent().getStringExtra("category"));      // 쓰레기 종류의 이름을 상단 툴바에 표시

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 쓰레기 메인 설명
        trashAdapter2 = new TrashAdapter(this, )


        // 쓰레기 목록
        trashAdapter = new TrashAdapter(this, arrayList);
        setRetrofit();
        listView.setAdapter(trashAdapter);
        listView.setClickable(true);
        clickListviewItem();
    }



    // retrofit, 리스트뷰 목록의 요청한 값 리스트뷰에 추가
    private void setRetrofit() {

        Call<List<RetroUser>> call = RetroClient.getRetroService().getUserId(1);    // RetroService()로 인터페이스 구현체 생성
        String tag = getIntent().getStringExtra("category");    // 쓰레기 종류별 구분해줄 값

        call.enqueue(new Callback<List<RetroUser>>() {
            @Override
            public void onResponse(Call<List<RetroUser>> call, Response<List<RetroUser>> response) {


                // 전체 데이터 중 태그를 이용해 값 구별해서 가져오기

                if (response.isSuccessful()) {  // HTTP 응답의 StatCode가 200~299인 경우 true 반환
                    String title, body = null;
                    List<RetroUser> retroUser = response.body();    // body()는 서버로부터 받은 응답 형식을 변수에 입력

                    for (RetroUser user : retroUser){
                        title = user.getEmail();
                        body = user.getId();
                        arrayList.add(new TrashListData(title, body));  // arrayList에 항목 추가
                        Log.i("category 리스트뷰 전달 성공", "title : " + title + ", body : "+ body);
                    }

                    trashAdapter.notifyDataSetChanged();    // listview 리스트의 크기+아이템 둘 다 변경될 때 사용 (=리스트 업데이트)

                    // 리스트뷰의 높이를 계산에서 layout 크기를 설정해줌
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
                else {
                    Log.e("category 리스트뷰", "리스트뷰 출력 실패");
                }
            }

            @Override
            public void onFailure(Call<List<RetroUser>> call, Throwable t) {
                Log.e("category 리스트뷰", "예외, 네트워크 오류 등");
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
