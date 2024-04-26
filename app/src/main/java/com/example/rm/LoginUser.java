package com.example.rm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

// 사용자 로그인 화면
public class LoginUser extends AppCompatActivity {

    String userId, userPwd;
    ImageView btnBack;     // 뒤로 가기 버튼
    EditText loginId, loginPwd;       // 아이디, 비밀번호 입력창
    Button btnLogin, searchId, searchPwd, signUp;  // 로그인, 아이디 찾기, 비밀번호 찾기, 회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);

        BtnClick btnClick = new BtnClick();

        btnBack = (ImageView) findViewById(R.id.btn_back);
        loginId = (EditText) findViewById(R.id.login_id);
        loginPwd = (EditText) findViewById(R.id.login_pwd);
        btnLogin = (Button)findViewById(R.id.btn_login);
        searchId = (Button)findViewById(R.id.search_id);
        searchPwd = (Button)findViewById(R.id.search_pwd);
        signUp = (Button)findViewById(R.id.sign_up);

        btnBack.setOnClickListener(btnClick);
        btnLogin.setOnClickListener(btnClick);
        btnLogin.setOnClickListener(btnClick);
        searchId.setOnClickListener(btnClick);
        searchPwd.setOnClickListener(btnClick);
        signUp.setOnClickListener(btnClick);

    }

    class BtnClick implements View.OnClickListener{
        Intent intent;

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_back){finish();}
            else if (v.getId() == R.id.btn_login) {
                userId = loginId.getText().toString();        // 아이디, 비번 텍스트값 반환
                userPwd = loginPwd.getText().toString();
            }
            else if (v.getId() == R.id.search_id) {intent = new Intent(LoginUser.this, SearchId.class); startActivity(intent);}
            else if (v.getId() == R.id.search_pwd) {intent = new Intent(LoginUser.this, SearchPwd.class); startActivity(intent);}
            else if (v.getId() == R.id.sign_up) {intent = new Intent(LoginUser.this, SignUp.class); startActivity(intent);}
        }
    }

}