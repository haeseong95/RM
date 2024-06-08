package com.example.rm.account;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.MainActivity;
import com.example.rm.R;
import com.example.rm.token.PreferenceHelper;
import com.example.rm.token.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginUser extends AppCompatActivity implements View.OnClickListener {
    ImageView btnBack;  // 뒤로 가기 버튼
    String userId, userPw = null;
    EditText loginId, loginPwd;  // 아이디, 비밀번호 입력창
    Button btnLogin, searchId, searchPwd, signUp;  // 로그인, 아이디 찾기, 비밀번호 찾기, 회원가입 버튼
    private static final String tag = "로그인";
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
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            userId = loginId.getText().toString().trim();  // 사용자가 입력한 아이디, 비번 텍스트값 받기
            userPw = loginPwd.getText().toString().trim();

            // 아이디 또는 비번이 입력되지 않았을 때
            if (userId.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (userPw.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(userId, userPw);  // 아이디, 비번 검사 -> 회원이 맞으면 메인화면으로 이동
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
                e.toString();
            }
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/create/login")
                    .post(body)
                    .addHeader("Device-Info", deviceModel)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    JSONObject responseJson = new JSONObject(responseBody);
                    String token = responseJson.getString("message");
                    tokenManager.saveToken(token);  // 토큰 저장
                    runOnUiThread(() -> {
                        PreferenceHelper.setLoginState(LoginUser.this, true, userId);   // 로그인 성공 시 true 값 저장 + 로그인 id 저장
                        Intent intent = new Intent(LoginUser.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    });
                } else {
                    runOnUiThread(() -> {
                        Log.e(tag, "로그인 실패" + responseBody);
                        Toast.makeText(LoginUser.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (IOException | JSONException e) {
                Log.e(tag, "네트워크 오류", e);
            }
        }).start();
    }
}
