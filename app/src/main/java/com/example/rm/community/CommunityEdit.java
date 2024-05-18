package com.example.rm.community;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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
                try {
                    String title = postTitle.getText().toString();      // 제목, 내용은 인코딩X 걍 문자열 상태로 json으로 보낼 거임
                    String content = postContent.getText().toString();
                    ArrayList<String> encodedImages = new ArrayList<>();
                    for (Uri uri : uriArrayList){       // uri -> bitmap -> base64 인코딩
                        String encode = encodeBase64(uri);
                        encodedImages.add(encode);
                    }

                    Intent intent = new Intent(CommunityEdit.this, CommunityContent.class);
                    intent.putStringArrayListExtra("encodedImages", encodedImages);
                    startActivity(intent);

                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    // uriArrayList에 저장된 이미지들의 uri -> 비트맵으로 변환 (디코딩)
    private String encodeBase64(Uri uri) throws IOException {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        ByteArrayOutputStream outputStream = null;
        String result = null;


        try {
            // 1번째 디코딩
            inputStream = new BufferedInputStream(this.getContentResolver().openInputStream(uri));  // 이미지 데이터를 읽음
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;          // 메모리 할당을 막아 이미지 파일의 메타데이터만 읽어옴 (true는 decodeStream()으로 이미지 파일을 읽을 때 실제 메모리에 로드X -> 객체 생성X, null 비트맵 반환)
            BitmapFactory.decodeStream(inputStream, null, options);     // 이미지 디코딩, decodestream 호출해 이미지 파일로부터 메타데이터 읽어옴 (이미지의 가로/세로 크기 등 기본 정보를 얻어 이미지 처리 계획을 세워 메모리 절약)
            inputStream.close();    // 이미지 파일을 다시 읽기 위해 이전에 읽은 stream을 닫았다가 new 다시 생성
            options.inSampleSize = calculateInSampleSize(options);      // 샘플 사이즈 계산
            options.inJustDecodeBounds = false;     // 실제 데이터를 메모리에 로드

            // 2번째 디코딩 (실제 이미지를 메모리에 로드)
            inputStream = new BufferedInputStream(this.getContentResolver().openInputStream(uri));
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            // resize
            Bitmap resize = resizeBitmap(bitmap, 500, 500);

            // Bitmap -> Base64 인코딩
            outputStream = new ByteArrayOutputStream();
            resize.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            resize.recycle();
            byte[] byteArray = outputStream.toByteArray();
            outputStream.close();
            result = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            Log.d(tag + " 인코딩", "uri 이미지 : " + uri.toString() + ", Base64 변환 성공 : " + result);
        } catch (Exception e){
            Log.e(tag + " 인코딩", "uri 이미지 : " + uri.toString() + ", Base64 변환 실패: " + result);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(tag, "InputStream 닫기 실패", e);
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e(tag, "OutputStream 닫기 실패", e);
                }
            }
        }
        return result;
    }

    // 이미지 사이즈 계산
    private int calculateInSampleSize(BitmapFactory.Options options){
        int MAX_WIDTH = dpToPx(getApplicationContext(), 150);  // 이미지를 표시할 imageview의 가로 크기
        int MAX_HEIGHT = dpToPx(getApplicationContext(), 150);  // 이미지 세로
        int height = options.outHeight;    // 이미지 세로, 가로, MIME 타입("image/jpeg" 등) 얻음 -> 이미지 원본 크기를 얻어 inSampleSize를 설정해 이미지 비율 축소O
        int width = options.outWidth;
        int sampleSize = 2;
        if (height > MAX_HEIGHT || width > MAX_WIDTH) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / sampleSize) >= MAX_HEIGHT && (halfWidth / sampleSize) >= MAX_WIDTH) {
                sampleSize *= 2;
            }
        }
        Log.i(tag + " dp -> 픽셀", "가로 : " + MAX_WIDTH + ", 세로 : " + MAX_HEIGHT);
        return sampleSize;
    }

    // 이미지 resize
    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= maxWidth && height <= maxHeight) {
            return bitmap; // 이미지가 최대 크기보다 작거나 같으면 리사이징 없이 반환
        }

        float bitmapRatio = (float)width / (float)height;
        if (bitmapRatio > 1) {
            width = maxWidth;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxHeight;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }


    // 디바이스의 DPI 계산 (dp -> 픽셀 변환)
    public int dpToPx(Context context, int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }



}
