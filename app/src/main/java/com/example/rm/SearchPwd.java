package com.example.rm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class SearchPwd extends AppCompatActivity {

    String inputUserId, inputUserEmail = null;

    ImageView btnBack;     // 뒤로가기
    EditText editId, findPw;    // 아이디, 이메일 입력
    Button btnSearchPwd, goLogin;   // 비밀번호 찾기 버튼
    LinearLayout layout, layout2;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_pwd);


        btnBack = (ImageView) findViewById(R.id.btn_back);
        editId = (EditText)findViewById(R.id.se_id);
        findPw = (TextView)findViewById(R.id.findpw);
        btnSearchPwd = (Button)findViewById(R.id.btn_search_pwd);
        layout = findViewById(R.id.li_i);
        layout2 = findViewById(R.id.li_login);
        goLogin = findViewById(R.id.go_login);
        sqliteHelper = new SqliteHelper(SearchPwd.this);

        // 뒤로 가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchPwd.this, LoginUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // 비밀번호 찾기 버튼 클릭 시
        btnSearchPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchPwd.this);
                inputUserId = editId.getText().toString();

                Map<String, String> userInfo = sqliteHelper.getUserInfo(inputUserId);

                RamdomPw();

                String password = userInfo.get("password");
                Log.i("SearchPwd", "아이디 입력 후 가져온 비밀번호 : " + password);
                findPw.setText(password);

                editId.setVisibility(View.INVISIBLE);
                btnSearchPwd.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
            }
        });

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchPwd.this, LoginUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    // 기존 비번 삭제 후 임시 비밀번호 생성 후 제공
    public void RamdomPw(){

    }

}
