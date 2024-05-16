package com.example.rm.community;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rm.R;

import java.util.ArrayList;

public class Notice extends AppCompatActivity {
    static final String tag = "Notice";
    final static int PICK_IMAGE = 1;    // 갤러리에서 사진 선택

    // 레이아웃
    ImageView btnBack, testImage;
    Button delete, addImage;
    RecyclerView recyclerView;

    // 값
    ImageAdapter adapter;
    ArrayList<Uri> uriArrayList = new ArrayList<>();    // 이미지의 uri을 담음 -> arrayList에 담아지려면 list형태
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);

        addImage = findViewById(R.id.test);
        btnBack = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recyclerview);
        testImage = findViewById(R.id.images_test);
        delete = findViewById(R.id.btn_image_delete_test);
        delete.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));

        btnBack.setOnClickListener(v -> finish());          // 뒤로 가기 버튼 클릭 시
        addImage.setOnClickListener(v -> attachAlbum());    // 사진 첨부하기 버튼 클릭 시
        init();
    }


    // 사용자가 addimage 버튼을 클릭하면 갤러리에서 이미지 선택해 이미지의 URI를 얻는 코드
    private void init(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if(result.getResultCode() == RESULT_OK){    // 이미지 가져오기 성공O
                Intent intent = result.getData();

                if(intent.getData() == null){   // 어떤 이미지도 선택X 경우
                    Log.i("리사이클뷰 이미지", "이미지 0개 (이미지 선택 안함)");
                } 
                else {  // 이미지를 1개라도 선택했을 때
                    if(intent.getClipData() == null){    // 이미지 1개만 선택
                        Log.e(tag + " 이미지 1개 선택", String.valueOf(intent.getData()));
                        Uri uri = intent.getData();     // 이미지 URI 얻음
                        uriArrayList.add(uri);
                        updateRecyclerView();
                    } 
                    else {    // 이미지 여러 개 선택
                        ClipData clipData = intent.getClipData();
                        Log.e("clipdata 이미지 여러 개 선택", String.valueOf(clipData.getItemCount()));

                        if(clipData.getItemCount() > 5) {   // 선택한 이미지가 5개를 초과했을 때
                            Log.e(tag, "이미지 5개 넘음");
                            Toast.makeText(getApplicationContext(), "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                        } 
                        else {  // 이미지를 1~5개를 선택했을 때
                            Log.e(tag, "이미지 다중 선택");
                            for (int i=0; i<clipData.getItemCount(); i++){
                                Uri uri = clipData.getItemAt(i).getUri();   // 선택한 이미지들의 uri 가져옴
                                try {
                                    uriArrayList.add(uri);
                                } catch (Exception e){
                                    Log.e(tag, "이미지 선택 에러", e);
                                }
                            }
                            updateRecyclerView();
                        }
                    }
                }
            }
        });
    }

    private void updateRecyclerView(){
        if(adapter == null){
            adapter = new ImageAdapter(uriArrayList, getApplicationContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(Notice.this, LinearLayoutManager.HORIZONTAL, true));
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    // 앨범에서 이미지 가져오기
    private void attachAlbum(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);     // 사용자가 데이터(여기선 이미지) 1개 선택
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);     // 이미지 여러 개 가져옴
        activityResultLauncher.launch(intent);
    }

}
