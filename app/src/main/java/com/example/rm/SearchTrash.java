package com.example.rm;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTrash extends AppCompatActivity {


    SearchView searchView;
    ListView listView;
    ArrayList<TrashListData> arrayList = new ArrayList<>();  // 리스트뷰에 넣을 데이터
    TrashAdapter trashAdapter;  // 어댑터


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchtrash);

        listView = findViewById(R.id.search_listview);

        // 1. 리스트뷰 설정먼저
        trashAdapter = new TrashAdapter(SearchTrash.this, arrayList);
        setRetrofit();
        listView.setAdapter(trashAdapter);
        listView.setClickable(true);


        // 2. 리스트뷰 목록 클릭하면 상세 페이지로 이동
        initSearchView();


    }

    private void initSearchView() {

        searchView = findViewById(R.id.searchview);
        searchView.setSubmitButtonEnabled(true);    // 검색 버튼 활성화
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {    // 검색창에 일어나는 이벤트 구현
            @Override
            public boolean onQueryTextSubmit(String query) {    // 검색 완료 시 실행됨 (false 검색 키보드 내림)
                return false;
            }   // 검색 버튼을 눌렀을 때

            @Override
            public boolean onQueryTextChange(String newText) {  // 검색어 입력할 때마다 실행됨

                ArrayList<RetroUser> retroUsers = new ArrayList<>();

                // 서버에서 값을 받아오면 user 객체가 받아서
                for(RetroUser user : retroUsers) {

                }





                /*
                for (int i=0; i<placeList.size(); i++){
                    ListData listData = placeList.get(i);

                    // 장소 데이터와 비교해서 사용자가 검색한 장소명이 있으면 filterPlace 리스트에 추가
                    if(listData.getName().toLowerCase().contains(newText.toLowerCase())){
                        filterPlace.add(listData);
                    }
                }


                ListViewAdapter adapter2 = new ListViewAdapter(getApplicationContext(), 0, filterPlace);
                listView.setAdapter(adapter2);


 */
                return false;


            }
        });
    }

    // 리스트뷰 설정 (검색 전에 이미 만들어져 있어야 함)
    private void setRetrofit() {

        Call<List<RetroUser>> call = RetroClient.getRetroService().getUserId(1);

        call.enqueue(new Callback<List<RetroUser>>() {
            @Override
            public void onResponse(Call<List<RetroUser>> call, Response<List<RetroUser>> response) {

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



}