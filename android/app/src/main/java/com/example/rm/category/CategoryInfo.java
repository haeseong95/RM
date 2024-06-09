package com.example.rm.category;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// 메인화면에서 쓰레기 종류 버튼 클릭 시 쓰레기 메인 설명+품목 보여줌
public class CategoryInfo extends AppCompatActivity {
    // 레이아웃
    TextView categoryTitle;
    ImageView btnBack;
    RecyclerView recyclerView;
    //
    private static final String tag = "카테고리 쓰레기 목록 화면";
    ArrayList<TrashData> trashData = new ArrayList<>(); // 쓰레기 목록 데이터 담음
    TrashAdapter trashAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_info);

        btnBack = (ImageView)findViewById(R.id.btn_back);
        categoryTitle = (TextView)findViewById(R.id.category_title);
        recyclerView = findViewById(R.id.trash_recyclerview);
        btnBack.setOnClickListener(v -> finish());
        categoryTitle.setText(getIntent().getStringExtra("category_trash_name"));      // 쓰레기 종류의 이름을 상단 툴바에 표시
        String hash = getIntent().getStringExtra("category_trash_hash");

        getTrashList(hash);
        setRecyclerView();
    }

    // 해당 쓰레기 종류의 쓰레기 리스트 출력
    private void getTrashList(String hash) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", "trash");
                jsonObject.put("whichWriting", hash);
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
                    JSONObject responseObject = new JSONObject(responseBody);
                    JSONArray jsonArray = responseObject.getJSONArray("message");  // json 결과를 배열로 저장
                    List<TrashData> allCategory = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonO = jsonArray.getJSONObject(i);
                        String title = jsonO.getString("title");   // 쓰레기 이름
                        String trash_hash = jsonO.getString("hash");   // 쓰레기 이름
                        allCategory.add(new TrashData(title, trash_hash));
                    }
                    runOnUiThread(() -> {
                        trashData.addAll(allCategory);
                        trashAdapter.notifyDataSetChanged();
                        Log.i(tag, "게시글 목록 보여줌" + responseBody);
                    });
                } else {
                    Log.e(tag, "서버 응답 오류: " + response.body().string());
                }
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    // recyclerView 초기화
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CategoryInfo.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        trashAdapter = new TrashAdapter(trashData, CategoryInfo.this);
        recyclerView.setAdapter(trashAdapter);
    }

}
