package com.example.rm;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Call;

public class RecycleLocation extends AppCompatActivity {

    ImageView btnBack;

    // 테스트
    ListView listView;
    ArrayList<TrashMainListData> arrayList = new ArrayList<>();
    TrashAdapter2 trashAdapter2;


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


    }

    private void setText(){
        Call<RetroApi> call = RetroClient.getRetroService().


    }



}
