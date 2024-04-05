package com.example.rm;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchId extends AppCompatActivity {

    ImageView btn_back;     // 뒤로가기
    EditText edit_email;    // 이메일 입력
    Button btn_search_id;   // 아이디 찾기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_id);

        btn_back = (ImageView) findViewById(R.id.btn_back);
        edit_email = (EditText)findViewById(R.id.edit_email);
        btn_search_id = (Button)findViewById(R.id.btn_search_id);

        // 버튼
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_search_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}