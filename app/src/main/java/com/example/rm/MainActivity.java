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
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.account.LoginUser;
import com.example.rm.category.CategoryInfo;
import com.example.rm.category.SearchTrash;
import com.example.rm.community.Community;
import com.example.rm.mypage.Mypage;
import com.example.rm.token.PreferenceHelper;
import com.example.rm.token.TokenManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    // 레이아웃
    LinearLayout btnSearch, btnCamera;      // 검색창, 카메라
    LinearLayout btnCan, btnGlass, btnPaper, btnPlastic, btnBattery, btnPlasticBag, btnSofa, btnRes;      // 카테고리 버튼
    Button mainMap, mainCommunity, mainUserinfo;        // 툴바 아이콘
    RecyclerView recyclerView;
    //
    private static final String tag = "메인화면";
    TokenManager tokenManager;
    NoticeAdapter adapter;
    ArrayList<NoticeData> arrayList = new ArrayList<>();    // 공지사항 데이터 담음


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
        btnBattery = findViewById(R.id.btn_battery);
        btnPlasticBag = findViewById(R.id.btn_plastic_bag);
        btnSofa = findViewById(R.id.btn_sofa);
        btnRes = findViewById(R.id.btn_res);
        mainMap = (Button)findViewById(R.id.main_map);
        mainCommunity = (Button)findViewById(R.id.main_community);
        mainUserinfo = (Button)findViewById(R.id.main_userinfo);
        recyclerView = findViewById(R.id.main_recyclerView);

        btnSearch.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnCan.setOnClickListener(this);
        btnGlass.setOnClickListener(this);
        btnPaper.setOnClickListener(this);
        btnPlastic.setOnClickListener(this);
        btnBattery.setOnClickListener(this);
        btnPlasticBag.setOnClickListener(this);
        btnSofa.setOnClickListener(this);
        btnRes.setOnClickListener(this);
        mainMap.setOnClickListener(this);
        mainCommunity.setOnClickListener(this);
        mainUserinfo.setOnClickListener(this);
        PreferenceHelper.init(this);
        tokenManager = new TokenManager(this);

        // 공지사항
        setRecyclerView();
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
            case R.id.btn_sofa: return "대형폐기물";
            case R.id.btn_plastic_bag: return "비닐류";
            case R.id.btn_battery: return "생활유혜폐기물";
            case R.id.btn_res: return "폐가전제품";
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
            case R.id.btn_sofa:
            case R.id.btn_plastic_bag:
            case R.id.btn_battery:
            case R.id.btn_res:
                intent = new Intent(MainActivity.this, CategoryInfo.class);
                intent.putExtra("category", categoryText(v.getId()));
                break;
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

    // 리사이클러뷰 세팅
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NoticeAdapter(arrayList, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }


}