package com.example.rm.category;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;
import com.example.rm.retrofit.RetroClient;
import com.example.rm.retrofit.RetroWriting;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 메인화면에서 쓰레기 종류 버튼 클릭 시 쓰레기 메인 설명+품목 보여줌
public class CategoryInfo extends AppCompatActivity {
    TextView categoryTitle;
    ImageView btnBack;
    ListView itemListView, mainListview;
    RecyclerView recyclerView;

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

        // recyclerview

    }

    //albumId값에 따라 카테고리 버튼 누르면 분류되게함
    private int categoryToNumber(String sort) {
        int result;

        switch (sort) {
            case "고철류": result = 1; break;
            case "유리병": result = 6; break;
            case "종이류": result = 8; break;
            case "플라스틱류": result = 9; break;
            default: result = -1;
        }

        Log.i("카테고리 분류 값", "리턴값: " + result);
        return result;
    }

    // retrofit, 쓰레기 메인 설명
    private void getMain(){
        String tag = getIntent().getStringExtra("category");    // 쓰레기 종류 구분해 줄 값
        Call<List<RetroWriting>> call = RetroClient.getRetroService().getCategoryMain(categoryToNumber(tag));
        call.enqueue(new Callback<List<RetroWriting>>() {
            @Override
            public void onResponse(Call<List<RetroWriting>> call, Response<List<RetroWriting>> response) {
                if (response.isSuccessful()) {
                    String title, url, thum= null;
                    List<RetroWriting> retroWritings = response.body();

                    for (int i=0; i<3; i++){
                        RetroWriting writing = retroWritings.get(i);
                        title = writing.getTitle();
                        url = writing.getUrl();
                        thum = writing.getThumbnailUrl();
                        mainArrayList.add(new TrashMainListData(url, title, thum));
                        Log.i("CateogoryInfo 메인 설명O", "title : " + mainArrayList.get(i).getTrashMainName() + ", url : " + url);
                    }
                    trashMainAdapter.notifyDataSetChanged();
                    setListviewHeight(mainListview, trashMainAdapter);
                }
                else {Log.e("CateogoryInfo.메인 설명 출력X", "");}
            }

            @Override
            public void onFailure(Call<List<RetroWriting>> call, Throwable t) {
                Log.e("categoryInfo 네트워크 오류", "", t);
            }
        });
    }

    // recyclerView 초기화
    private void initRecyclerView(){

    }


    // retrofit, 쓰레기 품목 종류
    private void getList() {
        String tag = getIntent().getStringExtra("category");
        Call<List<RetroWriting>> call = RetroClient.getRetroService().getCategoryList(categoryToNumber(tag));
        call.enqueue(new Callback<List<RetroWriting>>() {
            @Override
            public void onResponse(Call<List<RetroWriting>> call, Response<List<RetroWriting>> response) {
                if (response.isSuccessful()) {
                    String title, url, thum = null;
                    List<RetroWriting> retroWritings = response.body();

                    for (int i=0; i<10; i++){
                        RetroWriting writing = retroWritings.get(i);
                        title = writing.getTitle();
                        url = writing.getUrl();
                        thum = writing.getThumbnailUrl();
                        arrayList.add(new TrashListData(url, title, thum));
                        Log.i("CateogoryInfo 품목 리스트O", "title : " + arrayList.get(i).getTrashListName() + ", url : "+ url + "thum : " + thum);
                    }
                    trashAdapter.notifyDataSetChanged();
                    setListviewHeight(itemListView, trashAdapter);
                }
                else {Log.e("CateogoryInfo. 품목 리스트X", "");}
            }

            @Override
            public void onFailure(Call<List<RetroWriting>> call, Throwable t) {
                Log.e("categoryInfo 네트워크 오류", "", t);
            }
        });
    }

    // listview 목록 클릭하면 쓰레기 상세 페이지로 데이터 전달
    public void clickListviewItem() {
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long I) {
                Intent intent = new Intent(CategoryInfo.this, TrashDetail.class);
                intent.putExtra("trashName", arrayList.get(position).getTrashListName());
                intent.putExtra("trashImage", arrayList.get(position).getTrashListImage());
                intent.putExtra("trashInfo", arrayList.get(position).getTrashListInfo());
                startActivity(intent);
            }
        });
    }

    // listview의 높이 계산
    private void setListviewHeight(ListView listView, ListAdapter adapter){
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++){
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
