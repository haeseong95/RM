package com.example.rm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Community extends AppCompatActivity {

    ImageView btnBack;
    EditText searchBar;
    LinearLayout btnWrite;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnWrite = findViewById(R.id.c_write);
        searchBar = findViewById(R.id.c_search);
        listView = findViewById(R.id.com_listview);
        btnBack.setOnClickListener(v -> finish());

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community.this, CommunityPostEditor.class);
                startActivity(intent);
            }
        });

        // edittext로 설정한 검색창을 누르면 키보드 올라옴 -> 입력값이랑 같은 닉네임/게시글 제목을 입력하면 listview에 뜨게
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }



}
