package com.example.rm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchPwd extends AppCompatActivity implements View.OnClickListener {

    ImageView btnBack;
    EditText editEmail;
    Button btnSearchPwd;

    private static final String TAG = "SearchPwd";
    private static final String SEARCH_PWD_URL = "http://ipark4.duckdns.org:58395/api/create/search/sendpassword";  // SendNewPassword.py의 엔드포인트로 변경하세요

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_pwd);

        btnBack = findViewById(R.id.btn_back);
        editEmail = findViewById(R.id.edit_email_pwd);
        btnSearchPwd = findViewById(R.id.btn_search_pwd);

        btnBack.setOnClickListener(this);
        btnSearchPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            finish();
        } else if (v.getId() == R.id.btn_search_pwd) {
            String email = editEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
            } else {
                searchPwdByEmail(email);
            }
        }
    }

    private void searchPwdByEmail(final String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("email", email);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, json.toString());
                Request request = new Request.Builder()
                        .url(SEARCH_PWD_URL)
                        .post(body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SearchPwd.this, "비밀번호가 이메일로 전송되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SearchPwd.this, LoginUser.class);  // 로그인 화면으로 이동
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("비번 찾기 실패", responseBody);
                                Toast.makeText(SearchPwd.this, "비번 찾기 실패: " + responseBody, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SearchPwd.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
