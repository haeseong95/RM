package com.example.rm.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rm.MainActivity;
import com.example.rm.token.PreferenceHelper;
import com.example.rm.R;
import com.example.rm.token.SqliteHelper;
import com.example.rm.token.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


// 사용자 로그인 화면
public class LoginUser extends AppCompatActivity implements View.OnClickListener {
    // 레이아웃
    ImageView btnBack;  // 뒤로 가기 버튼
    String userId, userPw = null;
    EditText loginId, loginPwd;  // 아이디, 비밀번호 입력창
    Button btnLogin, searchId, searchPwd, signUp;  // 로그인, 아이디 찾기, 비밀번호 찾기, 회원가입 버튼

    //
    private static final String tag = "LoginUser";
    SqliteHelper sqliteHelper;
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

    // 버튼 클릭 시 화면 이동
    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.btn_back) {
            intent = new Intent(LoginUser.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_login) {
            userId = loginId.getText().toString();  // 사용자가 입력한 아이디, 비번 텍스트값 받기
            userPw = loginPwd.getText().toString();

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
        // 백그라운드 스레드 시작
        new Thread(new Runnable() {
            // 백그라운드 스레드에서 실행할 코드 정의
            @Override
            public void run() {
                // json 데이터를 포함해서 요청을 보내는 코드
                String deviceModel = Build.MODEL;  // 장치 모델 이름 가져오기
                OkHttpClient client = new OkHttpClient();   // http 클라이언트 생성, 요청을 보내고 응답함

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");    // json 미디어타입 설정, parse()는 문자열을 특정 형식으로 변환함 (-> 문자열을 mediatype 객체인 json으로 설정)
                JSONObject json = new JSONObject();     // json 객체 생성
                try {   // json객체에 데이터 추가 + 예외 처리
                    json.put("id", userId); // json 객체에 key-value 쌍 추가, 서버에 요청할 데이터를 json 형식으로 준비하기 위함 (id 키에 userId값 저장)
                    json.put("passwd", userPw);
                    json.put("device_info", deviceModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, json.toString());   // json데이터를 요청 body로 생성, 요청 body에는 서버에 보낼 데이터 작성
                Request request = new Request.Builder() // http 요청 생성, 작성한 요청 body와 데이터를 보낼 url을 request에 붙임
                        .url(LOGIN_URL)     // 서버에서 데이터를 가져올 api url
                        .post(body)     // POST 메소드 요청, 본문에 body 설정
                        .addHeader("Device-Info", deviceModel)  // 요청 헤더(장치 모델 이름) 추가
                        .build();   // request 객체 생성

                // 응답을 처리하는 코드
                try {
                    Response response = client.newCall(request).execute();  // 요청을 실행하고 응답 받기
                    String responseBody = response.body().string(); //응답 본문을 문자열로 변환, request.body를 쓰면 반환형이 toString()으로는 받을 수 없는 값이라 string() 써야함
                    if (response.isSuccessful()) {
                        JSONObject responseJson = new JSONObject(responseBody); // 응답 body를 json객체로 변환함
                        String token = responseJson.getString("message");   // 응답 json객체에서 message 필드를 추출하여 토큰으로 저장
                        tokenManager.saveToken(token);  // 토큰 저장
                        // 메인 ui 스레드에서 실행될 코드
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PreferenceHelper.setLoginState(LoginUser.this, true, userId);   // 로그인 성공 시 true 값 저장 + 로그인 id 저장
                                Intent intent = new Intent(LoginUser.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    } else {
                        // 메인 ui 스레드에서 실행될 코드
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(tag, "로그인 실패" + responseBody);
                                Toast.makeText(LoginUser.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.e(tag, "네트워크 오류", e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);  // 런타임 예외 발생
                }
            }
        }).start(); // 새로운 스레드 시작
    }
}