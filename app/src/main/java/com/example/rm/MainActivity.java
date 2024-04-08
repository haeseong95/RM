package com.example.rm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "LogTest";

    ImageView btn_setting;      // 관리자, 공지사항
    LinearLayout li_search, li_camera;      // 검색창, 카메라
    LinearLayout li_can, li_couch, li_plastic_bag, li_battery, li_stink, li_glass, li_clothes, li_paper, li_plastic, li_res;   // 카테고리 버튼 순서대로
    Button main_home, main_map, main_community, main_userinfo;  // 하단 툴바

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     // 액티비티 화면 초기화

        BtnOnClick btnOnClick = new BtnOnClick();

        // 버튼 초기화
        btn_setting = (ImageView)findViewById(R.id.btn_setting);
        li_search = (LinearLayout)findViewById(R.id.li_search);
        li_camera = (LinearLayout)findViewById(R.id.li_camera);
        li_can = (LinearLayout)findViewById(R.id.li_can);
        li_couch = (LinearLayout)findViewById(R.id.li_couch);
        li_plastic_bag = (LinearLayout)findViewById(R.id.li_plastic_bag);
        li_battery = (LinearLayout)findViewById(R.id.li_battery);
        li_stink = (LinearLayout)findViewById(R.id.li_stink);
        li_glass = (LinearLayout)findViewById(R.id.li_glass);
        li_clothes = (LinearLayout)findViewById(R.id.li_clothes);
        li_paper = (LinearLayout)findViewById(R.id.li_paper);
        li_plastic = (LinearLayout)findViewById(R.id.li_plastic);
        li_res = (LinearLayout)findViewById(R.id.li_res);
        main_home = (Button)findViewById(R.id.main_home);
        main_map = (Button)findViewById(R.id.main_map);
        main_community = (Button)findViewById(R.id.main_community);
        main_userinfo = (Button)findViewById(R.id.main_userinfo);

        // 클릭 초기화
        btn_setting.setOnClickListener(btnOnClick);
        li_search.setOnClickListener(btnOnClick);
        li_camera.setOnClickListener(btnOnClick);
        li_can.setOnClickListener(btnOnClick);
        li_couch.setOnClickListener(btnOnClick);
        li_plastic_bag.setOnClickListener(btnOnClick);
        li_battery.setOnClickListener(btnOnClick);
        li_stink.setOnClickListener(btnOnClick);
        li_glass.setOnClickListener(btnOnClick);
        li_clothes.setOnClickListener(btnOnClick);
        li_paper.setOnClickListener(btnOnClick);
        li_plastic.setOnClickListener(btnOnClick);
        li_res.setOnClickListener(btnOnClick);
        main_home.setOnClickListener(btnOnClick);
        main_map.setOnClickListener(btnOnClick);
        main_community.setOnClickListener(btnOnClick);
        main_userinfo.setOnClickListener(btnOnClick);
    }

    // 클릭 시 페이지 이동
    class BtnOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_setting){
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_search) {
                Intent i = new Intent(MainActivity.this, SearchTrash.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_camera) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_can) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_couch) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_plastic_bag) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_battery) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_stink) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_glass) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_clothes) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_paper) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_plastic) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.li_res) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.main_home) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.main_map) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.main_community) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            } else if (v.getId() == R.id.main_userinfo) {
                Intent i = new Intent(MainActivity.this, LoginUser.class);
                startActivity(i);
            }


        }
    }




}