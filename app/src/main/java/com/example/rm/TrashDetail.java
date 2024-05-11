package com.example.rm;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class TrashDetail extends AppCompatActivity {

    ImageView btnBack, trashImage;
    TextView trashName, trashInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trash_detail);

        btnBack = (ImageView)findViewById(R.id.btn_back);
        trashImage = findViewById(R.id.trash_image);
        trashName = findViewById(R.id.title_trashName);
        trashInfo = findViewById(R.id.trash_info);
        btnBack.setOnClickListener(v -> finish());

        getTrashData();     // 리스트뷰에서 정보 가져온 거 화면에 보여줌

    }


    public void getTrashData(){
        String imageUrl = getIntent().getStringExtra("trashImage"); // 이미지 URL 가져오기
        Glide.with(this)
                .load(imageUrl)
                .into(trashImage); // 로드할 ImageView 지정
        trashName.setText(getIntent().getStringExtra("trashName"));
    }
}
