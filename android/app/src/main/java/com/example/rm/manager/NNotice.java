package com.example.rm.manager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;
import com.example.rm.account.LoginUser;
import com.example.rm.community.CommunityEdit;
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

public class NNotice extends AppCompatActivity {
    ImageView btnBack, btnWrite;
    RecyclerView recyclerView;
    NNoticeAdapter nNoticeAdapter;
    ArrayList<NNoticeList> noticeList = new ArrayList<>();
    static final String tag = "관리자 공지사항 메인 화면";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_notice);
        btnBack = findViewById(R.id.btn_back);
        btnWrite = findViewById(R.id.manager_notice_write);
        recyclerView = findViewById(R.id.manager_notice_recyclerview);
        btnBack.setOnClickListener(v -> {finish();});
        btnWrite.setOnClickListener(v -> {
            Intent intent = new Intent(NNotice.this, NNoticeEdit.class);
            startActivity(intent);
        });

        // 공지사항 목록
        test();
//        getNotice();
        setNoticeRecycler();
    }

    private void test(){
        List<NNoticeList> noticeList = new ArrayList<>();
        noticeList.add(new NNoticeList("공지사항 1", "공지사항 내용 1", "2023-06-01", "hash1"));
        noticeList.add(new NNoticeList("공지사항 2", "공지사항 내용 2", "2023-06-02", "hash2"));
        noticeList.add(new NNoticeList("공지사항 3", "공지사항 내용 3", "2023-06-03", "hash3"));
        noticeList.add(new NNoticeList("공지사항 4", "공지사항 내용 4", "2023-06-04", "hash4"));
        noticeList.add(new NNoticeList("공지사항 5", "공지사항 내용 5", "2023-06-05", "hash5"));
    }

    // 공지사항 목록 가져옴
    private void getNotice() {
        OkHttpClient client = new OkHttpClient();
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "notice");
            jsonObject.put("whichWriting", "");
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                tokenManager.clearToken();
                Toast.makeText(NNotice.this, "로그인 세션이 만료되어 로그아웃 처리 되었습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NNotice.this, LoginUser.class);
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
                runOnUiThread(() -> Toast.makeText(NNotice.this, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject responseObject = new JSONObject(responseBody);
                        JSONArray jsonArray = responseObject.getJSONArray("message");
                        List<NNoticeList> allNotice = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonO = jsonArray.getJSONObject(i);
                            String title = jsonO.getString("title");   // 공지사항 제목
                            String content = jsonO.getString("contentText");    // 공지사항 내용
                            String createTime = jsonO.getString("createTime"); // 생성날짜
                            String date = createTime.split("T")[0];
                            String postHash = jsonO.getString("hash"); // 해시값
                            allNotice.add(new NNoticeList(title, content,date, postHash));
                        }
                        runOnUiThread(() -> {
                            noticeList.addAll(allNotice);
                            nNoticeAdapter.notifyDataSetChanged();
                            Log.i(tag, "게시글 목록 보여줌" + responseBody);
                        });
                    } catch (JSONException e) {
                        Log.e(tag, "JSON 파싱 오류", e);
                    }
                } else {
                    Log.e(tag, "서버 응답 오류: " + response.message());
                    runOnUiThread(() -> Toast.makeText(NNotice.this, "서버 응답 오류", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    // recyclerView 초기화
    private void setNoticeRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NNotice.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        nNoticeAdapter = new NNoticeAdapter(noticeList, NNotice.this);
        recyclerView.setAdapter(nNoticeAdapter);
    }
}





