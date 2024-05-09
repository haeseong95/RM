package com.example.rm;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTrash extends AppCompatActivity {

    ListView listView;

    ArrayList<TrashMainListData> arrayList = new ArrayList<>();  // 리스트뷰에 넣을 데이터
    ArrayList<TrashMainListData> searchList = new ArrayList<>();    // 검색 결과 저장할 리스트
    TrashAdapter2 trashAdapter;  // 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchtrash);
        listView = findViewById(R.id.search_listview);

        Toolbar toolbar = (Toolbar)findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 1. 리스트뷰 설정먼저
        trashAdapter = new TrashAdapter2(SearchTrash.this, arrayList);
        setRetrofit();
        listView.setAdapter(trashAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.expandActionView();



        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("검색어를 입력하세요 ");
        searchView.setIconifiedByDefault(false);    // 항상 확장된 상태
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocus();  // searchview에 포커스 요청+키보드 자동
        searchView.post(() ->{
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);  //show_inplicit 하던가 아님 아예 0으로 처리
            Log.i("searchview 실행", "검색창 자동 실행 성공");
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {        // 검색창에 일어나는 이벤트 구현
            @Override
            public boolean onQueryTextSubmit(String s) {    // 검색 버튼을 눌렀을 때
                return false;
            }
            @Override
            public boolean onQueryTextChange(String text) {    // 검색어 입력/변경할 때마다 실행됨

                ArrayList<TrashMainListData> result = new ArrayList<>();

                for(int i=0; i<searchList.size(); i++) {
                    TrashMainListData trashMainListData = searchList.get(i);

                    if(trashMainListData.getMainName().toLowerCase().contains(text.toLowerCase())) {
                        result.add(trashMainListData);
                    }
                }
                TrashAdapter2 trashAdapter2 = new TrashAdapter2(getApplicationContext(), result);
                listView.setAdapter(trashAdapter2);
                return false;
            }
        });

        return true;
    }

    // 리스트뷰 설정 (검색 전에 이미 만들어져 있어야 함)
    private void setRetrofit() {
        Call<List<RetroUser>> call = RetroClient.getRetroService().getUserId(1);

        call.enqueue(new Callback<List<RetroUser>>() {
            @Override
            public void onResponse(Call<List<RetroUser>> call, Response<List<RetroUser>> response) {
                if (response.isSuccessful()) {
                    int id = 0;
                    String title, url = null;
                    List<RetroUser> retroUser = response.body();

                    for (int i=0; i<7; i++){
                        RetroUser user = retroUser.get(i);
                        title = user.getTitle();
                        id = user.getId();
                        url = user.getUrl();
                        arrayList.add(new TrashMainListData(url, title));
                        Log.i("SearchTrash.listview 성공O", "title : " + title + ", body : "+ url);
                    }
                    trashAdapter.notifyDataSetChanged();

                    for(int i=0; i<arrayList.size(); i++) {
                        searchList.add(arrayList.get(i));
                    }
                }
                else {Log.e("SearchTrash.listview 출력X", "");}
            }
            @Override
            public void onFailure(Call<List<RetroUser>> call, Throwable t) {Log.e("SearchTrash 네트워크 오류", "");}
        });
    }
}