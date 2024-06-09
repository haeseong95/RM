package com.example.rm.category;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.rm.R;
import com.example.rm.token.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TrashDetail extends AppCompatActivity {
    ImageView btnBack, trashImage;
    TextView trashName, trashInfo;
    static final String tag = "쓰레기 상세 페이지";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trash_detail);
        btnBack = (ImageView)findViewById(R.id.btn_back);
        trashImage = findViewById(R.id.trash_image);
        trashName = findViewById(R.id.title_trashName);
        trashInfo = findViewById(R.id.trash_info);
        btnBack.setOnClickListener(v -> finish());

        String hash = getIntent().getStringExtra("category_trash_detail_hash");
        getTrashDetail(hash);
    }

    // 쓰레기 텍스트 값 가져옴
    private void getTrashDetail(String hash) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("hash", hash);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/read/writing/list/post")
                    .post(requestBody)
                    .addHeader("Device-Info", Build.MODEL)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    JSONObject responseObject = new JSONObject(responseBody);
                    JSONObject json = responseObject.getJSONObject("message");  // json 결과를 배열로 저장
                    String title = json.getString("title");
                    String way = json.getString("contentText");
                    JSONArray imageArray = json.getJSONArray("images");

                    for (int i = 0; i < imageArray.length(); i++) {
                        JSONObject image = imageArray.getJSONObject(i);
                        String directory = image.getString("fileLocation");
                        String file = image.getString("name");
                        Log.e("디렉터리 파일", directory + file);
                        getImageData(directory, file);
                    }
                    runOnUiThread(() -> {
                        trashName.setText(title);
                        trashInfo.setText(way);
                        Log.i(tag, "쓰레기 텍스트 출력됨 : " + responseBody);
                    });
                } else {
                    Log.e(tag, "쓰레기 텍스트 망함 : " + responseBody);
                }
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    // 쓰레기 이미지 값 가져옴
    private void getImageData(String directory, String file){
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("directory", directory);
            jsonObject.put("file", file);
            Log.e("디렉터리 파일", directory + file);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Request request = new Request.Builder()
                .url("http://ipark4.duckdns.org:58395/api/read/image")
                .post(body)
                .addHeader("Device-Info", Build.MODEL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(tag, "이미지 서버 연결 실패", e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    byte[] imageBytes = response.body().bytes();
                    Bitmap bitmap = decodeSampledBitmapFromBytes(imageBytes, 1080, 1920);  // 원하는 크기로 디코딩
                    runOnUiThread(() -> {
                        trashImage.setImageBitmap(bitmap);
                        Log.e(tag, "쓰레기 이미지 출력됨 : " + response.body().toString());
                    });
                } else {
                    Log.e(tag, "쓰레기 이미지 망 : " + response.body().toString());
                }
            }
        });
    }

    private Bitmap decodeSampledBitmapFromBytes(byte[] bytes, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}