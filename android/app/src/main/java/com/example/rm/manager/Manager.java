package com.example.rm.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.MainActivity;
import com.example.rm.R;
import com.example.rm.mypage.Mypage;
import com.example.rm.token.PreferenceHelper;
import com.example.rm.token.TokenManager;

public class Manager extends AppCompatActivity implements View.OnClickListener{
    // 레이아웃
    LinearLayout liNotice, liCommunity, liTrashData;
    Button bntLogout;
    TokenManager tokenManager;
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
        tokenManager = new TokenManager(Manager.this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.manager_notice:   // 공지사항
                intent = new Intent(Manager.this, NNotice.class);
                startActivity(intent);
                break;
            case R.id.manager_community:    // 게시글, 댓글 관리
                intent = new Intent(Manager.this, CCommunity.class);
                startActivity(intent);
                break;
            case R.id.manager_trash_data:   // 쓰레기 데이터 관리
                break;
            case R.id.manager_logout:   // 로그아웃
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog alertDialog;
                builder.setTitle("로그아웃");
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("확인", (dialog, which) -> {
                    PreferenceHelper.logout();
                    tokenManager.clearToken();
                    Intent i = new Intent(Manager.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    Toast.makeText(Manager.this, "로그아웃 하였습니다.", Toast.LENGTH_LONG).show();
                });
                builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
                break;
            default: throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
}
