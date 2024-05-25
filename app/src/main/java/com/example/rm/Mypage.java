package com.example.rm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.token.ApiClient;
import com.example.rm.token.JWTUtils;
import com.example.rm.token.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Mypage extends AppCompatActivity implements View.OnClickListener {
    // 레이아웃
    LinearLayout btnLogout, btnDelete, btnCommunity, btnUserInfo;
    ImageView btnBack;
    TextView userId, userEmail;

    //
    private static final String tag = "마이페이지";
    TokenManager tokenManager;
    private static final String USER_INFO_URL = "http://ipark4.duckdns.org:58395/api/read/users/";  // Flask 서버의 유저 정보 URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);
        userId = findViewById(R.id.user_id);
        userEmail = findViewById(R.id.user_email);
        btnBack = findViewById(R.id.btn_back);
        btnLogout = findViewById(R.id.btn_logout);
        btnDelete = findViewById(R.id.li_delete);
        btnCommunity = findViewById(R.id.li_community);
        btnUserInfo = findViewById(R.id.li_user);

        btnBack.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCommunity.setOnClickListener(this);
        btnUserInfo.setOnClickListener(this);
        tokenManager = new TokenManager(this);

        fetchUserInfo();
    }

    // 마이페이지 상단에 닉네임 표시 + 토큰 만료되면 자동 로그아웃
    private void fetchUserInfo() {
        new Thread(() -> {
            OkHttpClient client = ApiClient.getClient(Mypage.this, tokenManager);

            try {
                JSONObject decodedToken = JWTUtils.decodeJWT(tokenManager.getToken().replace("Bearer ", ""));
                String userIdFromToken = decodedToken.getString("id");

                Request request = new Request.Builder()
                        .url(USER_INFO_URL + userIdFromToken.trim() + "/info")
                        .addHeader("Authorization", tokenManager.getToken())
                        .addHeader("Device-Info", Build.MODEL)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        JSONObject json = new JSONObject(responseBody).getJSONObject("message");
                        final String userIdStr = json.getString("id");
                        final String userEmailStr = json.getString("email");
                        final String nickname = json.getString("nickname");

                        runOnUiThread(() -> {
                            userId.setText(nickname);
                        });
                    } else {
                        runOnUiThread(() -> {
                            Log.e(tag, "사용자 정보 가져오기 실패");
                        });
                    }
                } catch (IOException | JSONException e) {
                    Log.e(tag, "네트워크 오류", e);
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    tokenManager.clearToken();
                    Toast.makeText(Mypage.this, "로그인 세션이 만료되어 로그아웃 처리 되었습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Mypage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog;
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_logout:   // 로그아웃
                builder.setTitle("로그아웃");
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("확인", (dialog, which) -> {
                    PreferenceHelper.logout();
                    tokenManager.clearToken();
                    Intent intent = new Intent(Mypage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(Mypage.this, "로그아웃 하였습니다.", Toast.LENGTH_LONG).show();
                });
                builder.setNeutralButton("취소", (dialog, which) -> dialog.dismiss());
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.li_delete:    // 탈퇴하기 페이지
                Intent intent = new Intent(Mypage.this, DeleteAccount.class);
                startActivity(intent);
                break;
            case R.id.li_community: // 내 게시글/댓글 수정 페이지
                intent = new Intent(Mypage.this, MypageModify.class);
                startActivity(intent);
                break;
            case R.id.li_user:  // 회원 정보 수정 페이지
                intent = new Intent(Mypage.this, MypageUserInfo.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}