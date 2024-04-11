package com.example.rm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SignUp extends AppCompatActivity {

    String userName, userId, userPwd, userEmail;        // 회원 정보 저장

    ImageView btnBack;
    Button btnCheck, btnCheck2, btnSignUp;
    EditText signName, signId, signPwd, signEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        btnBack = (ImageView)findViewById(R.id.btn_back);
        btnCheck = (Button)findViewById(R.id.btn_check);
        btnCheck2 = (Button)findViewById(R.id.btn_check2);
        btnSignUp = (Button)findViewById(R.id.btn_sign_up);
        signName = (EditText)findViewById(R.id.sign_name);
        signId = (EditText)findViewById(R.id.sign_id);
        signPwd = (EditText)findViewById(R.id.sign_pwd);
        signEmail = (EditText)findViewById(R.id.sign_email);

        userName = signName.getText().toString();      // 사용자 정보 텍스트값 저장
        userId = signId.getText().toString();
        userPwd = signPwd.getText().toString();
        userEmail = signEmail.getText().toString();


        // 뒤로 가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LoginUser.class));
                finish();
            }
        });

        // 중복 확인 버튼
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("닉네임");
            }
        });

        btnCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("아이디");
            }
        });




    }

    // 중복 확인 버튼 알림
    void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("사용 가능한 " + message + "입니다");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }






}