package com.example.rm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.account.LoginUser;
import com.example.rm.category.CategoryInfo;
import com.example.rm.category.SearchTrash;
import com.example.rm.community.Community;
import com.example.rm.manager.Manager;
import com.example.rm.mypage.Mypage;
import com.example.rm.token.PreferenceHelper;
import com.example.rm.token.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout btnSearch, btnCamera;      // 검색창, 카메라
    Button mainMap, mainCommunity, mainUserinfo;        // 툴바 아이콘
    RecyclerView recyclerView, categoryView;
    TextView manage;
    private static final String tag = "메인화면";
    TokenManager tokenManager;
    NoticeAdapter adapter;
    ArrayList<NoticeData> arrayList = new ArrayList<>();    // 공지사항 데이터 담음
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryData> categoryData = new ArrayList<>();   // 카테고리 쓰레기 데이터
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSearch = (LinearLayout)findViewById(R.id.li_search);
        btnCamera = (LinearLayout)findViewById(R.id.li_camera);
        mainMap = (Button)findViewById(R.id.main_map);
        mainCommunity = (Button)findViewById(R.id.main_community);
        mainUserinfo = (Button)findViewById(R.id.main_userinfo);
        recyclerView = findViewById(R.id.main_recyclerView);
        categoryView = findViewById(R.id.category_recyclerView);
        manage = findViewById(R.id.btnManage);
        btnSearch.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        mainMap.setOnClickListener(this);
        mainCommunity.setOnClickListener(this);
        mainUserinfo.setOnClickListener(this);
        PreferenceHelper.init(this);
        tokenManager = new TokenManager(this);
        manage.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Manager.class);
            startActivity(intent);
        });

        // 공지사항
        getNotice();
        setNoticeView();

        // 카테고리 버튼
        getCategory();
        setCategoryView();
    }

    // 카테고리 버튼
    private void getCategory(){
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", "category");
                jsonObject.put("whichWriting", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/read/writing/list")
                    .post(requestBody)
                    .addHeader("Device-Info", Build.MODEL)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    JSONArray jsonArray = new JSONObject(responseBody).getJSONArray("message");
                    List<CategoryData> allCategory = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonO = jsonArray.getJSONObject(i);
                        String title = jsonO.getString("title");   // 카테고리 이름
                        String noticeHash = jsonO.getString("hash"); // 해시값
                        allCategory.add(new CategoryData(title, noticeHash));
                    }
                    runOnUiThread(() -> {
                        categoryData.addAll(allCategory);
                        categoryAdapter.notifyDataSetChanged();
                        Log.i(tag, "공지사항 목록 보여줌" + responseBody);
                    });
                } else {
                    Log.e(tag, "서버 응답 오류: " + responseBody);
                }
            } catch (IOException | JSONException e) {
                Log.e(tag, "네트워크 오류", e);
            }
        }).start();
    }
    private void setCategoryView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        categoryView.setLayoutManager(linearLayoutManager);
        categoryAdapter = new CategoryAdapter(categoryData, MainActivity.this);
        categoryView.setAdapter(categoryAdapter);
    }

    // 공지사항 데이터 가져옴
    private void getNotice() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", "notice");
                jsonObject.put("whichWriting", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/read/writing/list")
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    JSONArray jsonArray = new JSONObject(responseBody).getJSONArray("message");
                    List<NoticeData> allNotice = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonO = jsonArray.getJSONObject(i);
                        String title = jsonO.getString("title");   // 공지사항 이름
                        String noticeHash = jsonO.getString("hash"); // 해시값
                        String createTime = jsonO.getString("createTime"); // 생성날짜
                        String date = createTime.split("T")[0];
                        allNotice.add(new NoticeData(title, date, noticeHash));
                    }
                    runOnUiThread(() -> {
                        arrayList.addAll(allNotice);
                        adapter.notifyDataSetChanged();
                        Log.i(tag, "공지사항 목록 보여줌" + responseBody);
                    });
                } else {
                    Log.e(tag, "서버 응답 오류: " + responseBody);
                }
            } catch (IOException | JSONException e) {
                Log.e(tag, "네트워크 오류", e);
            }
        }).start();
    }
    private void setNoticeView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NoticeAdapter(arrayList, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    // 버튼 클릭 시 액티비티 이동
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.li_search: intent = new Intent(MainActivity.this, SearchTrash.class); break;
            case R.id.li_camera:
                intent = new Intent(MainActivity.this, Camera.class);
                break;
            case R.id.main_map: intent = new Intent(MainActivity.this, RecycleLocation.class); break;
            case R.id.main_community:
                String token = tokenManager.getToken();
                if(token != null && !token.isEmpty()){
                    intent = new Intent(MainActivity.this, Community.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                return;
            case R.id.main_userinfo:
                boolean login = PreferenceHelper.getLoginState();
                Log.d("로그인/마이페이지 버튼 클릭 시", "마이페이지(true) / 로그인(false) : " + login);
                if(login) {intent = new Intent(MainActivity.this, Mypage.class);}
                else {intent = new Intent(MainActivity.this, LoginUser.class);}
                break;
            default: throw new IllegalStateException("Unexpected value: " + v.getId());
        }
        startActivity(intent);
    }

    // 로그인/마이 텍스트 변경
    @Override
    protected void onResume() {
        super.onResume();
        if(PreferenceHelper.getLoginState()){mainUserinfo.setText("마이");}
        else {mainUserinfo.setText("로그인");}

        String currentUserId = PreferenceHelper.getLoginId(MainActivity.this);    // 로그인 시 사용된 아이디
        if(currentUserId.equals("admin")){
            manage.setVisibility(View.VISIBLE);
        } else {
            manage.setVisibility(View.GONE);
        }
    }
}