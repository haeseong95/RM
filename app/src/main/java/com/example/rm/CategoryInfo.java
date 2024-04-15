package com.example.rm;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class CategoryInfo extends AppCompatActivity {

    ImageView btnBack;     // 뒤로 가기 버튼
    ViewPager2 viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_info);


        btnBack = (ImageView) findViewById(R.id.btn_back);
        viewPager = (ViewPager2) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_l);

    }
}
