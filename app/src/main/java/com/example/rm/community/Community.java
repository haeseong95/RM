package com.example.rm.community;

import android.content.Intent;
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
    Button btnMorePost, test;     // 더보기 버튼

    //
    private static final String tag = "Community 게시판 메인";
    private static final String url = "https://ipark4.duckdns.org:58395";
    private static final int item_count = 10;
    private int currentItemCount = 0;
    ArrayList<CommunityData> arrayList = new ArrayList<>();     // 게시글 목록 데이터 저장
    CommunityAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);
        recyclerView = findViewById(R.id.main_recyclerview);
        test = findViewById(R.id.content_test);
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

        // 게시글 상세 페이지
        test.setOnClickListener(v -> {
            Intent intent = new Intent(Community.this, CommunityContent.class);
            intent.putExtra("postId", "post1");
            startActivity(intent);
        });

        // 게시글 목록
        getPostList();
        setRecyclerView();
        clickRecyclerViewItem();

        // 검색
        searchPost();
    }

    // 게시글 목록 가져오는 okhttp (초기 게시글 10개 가져옴)
    private void getPosts() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url + "")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String title, nickname, level, date = null;
                    String jsonList = response.body().toString();
                    JSONArray jsonArray = new JSONArray(jsonList);  // json 결과를 배열로 저장

                    for (int i = 0; i < item_count; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i); // 객체 1개씩 받음
                        title = jsonObject.getString("title");
                        nickname = jsonObject.getString("nickname");
                        level = jsonObject.getString("level");
                        date = jsonObject.getString("date");
                        arrayList.add(new CommunityData(nickname, level, date, title));
                        Log.i(tag, "게시글 정보 : " + arrayList.toString());
                    }
                    currentItemCount += item_count;
                    Log.i(tag, "현재 item 위치 : " + currentItemCount);
                    runOnUiThread(() -> {

                    });


                }
            } catch (Exception e){
                Log.e(tag, "json 연결 실패", e);
            }
        }).start();
    }

    // 초기 게시글 10개 데이터 목록, getPosts에서 얻은 arrayList값 중에서 다 출력하지 말고 10개만 출력
    private void getPostList(){
        List<CommunityData> newItem = new ArrayList<>();
        for (int i = 0; i < item_count; i++) {
            newItem.add(new CommunityData("닉네임 " + (i + 1), "등급 " + (i + 1), "2024-05-19", "제목 " + (i + 1)));
        }
        arrayList.addAll(newItem);
        currentItemCount = item_count;
        Log.i(tag, "현재 item 위치 : " + currentItemCount);
    }

    // 더보기 버튼 클릭하면 게시글 10개 추가됨
    private void getMorePosts(){
        ArrayList<CommunityData> newItem = new ArrayList<>();

        for(int i=0; i<item_count; i++){
            newItem.add(new CommunityData("닉네임 " + (currentItemCount + i + 1), "등급 " + (currentItemCount + i + 1), "2024-05-19", "제목 " + (currentItemCount + i + 1)));
        }
        arrayList.addAll(newItem);
        adapter.notifyItemRangeInserted(currentItemCount, newItem.size());
        currentItemCount += newItem.size();
        Log.i(tag, "더보기 눌렀을 때 item 위치 : " + currentItemCount);

    }

    // recyclerview 초기화
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Community.this);
        recyclerView.setLayoutManager(linearLayoutManager); // layoutManager 설정
        adapter = new CommunityAdapter(Community.this, arrayList, Community.this);
        recyclerView.setAdapter(adapter);
    }

    // 아이템 클릭하면 게시글 상세 페이지로 넘어감
    private void clickRecyclerViewItem(){
//        adapter.setOnItemClickListener(new CommunityAdapter.OnItemClickEventListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(Community.this, CommunityContent.class);
//                intent.putExtra("contentTitle", arrayList.get(position).getMain_title());   // 서버 연결 때는 게시글의 해시값을 보내야 함
//                intent.putExtra("contentNickname", arrayList.get(position).getMain_nickname());
//                intent.putExtra("contentPlace", arrayList.get(position).getMain_place());
//                intent.putExtra("contentDate", arrayList.get(position).getMain_date());
//                startActivity(intent);
//
//                CommunityData item = arrayList.get(position);
//                Log.i(tag, "현재 아이템 정보 " + arrayList.get(position));
//            }
//        });
    }

    // 인터페이스
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
                // 입력하기 전에 호출됨
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {   // editText에 변화가 있을 때
            }

            @Override
            public void afterTextChanged(Editable editable) {   // 입력이 끝났을 때
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
