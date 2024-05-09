package com.example.rm;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

// 갤러리에서 이미지 가져오기
public class RetroImage extends AppCompatActivity {

    Button button;
    ImageView imageView;

    private static final int REQUEST_CODE = 0;  // 갤러리에서 사진 요청 코드

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retro_camera);

        button = findViewById(R.id.camera_button);
        imageView = findViewById(R.id.camera_image);

        // 버튼 클릭 시 인텐트로 갤러리에 요청 코드 보내기
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });




    }


    // button 클릭 시 보낸 요청 코드를 onactivityresult가 응답 받음 -> uri 객체를 통해 이미지 데이터를 전달받고 glide로 imageview에 입력함
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    Glide.with(getApplicationContext()).load(uri).into(imageView);  // 다이얼로그 이미지 사진에 넣기
                } catch (Exception e){

                }
            } else if (resultCode == RESULT_CANCELED) { // 취소 시 호출할 행동 쓰기

            }
        }

    }




}
