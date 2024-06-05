package com.example.rm.community;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;
import com.example.rm.token.TokenManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Community extends AppCompatActivity implements CommunityAdapter.OnItemClickEventListener {
    // 레이아웃
    ImageView btnBack;
    EditText searchBar;     // 검색창
    LinearLayout btnWrite;  // 글쓰기 아이콘
    RecyclerView recyclerView;  // 게시글 목록
    Button btnMorePost;     // 더보기 버튼

    //
    private static final String tag = "Community 게시판 메인";
    private static final int item_count = 10;
    private int currentPage = 1;
    ArrayList<CommunityData> arrayList = new ArrayList<>();     // 게시글 목록 데이터 저장
    CommunityAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);
        recyclerView = findViewById(R.id.main_recyclerview);
        btnBack = findViewById(R.id.btn_back);
        btnWrite = findViewById(R.id.c_write);
        searchBar = findViewById(R.id.c_search);
        btnMorePost = findViewById(R.id.btn_morepost);
        btnBack.setOnClickListener(v -> finish());
        btnMorePost.setOnClickListener(v -> getMorePosts());

        // 연필 아이콘 클릭 -> 글쓰기 화면
        btnWrite.setOnClickListener(v -> {
            Intent intent = new Intent(Community.this, CommunityEdit.class);
            startActivity(intent);
        });

        // 게시글 목록
        getPostList();
        setRecyclerView();

        // 검색
        searchPost();
    }

    // 게시글 목록 가져오는 okhttp (초기 게시글 10개 가져옴, 게시글을 작성한 아이디, 해시값을 저장)
    private void getPosts(int page) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            TokenManager tokenManager = new TokenManager(getApplicationContext());
            String deviceModel = Build.MODEL;

            Request request = new Request.Builder()
                    .url("https://ipark4.duckdns.org:58395/api/read/writing/list/" + "?page=" + page + "&items=" + item_count)
                    .addHeader("Authorization", tokenManager.getToken())
                    .addHeader("Device-Info", deviceModel)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    String jsonList = response.body().string();
                    JSONObject responseObject = new JSONObject(jsonList);
                    JSONArray jsonArray = responseObject.getJSONArray("message");  // json 결과를 배열로 저장
                    List<CommunityData> allPosts = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");   // 게시글 이름
                        String author = jsonObject.getString("author"); // 아이디
                        String nickname = jsonObject.getString("nickname"); // 닉네임
                        String createTime = jsonObject.getString("createTime"); // 생성날짜
                        String postHash = jsonObject.getString("hash"); // 해시값
//                        allPosts.add(new CommunityData(nickname, author, createTime, title, postHash, author));
                    }

                    runOnUiThread(() -> {
                        arrayList.addAll(allPosts);
                        adapter.notifyDataSetChanged();
                        currentPage++;
                    });
                } else {
                    Log.e(tag, "서버 응답 오류: " + responseBody);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    // 초기 게시글 10개 데이터 목록 가져오기
    private void getPostList() {
        getPosts(currentPage);
    }

    // 더보기 버튼 클릭하면 게시글 10개 추가됨
    private void getMorePosts() {
        getPosts(currentPage);
    }

    // recyclerview 초기화
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Community.this);
        recyclerView.setLayoutManager(linearLayoutManager); // layoutManager 설정
        adapter = new CommunityAdapter(Community.this, arrayList, Community.this);
        recyclerView.setAdapter(adapter);
    }

    // 아이템 클릭하면 게시글 상세 페이지로 넘어감
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(Community.this, CommunityContent.class);
        intent.putExtra("contentTitle", arrayList.get(position).getMain_title());   // 서버 연결 때는 게시글의 해시값을 보내야 함
        intent.putExtra("contentNickname", arrayList.get(position).getMain_nickname());
        intent.putExtra("contentPlace", arrayList.get(position).getMain_place());
        intent.putExtra("contentDate", arrayList.get(position).getMain_date());
        startActivity(intent);
        Log.i(tag, "현재 게시글 position 값 : " + position);
    }


    // 게시글 제목을 입력하면 recyclerview가 업데이트 됨
    private void searchPost(){
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 텍스트 변경/입력 전에 바로 호출됨 (편집 전 텍스트의 상태 저장)
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 텍스트가 변경될 때 호출 (사용자가 입력하는 동안 실시간으로 UI 업데이트 수행, 입력 값 검증 작업 수행)
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 텍스트가 변경된 후에 호출 (입력 완료 후 최종적으로 데이터 처리/특정 조건에 따라 추가적인 UI 업데이트 수행)
                ArrayList<CommunityData> search_list = new ArrayList<>(); // 검색 결과를 담을 리스트
                String searchText = searchBar.getText().toString(); // 검색창에 입력한 검색어
                search_list.clear();

                if(searchText.equals("")){
                    adapter.updateRecyclerViewItem(arrayList);
                }
                else {
                    for(int a=0; a<arrayList.size(); a++){
                        if(arrayList.get(a).getMain_title().toLowerCase().contains(searchText.toLowerCase())){
                            search_list.add(arrayList.get(a));
                        }
                    }
                    adapter.updateRecyclerViewItem(search_list);
                }
            }
        });
    }

}
