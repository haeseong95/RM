package com.example.rm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnSetting;      // 관리자, 공지사항
    LinearLayout btnSearch, btnCamera;      // 검색창, 카메라
    LinearLayout btnCan, btnCouch, btnPlastic_bag, btnBattery, btnStink, btnGlass, btnClothes, btnPaper, btnPlastic, btnRes;      // 카테고리 버튼
    Button mainMap, mainCommunity, mainUserinfo;        // 툴바 아이콘

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSetting = (ImageView)findViewById(R.id.btn_setting);
        btnSearch = (LinearLayout)findViewById(R.id.li_search);
        btnCamera = (LinearLayout)findViewById(R.id.li_camera);
        btnCan = (LinearLayout) findViewById(R.id.btn_can);
        btnCouch = (LinearLayout) findViewById(R.id.btn_couch);
        btnPlastic_bag = (LinearLayout) findViewById(R.id.btn_plastic_bag);
        btnBattery = (LinearLayout) findViewById(R.id.btn_battery);
        btnStink = (LinearLayout) findViewById(R.id.btn_stink);
        btnGlass = (LinearLayout) findViewById(R.id.btn_glass);
        btnClothes = (LinearLayout) findViewById(R.id.btn_clothes);
        btnPaper = (LinearLayout) findViewById(R.id.btn_paper);
        btnPlastic = (LinearLayout) findViewById(R.id.btn_plastic);
        btnRes = (LinearLayout) findViewById(R.id.btn_res);
        mainMap = (Button)findViewById(R.id.main_map);
        mainCommunity = (Button)findViewById(R.id.main_community);
        mainUserinfo = (Button)findViewById(R.id.main_userinfo);

        btnSetting.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnCan.setOnClickListener(this);
        btnCouch.setOnClickListener(this);
        btnPlastic_bag.setOnClickListener(this);
        btnBattery.setOnClickListener(this);
        btnStink.setOnClickListener(this);
        btnGlass.setOnClickListener(this);
        btnClothes.setOnClickListener(this);
        btnPaper.setOnClickListener(this);
        btnPlastic.setOnClickListener(this);
        btnRes.setOnClickListener(this);
        mainMap.setOnClickListener(this);
        mainCommunity.setOnClickListener(this);
        mainUserinfo.setOnClickListener(this);


    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()){
            case R.id.btn_setting:
                startActivity(new Intent(MainActivity.this, LoginUser.class));
                break;
            case R.id.li_search:
                startActivity(new Intent(MainActivity.this, LoginUser.class));
                break;
            case R.id.li_camera:
                startActivity(new Intent(MainActivity.this, LoginUser.class));
                break;
            case R.id.btn_can:
            case R.id.btn_couch:
            case R.id.btn_plastic_bag:
            case R.id.btn_battery:
            case R.id.btn_stink:
            case R.id.btn_glass:
            case R.id.btn_clothes:
            case R.id.btn_paper:
            case R.id.btn_plastic:
            case R.id.btn_res:
                startActivity(new Intent(MainActivity.this, LoginUser.class));
                break;
            case R.id.main_map:
                startActivity(new Intent(MainActivity.this, LoginUser.class));
                break;
            case R.id.main_community:
                startActivity(new Intent(MainActivity.this, LoginUser.class));
                break;
            case R.id.main_userinfo:
                startActivity(new Intent(MainActivity.this, LoginUser.class));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }



}