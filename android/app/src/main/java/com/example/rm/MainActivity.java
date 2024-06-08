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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.account.LoginUser;
import com.example.rm.category.CategoryInfo;
import com.example.rm.category.SearchTrash;
import com.example.rm.community.Community;
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
    LinearLayout btnCan, btnGlass, btnPaper, btnPlastic, btnBattery, btnPlasticBag, btnSofa, btnRes;      // 카테고리 버튼
    Button mainMap, mainCommunity, mainUserinfo;        // 툴바 아이콘
    RecyclerView recyclerView;
    private static final String tag = "메인화면";
    TokenManager tokenManager;
    NoticeAdapter adapter;
    ArrayList<NoticeData> arrayList = new ArrayList<>();    // 공지사항 데이터 담음
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSearch = (LinearLayout)findViewById(R.id.li_search);
        btnCamera = (LinearLayout)findViewById(R.id.li_camera);
        btnCan = (LinearLayout) findViewById(R.id.btn_can);
        btnGlass = (LinearLayout) findViewById(R.id.btn_glass);
        btnPaper = (LinearLayout) findViewById(R.id.btn_paper);
        btnPlastic = (LinearLayout) findViewById(R.id.btn_plastic);
        btnBattery = findViewById(R.id.btn_battery);
        btnPlasticBag = findViewById(R.id.btn_plastic_bag);
        btnSofa = findViewById(R.id.btn_sofa);
        btnRes = findViewById(R.id.btn_res);
        mainMap = (Button)findViewById(R.id.main_map);
        mainCommunity = (Button)findViewById(R.id.main_community);
        mainUserinfo = (Button)findViewById(R.id.main_userinfo);
        recyclerView = findViewById(R.id.main_recyclerView);
        btnSearch.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnCan.setOnClickListener(this);
        btnGlass.setOnClickListener(this);
        btnPaper.setOnClickListener(this);
        btnPlastic.setOnClickListener(this);
        btnBattery.setOnClickListener(this);
        btnPlasticBag.setOnClickListener(this);
        btnSofa.setOnClickListener(this);
        btnRes.setOnClickListener(this);
        mainMap.setOnClickListener(this);
        mainCommunity.setOnClickListener(this);
        mainUserinfo.setOnClickListener(this);
        PreferenceHelper.init(this);
        tokenManager = new TokenManager(this);
//        getNotice();
//        setRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PreferenceHelper.getLoginState()){mainUserinfo.setText("마이");}
        else {mainUserinfo.setText("로그인");}
    }

    // 각 버튼의 쓰레기 종류명을 CategoryInfo의 툴바에 출력하기 위해 데이터 전달
    private String categoryText(int id){
        switch (id){
            case R.id.btn_can: return "고철류";
            case R.id.btn_glass: return "유리병";
            case R.id.btn_paper: return "종이류";
            case R.id.btn_plastic: return "플라스틱류";
            case R.id.btn_sofa: return "대형폐기물";
            case R.id.btn_plastic_bag: return "비닐류";
            case R.id.btn_battery: return "생활유혜폐기물";
            case R.id.btn_res: return "폐가전제품";
            default: return " ";
        }
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
            case R.id.btn_can:
            case R.id.btn_glass:
            case R.id.btn_paper:
            case R.id.btn_plastic:
            case R.id.btn_sofa:
            case R.id.btn_plastic_bag:
            case R.id.btn_battery:
            case R.id.btn_res:
                intent = new Intent(MainActivity.this, CategoryInfo.class);
                intent.putExtra("category", categoryText(v.getId()));
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
    
    // 공지사항 데이터 가져옴
    private void getNotice() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            TokenManager tokenManager = new TokenManager(getApplicationContext());
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
                    .addHeader("Authorization", tokenManager.getToken())
                    .addHeader("Device-Info", Build.MODEL)
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
                        String contentText = jsonO.getString("contentText"); // 내용
                        String noticeHash = jsonO.getString("hash"); // 해시값
                        String createTime = jsonO.getString("createTime"); // 생성날짜
                        String date = createTime.split("T")[0];
                        allNotice.add(new NoticeData(title, contentText, date, noticeHash));
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
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NoticeAdapter(arrayList, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }
}