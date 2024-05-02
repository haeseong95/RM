package com.example.rm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class Mypage extends AppCompatActivity implements View.OnClickListener{

    Button btnLogout;
    ImageView btnBack;
    TextView userNickname, userLevel;
    SqliteHelper sqliteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        btnBack = findViewById(R.id.btn_back);
        btnLogout = findViewById(R.id.btn_logout);
        userNickname = findViewById(R.id.user_nickname);
        userLevel = findViewById(R.id.user_level);

        btnBack.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        sqliteHelper = new SqliteHelper(this);

        displayLoginInfo();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back: finish(); break;
            case R.id.btn_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            default: throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    // 로그인 정보 가져와서 메인페이지 화면에 표시하기
    private void displayLoginInfo(){
        String id = PreferenceHelper.getLoginId(Mypage.this);

        Map<String, String> userInfo = sqliteHelper.getUserInfo(id);
        String nickname = userInfo.get("nickname");
        String level = userInfo.get("level");

        userNickname.setText(nickname);
        userLevel.setText(level);
    }


}
