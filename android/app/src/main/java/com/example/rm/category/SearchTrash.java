package com.example.rm.category;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchTrash extends AppCompatActivity {
    TextView categoryTag;
    RecyclerView recyclerView;
    ArrayList<TrashData> trashData = new ArrayList<>(); // 쓰레기 데이터 담음
    SearchAdapter searchAdapter;


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
                    searchAdapter.updateSearchView(new ArrayList<>());
                }
                else {
                    getTrashDetail(text);
                }
                return true;
            }
        });
        return true;
    }

    private void getTrashDetail(String text) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("titlePart", text);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/read/writing/info/search")
                    .post(requestBody)
                    .addHeader("Device-Info", Build.MODEL)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    JSONObject responseObject = new JSONObject(responseBody);
                    JSONArray json = responseObject.getJSONArray("message");  // json 결과를 배열로 저장
                    ArrayList<SearchData> result = new ArrayList<>();

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonO = json.getJSONObject(i);
                        String title = jsonO.getString("title");
                        String trash_hash = jsonO.getString("hash");
                        String trash_tag = jsonO.getString("category");
                        result.add(new SearchData(title, trash_hash, trash_tag));
                    }
                    runOnUiThread(() -> {
                        searchAdapter.updateSearchView(result);
                        Log.i(tag, "쓰레기 검색 결과 출력됨 : " + responseBody);
                    });
                } else {
                    Log.e(tag, "쓰레기 검색 결과 망함 : " + responseBody);
                }
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    // 태그의 색깔 변경
    private void setColor(TextView textView, String colorCode) {
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(colorCode));
        textView.setBackgroundTintList(colorStateList);
    }

    // recyclerView 초기화
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchTrash.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        searchAdapter = new SearchAdapter(new ArrayList<>(), SearchTrash.this);
        recyclerView.setAdapter(searchAdapter);
    }
}