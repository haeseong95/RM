package com.example.rm;

import static com.gun0912.tedpermission.normal.TedPermission.create;
import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int RESULT_CODE = 22;

    LinearLayout btnSearch, btnCamera;      // 검색창, 카메라
    LinearLayout btnCan, btnCouch, btnPlasticBag, btnBattery, btnStink, btnGlass, btnClothes, btnPaper, btnPlastic, btnRes;      // 카테고리 버튼
    Button mainMap, mainCommunity, mainUserinfo;        // 툴바 아이콘

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = (LinearLayout)findViewById(R.id.li_search);
        btnCamera = (LinearLayout)findViewById(R.id.li_camera);     // 카메라
        btnCan = (LinearLayout) findViewById(R.id.btn_can);
        btnCouch = (LinearLayout) findViewById(R.id.btn_couch);
        btnPlasticBag = (LinearLayout) findViewById(R.id.btn_plastic_bag);
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

        btnSearch.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnCan.setOnClickListener(this);
        btnCouch.setOnClickListener(this);
        btnPlasticBag.setOnClickListener(this);
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
        PreferenceHelper.init(this);

    }

    /*
    // 카메라 권한 허용
    private void cameraPermission(){
        create()
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }


    // 카메라 결과를 camera.java에 전달함
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Intent intent = new Intent(MainActivity.this, Camera.class);
            intent.putExtra("picture", bitmap);
            startActivity(intent);
        }
    }

     */

    // 앱이 다시 활성화 될 때 로그인 상태에세 따라 로그인/메인페이지 텍스트 갱신, true(마이페이지)/false(로그인)
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
            case R.id.btn_couch: return "대형폐기물";
            case R.id.btn_plastic_bag: return "비닐류";
            case R.id.btn_battery: return "생활유혜폐기물";
            case R.id.btn_stink: return "음식물쓰레기";
            case R.id.btn_glass: return "유리병";
            case R.id.btn_clothes: return "의류";
            case R.id.btn_paper: return "종이류";
            case R.id.btn_plastic: return "플라스틱류";
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
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CODE);
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



}