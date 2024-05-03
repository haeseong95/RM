package com.example.rm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class SearchId extends AppCompatActivity{

    String inputUserEmail = null;

    ImageView btnBack;     // 뒤로가기
    EditText editEmail;    // 이메일 입력
    Button btnSearchId, btnLogin, btnBtnSearchPw;   // 아이디 찾기 버튼
    SqliteHelper sqliteHelper;
    LinearLayout layout1, layout2;
    TextView findId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_id);

        btnBack = (ImageView) findViewById(R.id.btn_back);
        editEmail = (EditText)findViewById(R.id.edit_email);
        btnSearchId = (Button)findViewById(R.id.btn_search_id);
        layout1 = findViewById(R.id.li_io);
        layout2 = findViewById(R.id.btn_to);
        findId = findViewById(R.id.find_id);
        btnLogin = findViewById(R.id.start_login);
        btnBtnSearchPw = findViewById(R.id.search_pw);
        sqliteHelper = new SqliteHelper(SearchId.this);

        // 뒤로 가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 아이디 찾기 버튼 클릭 시
        btnSearchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchId.this);
                inputUserEmail = editEmail.getText().toString();

                // 이메일 형식 틀리면
                if(!Patterns.EMAIL_ADDRESS.matcher(inputUserEmail).matches()) {
                    Toast.makeText(SearchId.this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                // 이메일 형식 맞으면 아이디 제공
                else {
                    String id = sqliteHelper.checkEmail(inputUserEmail);
                    Log.i("SearchId", "이메일 입력 후 가져온 ID : " + id);

                    if(id == null){
                        builder.setTitle("아이디 찾기");
                        builder.setMessage("해당하는 이메일을 찾을 수 없습니다.");
                        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        editEmail.setVisibility(View.INVISIBLE);
                        btnSearchId.setVisibility(View.INVISIBLE);
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.VISIBLE);
                        findId.setText(id);
                    }
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBtnSearchPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchId.this, SearchPwd.class);
                startActivity(intent);
            }
        });
    }

}