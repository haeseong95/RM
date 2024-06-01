package com.example.rm;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rm.R;

import java.util.ArrayList;

public class Notice extends AppCompatActivity {
    // 레이아웃
    ImageView btnBack;
    TextView textView;

    //
    static final String tag = "공지사항 상세 페이지";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);

        textView = findViewById(R.id.notice_cotent);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

    }

    // 서버에서 해시값을 이용해 해당 공지사항의 내용 가져오기
    private void getNotice(){
        String noticeHash = getIntent().getStringExtra("notice_hash");
    }

}