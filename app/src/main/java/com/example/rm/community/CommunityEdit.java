package com.example.rm.community;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.R;

// 커뮤니티 글쓰기 화면
public class CommunityEdit extends AppCompatActivity {

    ImageView btnBack;
    EditText postTitle, postContent;    // 제목, 내용 입력창
    Button btnAttachImage;     // 이미지 첨부하기 버튼
    TextView btnCreate, btnDelete;   // 작성, 취소 버튼
    AlertDialog.Builder builder = new AlertDialog.Builder(CommunityEdit.this);
    AlertDialog.Builder builder2 = new AlertDialog.Builder(CommunityEdit.this);
    AlertDialog alertDialog;

    // 이미지는 어떻게 해야 할지 모르게씅ㅁ (가려야 하나)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_edit);

        btnBack = findViewById(R.id.btn_back);
        postTitle = findViewById(R.id.editTextTitle);
        postContent = findViewById(R.id.editTextContent);
        btnAttachImage = findViewById(R.id.btn_plusimage);
        btnCreate = findViewById(R.id.btn_postcreate);
        btnDelete = findViewById(R.id.btn_postdelete);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage();
            }
        });

        // 이미지 첨부하기 버튼을 클릭하면 갤러리에서 사진 가져오기 (최대 10장)
        btnAttachImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

}
