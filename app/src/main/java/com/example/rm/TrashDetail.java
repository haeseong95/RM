package com.example.rm;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        trashImage.setImageResource(Integer.parseInt(getIntent().getStringExtra("trashImage")));
        trashName.setText(getIntent().getStringExtra("trashName"));
        trashInfo.setText(getIntent().getStringExtra("trashInfo"));

    }

}
