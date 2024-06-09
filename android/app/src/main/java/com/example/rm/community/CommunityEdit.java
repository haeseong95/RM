package com.example.rm.community;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;
import com.example.rm.token.TokenManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommunityEdit extends AppCompatActivity {
    ImageView btnBack;
    RecyclerView recyclerView;
    EditText postTitle, postContent;
    TextView btnCreate, btnDelete, btnAddImage, textImageCount;

    // 값
//    private boolean onlyOneCall = false;
    private int initialImageCount;
    static final String tag = "게시글 작성 페이지";
    private String postHash;
    ImageAdapter adapter;
    ArrayList<Uri> uriArrayList = new ArrayList<>();    // 이미지 uri 담음
    ArrayList<ArrayList<String>> files = new ArrayList<>();
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
        btnBack.setOnClickListener(v -> {cancelEdit();});          // 뒤로 가기 버튼
        btnDelete.setOnClickListener(v -> cancelEdit());       // 취소 버튼
        btnCreate.setOnClickListener(v -> {finishEdit();});        // 작성 버튼
        init();     // 이미지 URI 얻기

        String post_hash = getIntent().getStringExtra("modify_post_hash");
        String post_title = getIntent().getStringExtra("modify_post_title");
        String post_content = getIntent().getStringExtra("modify_post_content");
        String post_image_Array = getIntent().getStringExtra("modify_post_image_Array");
        ArrayList<String> image_uri = getIntent().getStringArrayListExtra("modify_post_image_file_path");   // 게시글에서 보여준 이미지 비트맵
        Log.e("이미지 전달됨 ", String.valueOf(image_uri));

        try {
            if (
                    post_hash != null &&
                            post_title != null &&
                            post_content != null &&
                            post_image_Array != null
            ){
                postHash = post_hash;
                postTitle.setText(post_title);
                postContent.setText(post_content);

                JSONArray postImageArray = new JSONArray(post_image_Array);

                // 기존 이미지 + new 이미지 같은 어댑터로 정리
                for (int i = 0; i < postImageArray.length(); i++) {
                    JSONObject imageObject = postImageArray.getJSONObject(i);
                    ArrayList<String> file = new ArrayList<>();
                    String fileLocation = imageObject.getString("fileLocation");
                    String name = imageObject.getString("name");

                    file.add(fileLocation);
                    file.add(name);

                    Log.e("fileLocation 값", fileLocation);
                    Log.e("name 값", name);

                    files.add(file);

                    Log.e("postImageArray.getString(i)", postImageArray.getString(i));

                    Uri imageUri = Uri.parse(postImageArray.getString(i));
                    uriArrayList.add(imageUri);
                    Log.e("uriArrayList", uriArrayList.get(i).toString());

                }

                initialImageCount = uriArrayList.size();
                Log.e("uriArrayList.size()", String.valueOf(uriArrayList.size()));
                Log.i("기존 + new 이미지","files에는 기존 게시글 사진 경로가 저장됨 : " + String.valueOf(files));
                updateModifyImage(image_uri, files);


                // 수정 버튼
//                ArrayList<ArrayList<String>> deleteImage = adapter.getDeleteImage();   // 기존 이미지 삭제할 거 저장
                btnCreate.setOnClickListener(view -> {
                    try {
                        ArrayList<ArrayList<String>> deleteImage = adapter.getDeleteImage();   // 기존 이미지 삭제할 거 저장
                        Log.e("post_title", post_title);
                        Log.e("post_content", post_content);
                        Log.e("uriArrayList", String.valueOf(uriArrayList.size()));
                        Log.e("deleteImage", String.valueOf(deleteImage));

                        ArrayList<Uri> newArrayList = new ArrayList<>( uriArrayList.subList(initialImageCount - deleteImage.size(), uriArrayList.size()) );
                        submitUpdatedPost(post_title, post_content, newArrayList, deleteImage);
                    } catch (IOException e) {
                        Log.e("수정 버튼 클릭부터 에러", "!!!!!!!!!!!!!!!!!");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONException 발생함", e.toString());
        }
        Log.i(tag + " 수정", "수정할 해시값 : " + post_hash + ", 게시글 제목 : " + post_title + ", 게시글 내용 : " + post_content);

    }

    protected void onResume(){
        super.onResume();
        updateRecyclerView();
    }

    // 게시글 수정 시 원래 이미지 + 새 이미지 추가/삭제
    private void updateModifyImage(ArrayList<String> filePaths, ArrayList<ArrayList<String>> files) {
        uriArrayList.clear();
        for (String filePath : filePaths) {
            File imgFile = new File(filePath);
            if (imgFile.exists()) {
                Uri imageUri = Uri.fromFile(imgFile);
                uriArrayList.add(imageUri);
            }
        }

        Log.e("uriArrayList.size() updateModifyImage", String.valueOf(uriArrayList.size()));

        if (adapter == null) {
            adapter = new ImageAdapter(uriArrayList, files, getApplicationContext(), this::updateRecyclerView);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        } else {
            adapter.notifyDataSetChanged();
        }
        textImageCount.setText("(" + uriArrayList.size() + "/5)");
    }

    // 작성 버튼을 누르면 글쓰기가 완료되고 입력한 내용이 db에 저장됨 + 게시판에 내가 쓴 글이 올라감
    private void finishEdit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CommunityEdit.this);
        AlertDialog alertDialog;
        builder.setMessage("게시글을 추가하시겠습니까?");
        builder.setPositiveButton("네", (dialog, which) -> {
            try {
                String title = postTitle.getText().toString();      // 제목, 내용은 인코딩X 걍 문자열 상태로 json으로 보낼 거임
                String content = postContent.getText().toString();
                submitPost(title, content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton("아니오", (dialog, which) -> dialog.dismiss());
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
    }

    // 게시글 제목/내용/이미지 서버로 전송
    private void submitPost(String title, String content) throws IOException {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("title", title)
                    .addFormDataPart("contentText", content)
                    .addFormDataPart("type", "post")
                    .addFormDataPart("whichWriting", "");

            // 이미지 추가
            if (!uriArrayList.isEmpty()) {
                ArrayList<String> imageList = new ArrayList<>();
                for (int i = 0; i < uriArrayList.size(); i++) {
                    Uri uri = uriArrayList.get(i);
                    Bitmap bitmap = null;
                    try {
                        bitmap = decodeBitmap(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String imageFileName = uri.getLastPathSegment();
                    multipartBodyBuilder.addFormDataPart("images" + (i+1), imageFileName, RequestBody.create(byteArray, MediaType.parse("image/jpeg")));
                    imageList.add(String.valueOf(i));
                }
                multipartBodyBuilder.addFormDataPart("image_lines", new JSONArray(imageList).toString());
            }

            RequestBody requestBody = multipartBodyBuilder.build();
            TokenManager tokenManager = new TokenManager(getApplicationContext());
            String token = tokenManager.getToken();
            String deviceModel = Build.MODEL;

            if (token == null) {
                Log.e(tag, "Token is null");
                runOnUiThread(() -> Toast.makeText(CommunityEdit.this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show());
                return;
            }

            if (deviceModel == null) {
                Log.e(tag, "Device model is null");
                runOnUiThread(() -> Toast.makeText(CommunityEdit.this, "디바이스 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show());
                return;
            }

            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/create/users/writings")
                    .post(requestBody)
                    .addHeader("Authorization", token)
                    .addHeader("Device-Info", deviceModel)
                    .build();

            Log.d(tag, "Request: " + request.toString());

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Log.e(tag, "게시글 작성 완료 " + response.body().toString());
                        Toast.makeText(CommunityEdit.this, "게시글이 성공적으로 작성되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent result = new Intent();
                        result.putExtra("post_create_success", true);
                        setResult(RESULT_OK, result);

                        Intent intent = new Intent(CommunityEdit.this, Community.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        try {
                            Log.e(tag, "게시글 작성 실패 " + responseBody);
                            Toast.makeText(CommunityEdit.this, "게시글 작성에 실패했습니다: " + responseBody, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }).start();
    }

    // 수정된 게시글과 이미지 정보를 서버로 전송
    private void submitUpdatedPost(String title, String content, ArrayList<Uri> newImages, ArrayList<ArrayList<String>> deletedImages) throws IOException {
        new Thread(() -> {

            String jsonData = (new Gson()).toJson(deletedImages);
            OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("hash", postHash)
                    .addFormDataPart("title", title)
                    .addFormDataPart("contentText", content)
                    .addFormDataPart("deleteImageNames", jsonData);

            // 이미지 추가
            if (!newImages.isEmpty()) {
                Log.e("newImages", newImages.toString());
                ArrayList<String> imageList = new ArrayList<>();
                int notServerImageStart = initialImageCount - deletedImages.size();
                Log.e("notServerImageStart", String.valueOf(notServerImageStart));
                Log.e("newImages.size()", String.valueOf(newImages.size()));
                for (int i = 0; i < newImages.size(); i++) {
                    Uri uri = newImages.get(i);
                    Bitmap bitmap = null;
                    try {
                        bitmap = decodeBitmap(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String imageFileName = uri.getLastPathSegment();
                    multipartBodyBuilder.addFormDataPart("images" + (i + 1), imageFileName, RequestBody.create(byteArray, MediaType.parse("image/jpeg")));
                    imageList.add(imageFileName.split(":")[1]);
                }
                multipartBodyBuilder.addFormDataPart("new_image_lines", new JSONArray(imageList).toString());
                Log.e("new_image_lines", new JSONArray(imageList).toString());
            }

//        // 추가할 이미지 정보
//        JSONArray newImageLines = new JSONArray();
//        for (int i = 0; i < newImages.size(); i++) {
//            Uri imageUri = newImages.get(i);
//            Bitmap bitmap = decodeBitmap(imageUri);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
////            String imageFileName = "updated_image_" + i + ".jpg";
//            String imageFileName = imageUri.getLastPathSegment();
//            multipartBodyBuilder.addFormDataPart("newImages", imageFileName,
//                    RequestBody.create(MediaType.parse("image/jpeg"), stream.toByteArray()));
//            newImageLines.put(i); // 이미지 순서 정보
//        }
//        multipartBodyBuilder.addFormDataPart("new_image_lines", newImageLines.toString());

//        // 삭제할 이미지 정보
//        JSONArray deleteImageNames = new JSONArray();
//        for (String fileInfo : deletedImages) {
//            JSONArray fileJson = new JSONArray(Arrays.asList(fileInfo));
//            deleteImageNames.put(fileJson);
//        }
//        multipartBodyBuilder.addFormDataPart("deleteImageNames", deleteImageNames.toString());

            // 토큰과 디바이스 정보를 헤더에 추가

//            RequestBody requestBody = multipartBodyBuilder.build();
            String token = new TokenManager(getApplicationContext()).getToken();
            String deviceModel = Build.MODEL;

            if (token == null) {
                Log.e(tag, "Token is null");
                runOnUiThread(() -> Toast.makeText(CommunityEdit.this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show());
                return;
            }

            if (deviceModel == null) {
                Log.e(tag, "Device model is null");
                runOnUiThread(() -> Toast.makeText(CommunityEdit.this, "디바이스 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show());
                return;
            }

            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/update/writing/info")
                    .post(multipartBodyBuilder.build())
                    .addHeader("Authorization", token)
                    .addHeader("Device-Info", deviceModel)
                    .build();

//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    runOnUiThread(() -> Toast.makeText(CommunityEdit.this, "네트워크 오류: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        runOnUiThread(() -> {
//                            Toast.makeText(CommunityEdit.this, "게시글 수정 성공", Toast.LENGTH_SHORT).show();
//                            finish(); // 액티비티 종료
//                        });
//                    } else {
//                        Log.e("게시글 수정 실패", response.body().toString());
//                    }
//                }
//            });

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Log.e(tag, "게시글 수정 완료 " + response.body().toString());
                        Toast.makeText(CommunityEdit.this, "게시글이 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent result = new Intent();
                        result.putExtra("post_create_success", true);
                        setResult(RESULT_OK, result);

                        Intent intent = new Intent(CommunityEdit.this, CommunityContent.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        try {
                            Log.e(tag, "게시글 수정 실패 " + responseBody);
                            Toast.makeText(CommunityEdit.this, "게시글 수정에 실패했습니다: " + responseBody, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }).start();
    }

    // 리사이클뷰 초기화 + 업데이트
    private void updateRecyclerView(){
        if(adapter == null){
            adapter = new ImageAdapter(uriArrayList, (new ArrayList<>()), getApplicationContext(), this::updateRecyclerView);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(CommunityEdit.this, LinearLayoutManager.HORIZONTAL, false));
            Log.e("어댑터 초기화 시킴 ", "어댑터 초기화됨");
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

    // 사용자가 이미지 첨부하기 버튼을 클릭하면 갤러리에서 이미지 선택해 이미지의 URI를 얻음
    private void init(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (uriArrayList.size() < 5) {
                    Log.i(tag + " uri 이미지 개수", "5개 이하면 실행됨");
                    if(intent.getData() != null){   // 이미지 1개만 선택
                        Log.e(tag + " 이미지 1개 선택", String.valueOf(intent.getData()));
                        Uri uri = intent.getData();     // 이미지 URI 얻음
                        if (!uriArrayList.contains(uri) && uriArrayList.size() < 5){   // 중복된 uri가 있으면 추가X
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
//                        updateRecyclerView();

                        adapter.notifyDataSetChanged();
                        textImageCount.setText("(" + uriArrayList.size() + "/5)");
                    }
                } else {Log.e(tag + " uri 이미지 개수", "5개 초과하면 추가안됨");}
            }
        });
    }

    // uriArrayList에 저장된 이미지들의 uri -> 비트맵으로 변환 (디코딩)
    private Bitmap decodeBitmap(Uri uri) throws IOException {
        InputStream inputStream = null;
        Bitmap bitmap = null;

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

            // 이미지 회전 각도 감지 및 회전
            inputStream = new BufferedInputStream(this.getContentResolver().openInputStream(uri));
            ExifInterface exif = new ExifInterface(inputStream);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationDegrees = exifToDegrees(orientation);
            bitmap = rotateBitmap(bitmap, rotationDegrees);

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(tag, "InputStream 닫기 실패", e);
                }
            }
        }
        return bitmap;
    }

    // 이미지 돌아간 거 회전시켜줌1
    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    // 이미지 돌아간 거 회전시켜줌2
    private Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return rotatedBitmap;
        }
        return bitmap;
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


    // 디바이스의 DPI 계산 (dp -> 픽셀 변환)
    public int dpToPx(Context context, int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // 뒤로가기/취소 버튼을 누르면 게시글 작성을 취소하겠냐는 메시지 띄우기
    private void cancelEdit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CommunityEdit.this);
        AlertDialog alertDialog;
        builder.setMessage("작성을 취소하시겠습니까?");
        builder.setPositiveButton("네", (dialog, which) -> finish());
        builder.setNegativeButton("아니오", (dialog, which) -> dialog.dismiss());
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
    }

}