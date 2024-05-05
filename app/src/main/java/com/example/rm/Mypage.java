package com.example.rm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class Mypage extends AppCompatActivity implements View.OnClickListener{

    LinearLayout btnLogout, btnDelete;
    ImageView btnBack;
    TextView userNickname, userLevel;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);


        userNickname = findViewById(R.id.user_nickname);
        userLevel = findViewById(R.id.user_level);
        btnBack = findViewById(R.id.btn_back);
        btnLogout = findViewById(R.id.btn_logout);
        btnDelete = findViewById(R.id.li_delete);

        btnBack.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        sqliteHelper = new SqliteHelper(this);

        // 로그인 정보 가져와서 메인페이지 화면에 표시하기
        String id = PreferenceHelper.getLoginId(Mypage.this);

        Map<String, String> userInfo = sqliteHelper.getUserInfo(id);
        String nickname = userInfo.get("nickname");
        String level = userInfo.get("level");

        userNickname.setText(nickname);
        userLevel.setText(level);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (v.getId()){
            case R.id.btn_back: finish(); break;    // 뒤로 가기
            case R.id.btn_logout:   // 로그아웃
                builder.setTitle("로그아웃");
                builder.setMessage("로그아웃 하시겠습니까?");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceHelper.logout();
                        Intent i = new Intent(Mypage.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.li_delete:    // 탈퇴하기 페이지
                Intent i = new Intent(Mypage.this, DeleteAccount.class);
                startActivity(i);
                break;
            default: throw new IllegalStateException("Unexpected value: " + v.getId());




        }
    }



}
