package com.example.rm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    SqliteHelper sqliteHelper;
    String userName, userId, userPwd, userEmail;        // 회원 정보 저장

    ImageView btnBack;
    Button btnCheck, btnCheck2, btnSignUp;
    EditText signName, signId, signPwd, signEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        btnBack = findViewById(R.id.btn_back);
        btnCheck = findViewById(R.id.btn_check);
        btnCheck2 = findViewById(R.id.btn_check2);
        btnSignUp = findViewById(R.id.btn_sign_up);
        signName = findViewById(R.id.sign_name);
        signId = findViewById(R.id.sign_id);
        signPwd = findViewById(R.id.sign_pwd);
        signEmail = findViewById(R.id.sign_email);

        sqliteHelper = new SqliteHelper(this);

        // 뒤로 가기 버튼
        btnBack.setOnClickListener(v -> finish());

        // 닉네임 중복 검사
        btnCheck.setOnClickListener(v -> {
            userName = signName.getText().toString().trim();

            if (!sqliteHelper.checkNickname(userName)) {
                showDialog("닉네임", false);
            } else {
                showDialog("닉네임", true);
            }
        });

        // 아이디 중복 검사
        btnCheck2.setOnClickListener(v -> {
            userId = signId.getText().toString().trim();

            if (!sqliteHelper.checkId(userId)) {
                showDialog("아이디", false);
            }
            else {
                showDialog("아이디", true);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {       // 회원가입 버튼
            @Override
            public void onClick(View v) {

                userPwd = signPwd.getText().toString().trim();
                userEmail = signEmail.getText().toString().trim();
                Boolean insert = sqliteHelper.insertData(userId, userPwd, userEmail, userName);

                // 빈칸이 생기면
                if (userId.isEmpty() || userPwd.isEmpty() || userName.isEmpty() || userEmail.isEmpty()){
                    Toast.makeText(SignUp.this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                    Log.d("SignUp", "userId: " + userId + ", userPwd: " + userPwd + ", userName: " + userName + ", userEmail: " + userEmail);
                }
                else {
                    if (insert){
                        Toast.makeText(SignUp.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();       // 화면이 바로 넘어감
                        Log.d("SignUp", "userId: " + userId + ", userPwd: " + userPwd + ", userName: " + userName + ", userEmail: " + userEmail);
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "회원가입 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // 중복 확인 버튼 알림
    void showDialog(String message, boolean b){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (b){
            builder.setMessage("중복된 " + message + "입니다.");
        } else builder.setMessage("사용 가능한 " + message + "입니다.");

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }






}