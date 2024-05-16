package com.example.rm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUp extends AppCompatActivity {

    SqliteHelper sqliteHelper;
    String userName, userId, userPwd, userEmail = null;        // 회원 정보 저장

    ImageView btnBack;
    Button btnNicknameCheck, btnIdCheck, btnSignUp;
    EditText signName, signId, signPwd, signPwdDoubleCheck, signEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        btnBack = findViewById(R.id.btn_back);
        btnNicknameCheck = findViewById(R.id.nicknameCheckButton);


        btnIdCheck = findViewById(R.id.idCheckButton);
        btnSignUp = findViewById(R.id.btn_sign_up);
        signName = findViewById(R.id.sign_name);
        signId = findViewById(R.id.sign_id);
        signPwd = findViewById(R.id.sign_pwd);
        signPwdDoubleCheck = findViewById(R.id.sign_pwdDoubleCheck);
        signEmail = findViewById(R.id.sign_email);

        sqliteHelper = new SqliteHelper(this);

        // 뒤로 가기 버튼
        btnBack.setOnClickListener(v -> finish());

        // 닉네임 중복 검사
        btnNicknameCheck.setOnClickListener(v -> {
            userName = signName.getText().toString().trim();

            if (!sqliteHelper.findAccount(userName, "nickname")) {
                showDialog("닉네임", false);
            } else {
                showDialog("닉네임", true);
            }
        });

        // 아이디 중복 검사
        btnIdCheck.setOnClickListener(v -> {
            userId = signId.getText().toString().trim();

            if (!sqliteHelper.findAccount(userId, "id")) {
                showDialog("아이디", false);
            }
            else {
                showDialog("아이디", true);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {       // 회원가입 버튼

            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
            AlertDialog alertDialog;

            @Override
            public void onClick(View v) {
                userId = signId.getText().toString().trim();
                userPwd = signPwd.getText().toString().trim();
                userEmail = signEmail.getText().toString().trim();
                userName = signName.getText().toString().trim();

                if (userId.isEmpty() || userPwd.isEmpty() || userName.isEmpty() || userEmail.isEmpty()) {
                    showMessage("모든 정보를 입력해주세요.");
                    Log.w("SignUp : 정보 입력 덜 함", "userId: " + userId + ", userPwd: " + userPwd + ", userName: " + userName + ", userEmail: " + userEmail);
                }
                else {
                    if(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                        String hashPwd = getSHA(userPwd);    // sha로 암호화된 비밀번호
                        Boolean insert = sqliteHelper.insertData(userId, hashPwd, userEmail, userName);

                        if (insert){
                            builder.setMessage("회원가입에 성공하였습니다.");
                            builder.setPositiveButton("확인", (dialog, which) -> finish());
                            alertDialog = builder.create();
                            alertDialog.show();
                            Log.i("SignUp : 회원가입 성공", "아이디: " + userId + ", 닉네임: " + userName + ", 이메일: " + userEmail);
                        } else {
                            showMessage("회원가입에 실패하였습니다.");
                            Log.e("SignUp : 회원가입 실패", "아이디: " + userId + ", 닉네임: " + userName + ", 이메일: " + userEmail);
                        }
                    }
                    else {showMessage("이메일 형식이 올바르지 않습니다.");}
                }
            }

            private void showMessage(String message){
                builder.setMessage(message);
                builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    // 중복 확인 버튼 알림
    void showDialog(String message, boolean b){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (b){
            builder.setMessage("중복된 " + message + "입니다.");
        } else builder.setMessage("사용 가능한 " + message + "입니다.");

        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // 비밀번호 -> 해시값 저장
    public static String getSHA(String password) {
        try {
            StringBuilder stringBuilder = new StringBuilder();      // 해시값을 16진수의 문자열로 변환
            MessageDigest md = MessageDigest.getInstance("SHA-256");    // SHA-256 알고리즘 사용

            byte[] encodehash = md.digest(password.getBytes(StandardCharsets.UTF_8));       // 해싱에 사용할 비밀번호 -> byte로 변환 후 해시 함수에 추가해 해시 계산함 (utf-8로 문자열 -> byte 변환), digest 호출하면 reset() 필요없이 자동 리셋되므로 필요없음
            for (int i = 0; i<encodehash.length; i++){
                String hex = Integer.toHexString(0xff & encodehash[i]);     // 각 바이트 -> 16진수 문자열로 변환

                if(hex.length() == 1) {stringBuilder.append('0');}  // 문자열 조립 시 hex가 단일 문자(1)일 때 0 저장
                stringBuilder.append(hex);
            }
            return stringBuilder.toString();

        } catch (NoSuchAlgorithmException e) {throw new RuntimeException(e);}
    }



}