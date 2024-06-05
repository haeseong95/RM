package com.example.rm.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.R;

public class Manager extends AppCompatActivity implements View.OnClickListener{
    // 레이아웃
    LinearLayout liNotice, liCommunity, liTrashData;
    Button bntLogout;

    //
    private static final String tag = "관리자 페이지";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager);
        liNotice = findViewById(R.id.manager_notice);
        liCommunity = findViewById(R.id.manager_community);
        liTrashData = findViewById(R.id.manager_trash_data);
        bntLogout = findViewById(R.id.manager_logout);

        liNotice.setOnClickListener(this);
        liCommunity.setOnClickListener(this);
        liTrashData.setOnClickListener(this);
        bntLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.manager_notice:   // 공지사항 관리
                break;
            case R.id.manager_community:    // 게시글, 댓글 관리
                intent = new Intent(Manager.this, CCommunity.class);
                startActivity(intent);
                break;
            case R.id.manager_trash_data:   // 쓰레기 데이터 관리
                break;
            case R.id.manager_logout:   // 로그아웃
                break;
            default: throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
}
