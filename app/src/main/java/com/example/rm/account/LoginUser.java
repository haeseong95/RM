package com.example.rm.account;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.MainActivity;
import com.example.rm.R;
import com.example.rm.token.PreferenceHelper;
import com.example.rm.token.SqliteHelper;
import com.example.rm.token.TokenManager;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginUser extends AppCompatActivity implements View.OnClickListener {
    // 레이아웃
    ImageView btnBack;  // 뒤로 가기 버튼
    String userId, userPw = null;
    EditText loginId, loginPwd;  // 아이디, 비밀번호 입력창
    Button btnLogin, searchId, searchPwd, signUp;  // 로그인, 아이디 찾기, 비밀번호 찾기, 회원가입 버튼

    private static final String tag = "LoginUser";
    SqliteHelper sqliteHelper;
    private static final String LOGIN_URL = "http://ipark4.duckdns.org:58395/api/create/login";  // Flask 서버의 로그인 URL로 변경하세요
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);

        btnBack = findViewById(R.id.btn_back);
        loginId = findViewById(R.id.login_id);
        loginPwd = findViewById(R.id.login_pwd);
        btnLogin = findViewById(R.id.btn_login);
        searchId = findViewById(R.id.search_id);
        searchPwd = findViewById(R.id.search_pwd);
        signUp = findViewById(R.id.sign_up);

        btnBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        searchId.setOnClickListener(this);
        searchPwd.setOnClickListener(this);
        signUp.setOnClickListener(this);

        sqliteHelper = new SqliteHelper(this);
        tokenManager = new TokenManager(this);
    }

    // 버튼 클릭 시 화면 이동
    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.btn_back) {
            intent = new Intent(LoginUser.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_login) {
            userId = loginId.getText().toString();
            userPw = loginPwd.getText().toString();

            if (userId.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (userPw.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
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
        new Thread(() -> {
            String deviceModel = Build.MODEL;
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

            RequestBody body = RequestBody.create(json.toString(), JSON);
            String token = tokenManager.getToken();  // 기존 토큰 가져오기
            Request.Builder requestBuilder = new Request.Builder()
                    .url(LOGIN_URL)
                    .post(body)
                    .addHeader("Device-Info", deviceModel);

            if (token != null && !token.isEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer " + token);
            }

            Request request = requestBuilder.build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Log.e(tag, "로그인 요청 실패: " + e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject responseJson = new JSONObject(responseBody);
                                String message = responseJson.getString("message");
                                if (responseJson.has("token")) {
                                    String newToken = responseJson.getString("token");
                                    tokenManager.saveToken(newToken);
                                }
                                PreferenceHelper.setLoginState(LoginUser.this, true, userId);
                                Intent intent = new Intent(LoginUser.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e(tag, "로그인 실패: " + responseBody);
                            Toast.makeText(LoginUser.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }).start();
    }
}
