package com.example.rm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchId extends AppCompatActivity {

    String inputUserEmail;

    ImageView btnBack;     // 뒤로가기
    EditText editEmail;    // 이메일 입력
    Button btnSearchId;   // 아이디 찾기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_id);

        btnBack = (ImageView) findViewById(R.id.btn_back);
        editEmail = (EditText)findViewById(R.id.edit_email);
        btnSearchId = (Button)findViewById(R.id.btn_search_id);

        inputUserEmail = editEmail.getText().toString();



        // 뒤로 가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchId.this, LoginUser.class));
                finish();
            }
        });


        // 아이디 찾기 버튼 클릭 시
        btnSearchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }


}