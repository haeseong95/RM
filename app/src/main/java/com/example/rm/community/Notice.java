package com.example.rm.community;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.rm.R;

import java.util.ArrayList;

public class Notice extends AppCompatActivity {

    ImageView btnBack, testImage;
    Button delete, addImage;
    ListView listView;
    
    CommunityImageAdapter communityImageAdapter;
    ArrayList<ImageData> imageData = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);

        listView = findViewById(R.id.list_test);
        addImage = findViewById(R.id.test);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        testImage = findViewById(R.id.images_test);
        delete = findViewById(R.id.btn_image_delete_test);
        delete.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));

        communityImageAdapter = new CommunityImageAdapter(Notice.this, R.layout.community_listview_image, imageData);
        listView.setAdapter(communityImageAdapter);




        // 사용자가 addimage 버튼을 클릭하면 갤러리에서 이미지 선택해 이미지의 URI를 얻는 코드
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();

                    try {
                        Uri uri = intent.getData();     // 이미지 URI 얻음
                        Glide.with(Notice.this).load(uri).into(testImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                
                // 갤러리 호출
                Intent intent = new Intent(Intent.ACTION_PICK);     // 사용자가 데이터(여기선 이미지)를 1개 선택하도록 인텐트 제공
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                // setdatatype으로 데이터의 URL + MIME 타입 설정 -> Media~_URI: URI는 외부 저장소의 이미지 데이터를 가리킴, image/*는 MIME 타입으로 모든 이미지 타입을 나타냄
                intent.setAction(Intent.ACTION_PICK);
                activityResultLauncher.launch(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testImage.setImageResource(0);
            }
        });
    }
}
