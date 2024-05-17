package com.example.rm.community;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import java.util.ArrayList;

// 커뮤니티 글쓰기 화면
public class CommunityEdit extends AppCompatActivity {
    // 레이아웃
    ImageView btnBack;
    RecyclerView recyclerView;
    EditText postTitle, postContent;
    TextView btnCreate, btnDelete, btnAddImage, textImageCount;

    // 값
    static final String tag = "CommunityEdit";
    ImageAdapter adapter;
    ArrayList<Uri> uriArrayList = new ArrayList<>();    // 이미지의 uri 담음
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_edit);
        recyclerView = findViewById(R.id.recyclerView);
        btnBack = findViewById(R.id.btn_back);
        postTitle = findViewById(R.id.editTextTitle);
        postContent = findViewById(R.id.editTextContent);
        btnCreate = findViewById(R.id.btn_postcreate);
        btnDelete = findViewById(R.id.btn_postdelete);
        btnAddImage = findViewById(R.id.btn_addImage);
        textImageCount = findViewById(R.id.image_count);

        btnAddImage.setOnClickListener(v -> attachAlbum());     // 이미지 첨부하기 버튼
        btnBack.setOnClickListener(v -> cancelEdit());          // 뒤로 가기 버튼
        btnDelete.setOnClickListener(v -> cancelEdit());       // 취소 버튼
        btnCreate.setOnClickListener(v -> finishEdit());        // 작성 버튼
        init();
    }

    // 사용자가 addimage 버튼을 클릭하면 갤러리에서 이미지 선택해 이미지의 URI를 얻음
    private void init(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (uriArrayList.size() < 5) {
                    Log.i(tag + " uri 이미지 개수", "5개 이하면 실행됨");
                    if(intent.getData() != null){   // 이미지 1개만 선택
                        Log.e(tag + " 이미지 1개 선택", String.valueOf(intent.getData()));
                        Uri uri = intent.getData();     // 이미지 URI 얻음
                        if (!uriArrayList.contains(uri)){   // 중복된 uri가 있으면 추가X
                            uriArrayList.add(uri);
                        }
                        updateRecyclerView();
                    }
                    else {  // 여러 개 선택
                        ClipData clipData = intent.getClipData();
                        Log.e(tag, "이미지 다중 선택");
                        for (int i=0; i<clipData.getItemCount(); i++){
                            Uri uri = clipData.getItemAt(i).getUri();   // 선택한 이미지들의 uri 가져옴
                            Log.e("clipdata 이미지 여러 개 선택", String.valueOf(clipData.getItemAt(i).getUri()));
                            if(!uriArrayList.contains(uri) && uriArrayList.size() < 5){
                                uriArrayList.add(uri);
                            }
                        }
                        updateRecyclerView();
                    }
                } else {Log.e(tag + " uri 이미지 개수", "5개 초과하면 추가안됨");}
            }
        });
    }

    // 리사이클뷰 초기화 + 업데이트
    private void updateRecyclerView(){
        if(adapter == null){
            adapter = new ImageAdapter(uriArrayList, getApplicationContext(), this::updateRecyclerView);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(CommunityEdit.this, LinearLayoutManager.HORIZONTAL, true));
        }
        adapter.notifyDataSetChanged();
        textImageCount.setText("(" + uriArrayList.size() + "/5)");
    }

    // 앨범에서 이미지 가져오기
    private void attachAlbum(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);     // 사용자가 데이터(여기선 이미지) 1개 선택
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);     // 이미지 여러 개 가져옴
        activityResultLauncher.launch(intent);
    }

    // 작성 버튼을 누르면 글쓰기가 완료되고 입력한 내용이 db에 저장됨 + 게시판에 내가 쓴 글이 올라감
    private void finishEdit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CommunityEdit.this);
        AlertDialog alertDialog;
        builder.setMessage("게시글을 추가하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // call해서 작성한 게시글 추가하고 커뮤니티 메인화면으로 넘어가기 +  넘어가면 내가 작성한 게시글이 맨 위에 올라와야 있어야 함

                finish();
            }
        });
        builder.setNeutralButton("아니오", (dialog, which) -> dialog.dismiss());
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));
    }

    // 뒤로가기/취소 버튼을 누르면 게시글 작성을 취소하겠냐는 메시지 띄우기
    private void cancelEdit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CommunityEdit.this);
        AlertDialog alertDialog;
        builder.setMessage("작성을 취소하시겠습니까?");
        builder.setPositiveButton("네", (dialog, which) -> finish());
        builder.setNeutralButton("아니오", (dialog, which) -> dialog.dismiss());
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));
    }



}
