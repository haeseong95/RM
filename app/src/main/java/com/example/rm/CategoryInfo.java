package com.example.rm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 메인화면에서 쓰레기 종류 버튼 클릭 시 쓰레기 메인 설명+품목 보여줌
public class CategoryInfo extends AppCompatActivity {
    TextView categoryTitle;
    ImageView btnBack;
    ListView itemListView, mainListview;

    // 쓰레기 종류 목록
    ArrayList<TrashListData> arrayList = new ArrayList<>();;
    TrashListAdapter trashAdapter;

    // 메인 설명
    ArrayList<TrashMainListData> mainArrayList = new ArrayList<>();
    TrashMainAdapter trashMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_info);

        btnBack = (ImageView)findViewById(R.id.btn_back);
        categoryTitle = (TextView)findViewById(R.id.category_title);
        itemListView = (ListView)findViewById(R.id.category_listview);  // 쓰레기 품목
        mainListview = findViewById(R.id.category_main_listview);      // 메인 설명
        btnBack.setOnClickListener(v -> finish());
        categoryTitle.setText(getIntent().getStringExtra("category"));      // 쓰레기 종류의 이름을 상단 툴바에 표시

        // 메인 설명
        trashMainAdapter = new TrashMainAdapter(this, mainArrayList);
        getMain();
        mainListview.setAdapter(trashMainAdapter);
        
        // 쓰레기 목록
        trashAdapter = new TrashListAdapter(this, arrayList);
        getList();
        itemListView.setAdapter(trashAdapter);
        itemListView.setClickable(true);
        clickListviewItem();
    }

    // retrofit, 쓰레기 메인 설명을 리스트뷰에 추가 
    private void getMain(){
        
    }


    // retrofit, 쓰레기 품목 종류를 요청한 값을 리스트뷰에 추가
    private void getList() {

    }
    

    /*
    private void setRetroTrashItem() {

        String tag = getIntent().getStringExtra("category");    // 쓰레기 종류 구분해 줄 값
        Call<RetroApi> call = RetroClient.getRetroService().setCategoryItem(tag);  //RetroService()로 인터페이스 구현체 생성

        call.enqueue(new Callback<RetroApi>() {
            @Override
            public void onResponse(Call<RetroApi> call, Response<RetroApi> response) {
                ArrayList<RetroWriting> retroWritings = response.body().getRetroWriting();  // Writing 객체 리스트 반환
                String name, info;


                if (response.isSuccessful()){
                    for (RetroWriting writing : retroWritings) {

                    }

                }
            }

            @Override
            public void onFailure(Call<RetroApi> call, Throwable t) {
                Log.e("네트워크 연결 실패", "망", t);
            }
        });



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
    */

    // listview 목록 클릭하면 쓰레기 상세 페이지로 데이터 전달
    public void clickListviewItem() {
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long I) {
                Intent intent = new Intent(CategoryInfo.this, TrashDetail.class);
                intent.putExtra("trashName", arrayList.get(position).trashListInfo);
               //intent.putExtra("trashImage", Integer.toString(arrayList.get(position).trashListInfo));
                intent.putExtra("trashInfo", arrayList.get(position).trashListInfo);
                startActivity(intent);
            }
        });
    }
}
