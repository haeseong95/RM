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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.MainActivity;
import com.example.rm.R;
import com.example.rm.account.LoginUser;
import com.example.rm.mypage.Mypage;
import com.example.rm.token.TokenManager;
import com.google.gson.Gson;

import org.apache.commons.fileupload.util.Streams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.commons.fileupload.MultipartStream;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class Community extends AppCompatActivity {
    // 레이아웃
    ImageView btnBack;
    EditText searchBar;     // 검색창
    LinearLayout btnWrite;  // 글쓰기 아이콘
    RecyclerView recyclerView;  // 게시글 목록
    //
    private static final String tag = "Community 게시판 메인";
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
        btnBack.setOnClickListener(v -> finish());

        // 연필 아이콘 클릭 -> 글쓰기 화면
        btnWrite.setOnClickListener(v -> {
            Intent intent = new Intent(Community.this, CommunityEdit.class);
            activityResultLauncher.launch(intent);
        });

        // 게시글 목록
        getPosts();
        setRecyclerView();

        // 검색
        searchPost();
    }

    // setResult로 전송한 결과를 받는 코드
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    if (result.getData().getBooleanExtra("post_create_success", false)) {
                        getPosts();
                        Log.i(tag, "게시글 업데이트");
                    }
                }
            });

    // 게시글 목록 가져옴
    private void getPosts() {
        OkHttpClient client = new OkHttpClient();
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "post");
            jsonObject.put("whichWriting", "");
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                tokenManager.clearToken();
                Toast.makeText(Community.this, "로그인 세션이 만료되어 로그아웃 처리 되었습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Community.this, LoginUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }

        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("http://ipark4.duckdns.org:58395/api/read/writing/list")
                .post(requestBody)
                .addHeader("Authorization", tokenManager.getToken())
                .addHeader("Device-Info", Build.MODEL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(tag, "서버 연결 실패", e);
                runOnUiThread(() -> Toast.makeText(Community.this, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject responseObject = new JSONObject(responseBody);
                        JSONArray jsonArray = responseObject.getJSONArray("message");  // json 결과를 배열로 저장
                        List<CommunityData> allPosts = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonO = jsonArray.getJSONObject(i);
                            String title = jsonO.getString("title");   // 게시글 이름
                            String author = jsonO.getString("author"); // 아이디
                            String nickname = jsonO.getString("nickname"); // 닉네임
                            String createTime = jsonO.getString("createTime"); // 생성날짜
                            String date = createTime.split("T")[0];
                            String postHash = jsonO.getString("hash"); // 해시값
                            allPosts.add(new CommunityData(nickname, date, title, postHash, author));
                        }
                        runOnUiThread(() -> {
                            arrayList.addAll(allPosts);
                            adapter.notifyDataSetChanged();
                            Log.i(tag, "게시글 목록 보여줌" + responseBody);
                        });
                    } catch (JSONException e) {
                        Log.e(tag, "JSON 파싱 오류", e);
                    }
                } else {
                    Log.e(tag, "서버 응답 오류: " + response.message());
                }
            }
        });
    }

    // recyclerview 초기화
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Community.this);
        recyclerView.setLayoutManager(linearLayoutManager); // layoutManager 설정
        adapter = new CommunityAdapter(Community.this, arrayList);
        recyclerView.setAdapter(adapter);
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

