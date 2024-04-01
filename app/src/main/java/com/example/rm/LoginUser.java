package com.example.rm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

// 사용자 로그인 화면
public class LoginUser extends AppCompatActivity {
    EditText login_id, login_pwd;       // 아이디, 비밀번호 텍스트창
    Button btn_login, btn_search_id, btn_search_pwd, btn_sign_up;  // 로그인, 아이디 찾기, 비밀번호 찾기, 회원가입 버튼
    ImageView btn_back;     // 뒤로 가기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        BtnOnClick btnOnClick = new BtnOnClick();

        btn_back = findViewById(R.id.btn_back);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_search_id = (Button)findViewById(R.id.btn_search_id);
        btn_search_pwd = (Button)findViewById(R.id.btn_search_pwd);
        btn_sign_up = (Button)findViewById(R.id.btn_sign_up);

        login_id = (EditText) findViewById(R.id.login_id);
        login_pwd = (EditText) findViewById(R.id.login_pwd);

        btn_back.setOnClickListener(btnOnClick);
        btn_login.setOnClickListener(btnOnClick);
        btn_search_id.setOnClickListener(btnOnClick);
        btn_search_pwd.setOnClickListener(btnOnClick);
        btn_sign_up.setOnClickListener(btnOnClick);

        // 뒤로 가기 버튼
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    class BtnOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_back){
                Intent i = new Intent(LoginUser.this, MainActivity.class);
                startActivity(i);
            }
            else if (v.getId() == R.id.btn_sign_up) {
                Intent i = new Intent(LoginUser.this, SignUp.class);
                startActivity(i);
            }


        }
    }

}