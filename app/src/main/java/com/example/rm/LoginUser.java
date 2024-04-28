package com.example.rm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

// 사용자 로그인 화면
public class LoginUser extends AppCompatActivity implements View.OnClickListener{

    SqliteHelper sqliteHelper;
    ImageView btnBack;     // 뒤로 가기 버튼
    EditText loginId, loginPwd;       // 아이디, 비밀번호 입력창
    Button btnLogin, searchId, searchPwd, signUp;  // 로그인, 아이디 찾기, 비밀번호 찾기, 회원가입 버튼



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);

        btnBack = (ImageView) findViewById(R.id.btn_back);
        loginId = (EditText) findViewById(R.id.login_id);
        loginPwd = (EditText) findViewById(R.id.login_pwd);
        btnLogin = (Button)findViewById(R.id.btn_login);
        searchId = (Button)findViewById(R.id.search_id);
        searchPwd = (Button)findViewById(R.id.search_pwd);
        signUp = (Button)findViewById(R.id.sign_up);

        btnBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        searchId.setOnClickListener(this);
        searchPwd.setOnClickListener(this);
        signUp.setOnClickListener(this);
        sqliteHelper = new SqliteHelper(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;

        if (v.getId() == R.id.btn_back){finish();}
        else if (v.getId() == R.id.btn_login) {
            String userId = loginId.getText().toString();        // 사용자가 입력한 아이디, 비번 텍스트값 받기
            String userPw = loginPwd.getText().toString();

            // 아이디 또는 비번이 입력되지 않았을 때
            if (userId.isEmpty()){
                Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (userPw.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
            // 아이디, 비번 검사
            else {
                if (sqliteHelper.checkId(userId)){
                    if (sqliteHelper.checkIdPassword(userId, userPw)){
                        Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(this, "잘못된 비밀번호입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "잘못된 아이디입니다.", Toast.LENGTH_SHORT).show();
                }
            }


        }
        else if (v.getId() == R.id.search_id) {intent = new Intent(LoginUser.this, SearchId.class); startActivity(intent);}
        else if (v.getId() == R.id.search_pwd) {intent = new Intent(LoginUser.this, SearchPwd.class); startActivity(intent);}
        else if (v.getId() == R.id.sign_up) {intent = new Intent(LoginUser.this, SignUp.class); startActivity(intent);}
    }



}