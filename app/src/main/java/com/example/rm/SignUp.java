package com.example.rm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SignUp extends AppCompatActivity {

    ImageView btn_back;
    Button btn_check, btn_check2, btn_sign_up;
    EditText sign_name, sign_id, sign_pwd, sign_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        btn_back = (ImageView)findViewById(R.id.btn_back);
        btn_check = (Button)findViewById(R.id.btn_check);
        btn_check2 = (Button)findViewById(R.id.btn_check2);
        btn_sign_up = (Button)findViewById(R.id.btn_sign_up);
        sign_name = (EditText)findViewById(R.id.sign_name);
        sign_id = (EditText)findViewById(R.id.sign_id);
        sign_pwd = (EditText)findViewById(R.id.sign_pwd);
        sign_email = (EditText)findViewById(R.id.sign_email);

        // 뒤로 가기 버튼
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 중복 확인 버튼
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("닉네임");
            }
        });

        btn_check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("아이디");
            }
        });



    }

    // 중복 확인 버튼 알림
    void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("사용 가능한 " + message + "입니다");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }






}