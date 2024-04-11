package com.example.rm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchPwd extends AppCompatActivity {

    String inputUserId;

    ImageView btnBack;     // 뒤로가기
    EditText editId;    // 아이디 입력
    Button btnSearchPwd;   // 비밀번호 찾기 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_pwd);


        btnBack = (ImageView) findViewById(R.id.btn_back);
        editId = (EditText)findViewById(R.id.edit_id);
        btnSearchPwd = (Button)findViewById(R.id.btn_search_pwd);


        inputUserId = editId.getText().toString();


        // 뒤로 가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchPwd.this, LoginUser.class));
                finish();
            }
        });

        // 비밀번호 찾기 버튼 클릭 시
        btnSearchPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
