package com.example.rm;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.category.CategoryInfo;
import com.example.rm.category.SearchTrash;
import com.example.rm.community.Community;
import com.example.rm.community.CommunityEdit;
import com.example.rm.token.TokenManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView mainNotice;
    LinearLayout btnSearch, btnCamera;      // 검색창, 카메라
    LinearLayout btnCan, btnGlass, btnPaper, btnPlastic;      // 카테고리 버튼
    Button mainMap, mainCommunity, mainUserinfo;        // 툴바 아이콘
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getHashKey();

        btnSearch = (LinearLayout)findViewById(R.id.li_search);
        btnCamera = (LinearLayout)findViewById(R.id.li_camera);     // 카메라
        btnCan = (LinearLayout) findViewById(R.id.btn_can);
        btnGlass = (LinearLayout) findViewById(R.id.btn_glass);
        btnPaper = (LinearLayout) findViewById(R.id.btn_paper);
        btnPlastic = (LinearLayout) findViewById(R.id.btn_plastic);
        mainMap = (Button)findViewById(R.id.main_map);
        mainCommunity = (Button)findViewById(R.id.main_community);
        mainUserinfo = (Button)findViewById(R.id.main_userinfo);
        mainNotice = (ImageView) findViewById(R.id.main_notice);

        btnSearch.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnCan.setOnClickListener(this);
        btnGlass.setOnClickListener(this);
        btnPaper.setOnClickListener(this);
        btnPlastic.setOnClickListener(this);
        mainMap.setOnClickListener(this);
        mainCommunity.setOnClickListener(this);
        mainUserinfo.setOnClickListener(this);
        mainNotice.setOnClickListener(this);
        PreferenceHelper.init(this);
        tokenManager = new TokenManager(this);
    }

    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(PreferenceHelper.getLoginState()){mainUserinfo.setText("마이");}
        else {mainUserinfo.setText("로그인");}
    }

    // 각 버튼의 쓰레기 종류명을 CategoryInfo의 툴바에 출력하기 위해 데이터 전달
    private String categoryText(int id){
        switch (id){
            case R.id.btn_can: return "고철류";
            case R.id.btn_glass: return "유리병";
            case R.id.btn_paper: return "종이류";
            case R.id.btn_plastic: return "플라스틱류";
            default: return " ";
        }
    }

    // 버튼 클릭 시 액티비티 이동
    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){
            case R.id.li_search: intent = new Intent(MainActivity.this, SearchTrash.class); break;
            case R.id.li_camera:
                intent = new Intent(MainActivity.this, Camera.class);
                break;
            case R.id.btn_can:
            case R.id.btn_glass:
            case R.id.btn_paper:
            case R.id.btn_plastic:
                intent = new Intent(MainActivity.this, CategoryInfo.class);
                intent.putExtra("category", categoryText(v.getId()));
                break;
            case R.id.main_notice: intent = new Intent(MainActivity.this, Notice.class); break;
            case R.id.main_map: intent = new Intent(MainActivity.this, RecycleLocation.class); break;
            case R.id.main_community: intent = new Intent(MainActivity.this, Community.class); break;
            case R.id.main_userinfo:

                boolean login = PreferenceHelper.getLoginState();
                Log.d("로그인/마이페이지 버튼 클릭 시", "마이페이지(true) / 로그인(false) : " + login);

                if(login) {intent = new Intent(MainActivity.this, Mypage.class);}
                else {intent = new Intent(MainActivity.this, LoginUser.class);}
                break;
            default: throw new IllegalStateException("Unexpected value: " + v.getId());
        }

        startActivity(intent);
    }

    // 나중에 다 잡히면 사용
    private void checkToken(){
        if (tokenManager.LoginState(this)) {                // 로그인O -> 커뮤니티 활동O
            startActivity(new Intent(this, Community.class));
        } else {// 로그인 상태X -> 로그인 화면으로 이동
            startActivity(new Intent(this, LoginUser.class));
        }
    }

}