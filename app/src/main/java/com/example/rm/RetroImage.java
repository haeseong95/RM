package com.example.rm;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// 갤러리에서 이미지 가져오기
public class RetroImage extends AppCompatActivity {

    Button endcode, decode;
    ImageView imageView;

    String sImage;
    private static final int REQUEST_CODE = 0;  // 갤러리에서 사진 요청 코드
    private final int GALLERY = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retro_camera);

        decode = findViewById(R.id.camera_button);  // 디코딩
        endcode = findViewById(R.id.button_end);
        imageView = findViewById(R.id.camera_image);

        /*


        // 버튼 클릭 시 인텐트로 갤러리에 요청 코드 보내기
        endcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check condition
                if (ContextCompat.checkSelfPermission(RetroImage.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    // when permission is nor granted
                    // request permission
                    ActivityCompat.requestPermissions(RetroImage.this
                            , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

                }
                else
                {
                    // when permission
                    // is granted
                    // create method
                    selectImage();
                }
            }
        });

        decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // decode base64 string
                byte[] bytes=Base64.decode(sImage,Base64.DEFAULT);
                // Initialize bitmap
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                // set bitmap on imageView
                imageView.setImageBitmap(bitmap);
            }
        });


         */
    }

    private void selectImage() {
        imageView.setImageBitmap(null);
        // Initialize intent
        Intent intent=new Intent(Intent.ACTION_PICK);
        // set type
        intent.setType("image/*");
        // start activity result
        startActivityForResult(Intent.createChooser(intent,"Select Image"),100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // check condition
        if (requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            // when permission
            // is granted
            // call method
            selectImage();
        }
        else
        {
            // when permission is denied
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check condition
        if (requestCode==100 && resultCode==RESULT_OK && data!=null)
        {
            // when result is ok
            // initialize uri
            Uri uri=data.getData();
            // Initialize bitmap
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                // initialize byte stream
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                // compress Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                // Initialize byte array
                byte[] bytes=stream.toByteArray();
                // get base64 encoded string
                sImage= Base64.encodeToString(bytes,Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
