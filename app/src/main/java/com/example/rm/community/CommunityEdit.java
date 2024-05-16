package com.example.rm.community;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.R;

import java.util.ArrayList;

// 커뮤니티 글쓰기 화면
public class CommunityEdit extends AppCompatActivity {

    ListView listView;
    CommunityImageAdapter communityImageAdapter;
    ArrayList<ImageData> imageData = new ArrayList<>();
    ImageView btnBack;
    EditText postTitle, postContent;    // 제목, 내용 입력창
    TextView btnCreate, btnDelete, btnAddImage, textImageCount;   // 작성, 취소, 이미지 추가 버튼

    // 이미지는 어떻게 해야 할지 모르게씅ㅁ (가려야 하나)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_edit);

        listView = findViewById(R.id.listview_image);
        btnBack = findViewById(R.id.btn_back);
        postTitle = findViewById(R.id.editTextTitle);
        postContent = findViewById(R.id.editTextContent);
        btnCreate = findViewById(R.id.btn_postcreate);
        btnDelete = findViewById(R.id.btn_postdelete);
        btnAddImage = findViewById(R.id.btn_addImage);
        textImageCount = findViewById(R.id.image_count);

        AlertDialog.Builder builder = new AlertDialog.Builder(CommunityEdit.this);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(CommunityEdit.this);
        AlertDialog alertDialog;



        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // listview의 클릭 이벤트 정의: lsitview 안 버튼인 x 버튼을 클릭하면 선택된 사진을 지움
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        /*
        // 작성 버튼을 클릭하면 입력한 내용이 db에 저장되면서 커뮤니티 게시글에 추가되어야 함
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("게시글을 추가하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // call해서 작성한 게시글 추가하고 커뮤니티 메인화면으로 넘어가기




                        finish();
                    }
                });

                builder.setNeutralButton("아니오", (dialog, which) -> dialog.dismiss());
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // 취소 버튼 클릭하면 작성을 취소하겠습니까라는 메시지를 띄우고 확인 버튼을 클릭하면 작성 취소 후 뒤로가기
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage();
            }
        });


         */
    }

    // 제목창에 제목 입력하기
    private void editPostTitle(){

    }

    // 내용 입력창에 내용 입력하기, 글자 제한수 일단 1000자
    private void editPostContent(){

    }

    // 갤러리에서 사진 가져오기
    private void getImageToAlbum(){

    }

    /*

    // 뒤로가기/취소 버튼을 누르면 게시글 작성을 취소하겠냐는 메시지 띄우기
    private void showMessage(){
        builder2.setMessage("작성을 취소하시겠습니까?");
        builder2.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder2.setNeutralButton("아니오", (dialog, which) -> dialog.dismiss());
        alertDialog = builder2.create();
        alertDialog.show();
    }

     */

}
