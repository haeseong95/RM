package com.example.rm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rm.token.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;


// 사용자 로그인 화면
public class LoginUser extends AppCompatActivity implements View.OnClickListener {

    SqliteHelper sqliteHelper;
    ImageView btnBack;  // 뒤로 가기 버튼
    String userId, userPw = null;
    EditText loginId, loginPwd;  // 아이디, 비밀번호 입력창
    Button btnLogin, searchId, searchPwd, signUp;  // 로그인, 아이디 찾기, 비밀번호 찾기, 회원가입 버튼

    private static final String tag = "LoginUser";
    private static final String LOGIN_URL = "http://ipark4.duckdns.org:58395/api/create/login";  // Flask 서버의 로그인 URL로 변경하세요
    private TokenManager tokenManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);

        btnBack = (ImageView) findViewById(R.id.btn_back);
        loginId = (EditText) findViewById(R.id.login_id);
        loginPwd = (EditText) findViewById(R.id.login_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        searchId = (Button) findViewById(R.id.search_id);
        searchPwd = (Button) findViewById(R.id.search_pwd);
        signUp = (Button) findViewById(R.id.sign_up);

        btnBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        searchId.setOnClickListener(this);
        searchPwd.setOnClickListener(this);
        signUp.setOnClickListener(this);
        sqliteHelper = new SqliteHelper(this);
        tokenManager = new TokenManager(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        if (v.getId() == R.id.btn_back) {
            intent = new Intent(LoginUser.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);    // 전환 애니메이션 비활성화 
        } else if (v.getId() == R.id.btn_login) {
            userId = loginId.getText().toString();  // 사용자가 입력한 아이디, 비번 텍스트값 받기
            userPw = loginPwd.getText().toString();

            // 아이디 또는 비번이 입력되지 않았을 때
            if (userId.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (userPw.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 아이디, 비번 검사
                loginUser(userId, userPw);
            }
        } else if (v.getId() == R.id.search_id) {
            intent = new Intent(LoginUser.this, SearchId.class);
            startActivity(intent);
        } else if (v.getId() == R.id.search_pwd) {
            intent = new Intent(LoginUser.this, SearchPwd.class);
            startActivity(intent);
        } else if (v.getId() == R.id.sign_up) {
            intent = new Intent(LoginUser.this, SignUp.class);
            startActivity(intent);
        }
    }

    private void loginUser(final String userId, final String userPw) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String deviceModel = Build.MODEL;  // 장치 모델 이름 가져오기
                OkHttpClient client = new OkHttpClient();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("id", userId);
                    json.put("passwd", userPw);
                    json.put("device_info", deviceModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, json.toString());
                Request request = new Request.Builder()
                        .url(LOGIN_URL)
                        .post(body)
                        .addHeader("Device-Info", deviceModel)  // 장치 모델 이름 헤더 추가
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        JSONObject responseJson = new JSONObject(responseBody);
                        String token = responseJson.getString("message");

                        tokenManager.saveToken(token);
                        // 로그인 성공 시 메인 페이지로 이동
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PreferenceHelper.setLoginState(LoginUser.this, true);   // 로그인 성공 시 true 값 저장 + 로그인 시 입력한 아이디 저장
                                Intent intent = new Intent(LoginUser.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(tag, "로그인 실패" + responseBody);
                                Toast.makeText(LoginUser.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(tag, "네트워크 오류", e);
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}