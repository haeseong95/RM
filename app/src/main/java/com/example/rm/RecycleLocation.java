package com.example.rm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleLocation extends AppCompatActivity {

    ImageView btnBack;

    // 테스트
    ListView listView;
    ArrayList<TrashMainListData> arrayList = new ArrayList<>();
    TrashAdapter2 trashAdapter2;

    // 이미지
    private final int GALLERY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        btnBack = (ImageView)findViewById(R.id.btn_back);
        listView = findViewById(R.id.testlist);

        // 뒤로 가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        trashAdapter2 = new TrashAdapter2(this, arrayList);
        test();
        listView.setAdapter(trashAdapter2);

    }

    // 이미지
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    // 이미지 업로드
    private void upLoadImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());

    }


    private void test(){
        Call<List<RetroUser>> call = RetroClient.getRetroService().getImage();
        call.enqueue(new Callback<List<RetroUser>>() {
            @Override
            public void onResponse(Call<List<RetroUser>> call, Response<List<RetroUser>> response) {
                if (response.isSuccessful()) {

                    int id = 0;
                    String title, url = null;
                    List<RetroUser> retroUser = response.body();

                    for (int i=0; i<7; i++){
                        RetroUser user = retroUser.get(i);

                        title = user.getTitle();
                        id = user.getId();
                        url = user.getUrl();
                        arrayList.add(new TrashMainListData(url, title));  // arrayList에 항목 추가
                        Log.i("category 리스트뷰 전달 성공", "title : " + title + ", body : "+ url);
                    }

                    trashAdapter2.notifyDataSetChanged();    // listview 리스트의 크기+아이템 둘 다 변경될 때 사용 (=리스트 업데이트)
                }
                else {
                    Log.e("category 리스트뷰", "리스트뷰 출력 실패");
                }
            }

            @Override
            public void onFailure(Call<List<RetroUser>> call, Throwable t) {

            }
        });

    }


    /*
    private void setText(){
        Call<RetroApi> call = RetroClient.getRetroService().setCategoryMain();

        call.enqueue(new Callback<RetroApi>() {
            @Override
            public void onResponse(Call<RetroApi> call, Response<RetroApi> response) {
                ArrayList<RetroWriting> retroWritings = response.body().getRetroWriting();

                String name, info, image;

                if(response.isSuccessful()) {
                    for (RetroWriting writing : retroWritings) {
                        name = writing.getTitle();
                        info = writing.getContent();
                        image = writing.getImage();
                        arrayList.add(new TrashMainListData(image, name, info));
                        Log.i("지도, 리스트뷰 테스트", "image : " + image + ", name : " + name + ", info" + info);
                    }

                    trashAdapter2.notifyDataSetChanged();

                    int totalHeight = 0;
                    for (int i = 0; i < trashAdapter2.getCount(); i++){
                        View listItem = trashAdapter2.getView(i, null, listView);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }
                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    params.height = totalHeight + (listView.getDividerHeight() * (trashAdapter2.getCount() - 1));
                    listView.setLayoutParams(params);
                }else {
                    Log.e("리스트뷰", "리스트뷰 출력 실패");
                }
            }

            @Override
            public void onFailure(Call<RetroApi> call, Throwable t) {
                Log.e("리스트뷰", "예외, 네트워크 오류 등", t);
            }
        });


    }

     */



}
