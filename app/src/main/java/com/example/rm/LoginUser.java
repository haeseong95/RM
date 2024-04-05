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

    ImageView btn_back;     // 뒤로 가기 버튼
    EditText login_id, login_pwd;       // 아이디, 비밀번호 입력창
    Button btn_login, search_id, search_pwd, sign_up;  // 로그인, 아이디 찾기, 비밀번호 찾기, 회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);

        BtnOnClick btnOnClick = new BtnOnClick();

        btn_back = (ImageView) findViewById(R.id.btn_back);
        login_id = (EditText) findViewById(R.id.login_id);
        login_pwd = (EditText) findViewById(R.id.login_pwd);
        btn_login = (Button)findViewById(R.id.btn_login);
        search_id = (Button)findViewById(R.id.search_id);
        search_pwd = (Button)findViewById(R.id.search_pwd);
        sign_up = (Button)findViewById(R.id.sign_up);

        btn_back.setOnClickListener(btnOnClick);
        btn_login.setOnClickListener(btnOnClick);
        search_id.setOnClickListener(btnOnClick);
        search_pwd.setOnClickListener(btnOnClick);
        sign_up.setOnClickListener(btnOnClick);

    }

    class BtnOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_back){
                Intent i = new Intent(LoginUser.this, MainActivity.class);
                startActivity(i);
            }
            else if (v.getId() == R.id.btn_login) {
                Intent i = new Intent(LoginUser.this, MainActivity.class);
                startActivity(i);
            }
            else if (v.getId() == R.id.search_id) {
                Intent i = new Intent(LoginUser.this, SearchId.class);
                startActivity(i);
            }
            else if (v.getId() == R.id.search_pwd) {
                Intent i = new Intent(LoginUser.this, SearchPwd.class);
                startActivity(i);
            }
            else if (v.getId() == R.id.sign_up) {
                Intent i = new Intent(LoginUser.this, SignUp.class);
                startActivity(i);
            }

        }
    }

}