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

import java.security.SecureRandom;
import java.util.Map;

public class SearchPwd extends AppCompatActivity {

    String inputUserId = null;
    TextView tempPw;
    ImageView btnBack;     // 뒤로가기
    EditText editId;    // 아이디, 이메일 입력
    Button btnSearchPwd, goLogin;   // 비밀번호 찾기 버튼
    LinearLayout layout, layout2;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_pwd);


        btnBack = (ImageView) findViewById(R.id.btn_back);
        editId = (EditText)findViewById(R.id.se_id);
        tempPw = (TextView)findViewById(R.id.temp_pw);
        btnSearchPwd = (Button)findViewById(R.id.btn_search_pwd);
        layout = findViewById(R.id.li_i);
        layout2 = findViewById(R.id.li_login);
        goLogin = findViewById(R.id.go_login);
        sqliteHelper = new SqliteHelper(SearchPwd.this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchPwd.this, LoginUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // 비밀번호 찾기 버튼 클릭
        btnSearchPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SearchPwd.this);
                inputUserId = editId.getText().toString();

                // 입력한 아이디가 존재X
                if (!sqliteHelper.checkId(inputUserId)) {
                    builder.setTitle("비밀번호 찾기");
                    builder.setMessage("해당하는 아이디를 찾을 수 없습니다.");
                    builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                // 아이디의 기존 비번 삭제 후 임시 비번 저장
                else {
                    String getTempPw = getTempPassword();       // 임시 비번 지정
                    tempPw.setText(getTempPw);
                    Log.i("SearchPwd", "사용자에게 제공되는 임시 비번 : " + getTempPw);

                    String hashpwd = SignUp.getSHA(getTempPw);
                    Log.i("SearchPwd", "임시 비번 암호화 된 해시값 : " + hashpwd);
                    sqliteHelper.updatePassword(inputUserId, hashpwd);        // 임시 비번 저장

                    tempPw.setText(getTempPw);
                    editId.setVisibility(View.INVISIBLE);
                    btnSearchPwd.setVisibility(View.INVISIBLE);
                    layout.setVisibility(View.VISIBLE);
                    layout2.setVisibility(View.VISIBLE);
                }
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

    // 임시 비밀번호 생성
    public static String getTempPassword(){
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(str.length());
            sb.append(str.charAt(index));
        }
        return sb.toString();
    }



}
