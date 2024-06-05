package com.example.rm.category;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;
import com.example.rm.retrofit.RetroClient;
import com.example.rm.retrofit.RetroWriting;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTrash extends AppCompatActivity {
    // 레이아웃
    RecyclerView recyclerView;
    ArrayList<TrashData> trashData = new ArrayList<>(); // 쓰레기 데이터 담음
    TrashAdapter trashAdapter;
    //
    static final String tag = "쓰레기 검색창";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchtrash);
        recyclerView = findViewById(R.id.search_recyclerview);

        // 툴바
        Toolbar toolbar = (Toolbar)findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // recyclerView
        setRecyclerView();
        getTrashData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     // 검색창 설정
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconifiedByDefault(false);    // 항상 확장된 상태O
        searchView.requestFocus();  // searchview에 포커스 요청+키보드 자동O

        searchView.post(() ->{
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {        // 검색창에 일어나는 이벤트 구현
            @Override
            public boolean onQueryTextSubmit(String str) {return false;}    // 키보드의 검색 버튼을 눌렀을 때 이벤트(=검색 업무 처리), 입력한 값을 str로 받아서 처리
            @Override
            public boolean onQueryTextChange(String text) {    // 검색어 입력할 때마다 실행됨
                if(text.isEmpty()){
                    trashAdapter.updateSearchView(new ArrayList<>());
                }
                else {filterTrash(text);}
                return true;
            }
        });
        return true;
    }

    // 검색창에 입력한 텍스트가 있으면 텍스트값과 비교해서 같은 데이터를 result에 추가
    private void filterTrash(String text){
        ArrayList<TrashData> result = new ArrayList<>();
        for(int i=0; i<trashData.size(); i++){
            TrashData data = trashData.get(i);
            if(data.getTrash_name().toLowerCase().contains(text.toLowerCase())){
                result.add(data);
            }
        }
        trashAdapter.updateSearchView(result);
    }

    // recyclerView 초기화
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchTrash.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        trashAdapter = new TrashAdapter(new ArrayList<>(), SearchTrash.this);
        recyclerView.setAdapter(trashAdapter);
    }

    // 리스트뷰에 출력할 데이터 서버에서 가져와 trashData에 저장 (검색 전에 이미 만들어져 있어야 하고, 데이터를 가져오기만 하므로 화면에 출력X)
    private void getTrashData() {
        trashData.add(new TrashData("Plastic Bottle", "Recycle as plastic", "hash001"));
        trashData.add(new TrashData("Glass Bottle", "Recycle as glass", "hash002"));
        trashData.add(new TrashData("Newspaper", "Recycle as paper", "hash003"));
        trashData.add(new TrashData("Aluminum Can", "Recycle as metal", "hash004"));
    }
}