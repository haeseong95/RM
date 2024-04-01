package com.example.rm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{
    Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     // 액티비티 화면 초기화

        BtnOnClick btnOnClick = new BtnOnClick();

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(btnOnClick);

    }

    class BtnOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_login){
                Intent i1 = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i1);
            }
        }
    }
}