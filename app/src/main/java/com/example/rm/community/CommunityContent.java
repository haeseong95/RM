package com.example.rm.community;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rm.R;

import java.io.IOException;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class CommunityContent extends AppCompatActivity {
    // 레이아웃
    ViewPager2 viewPager2;
    ViewPagerAdapter adapter;
    FragmentStateAdapter fragmentStateAdapter;
    CircleIndicator3 indicator3;
    ImageView btnBack;
    ImageView like;  // 상단 사진, 추천 따봉 이미지
    TextView cNickname, cLevel, cDate, cTitle, cContent, cCount;    // 닉네임, 등급, 생성날짜, 게시글 제목, 게시글 내용, 추천 개수
    ListView listView;  // 댓글
    EditText editText;  // 댓글 입력창

    // 값
    private static final String tag = "CommunityContent";
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();    // viewpager의 이미지를 담을 bitmap 리스트

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_content);

        btnBack = findViewById(R.id.btn_back);
        cNickname = findViewById(R.id.cc_nickname);
        cLevel = findViewById(R.id.cc_level);
        cDate = findViewById(R.id.cc_date);
        cTitle = findViewById(R.id.cc_title);
        cContent = findViewById(R.id.cc_content);
        cCount = findViewById(R.id.cc_count);
        listView = findViewById(R.id.cc_comment);
        editText = findViewById(R.id.cc_edit_comment);
        viewPager2 = findViewById(R.id.viewpager);
        indicator3 = findViewById(R.id.indicator);

        btnBack.setOnClickListener(v -> finish());

        adapter = new ViewPagerAdapter(getApplicationContext(), bitmapArrayList);   // 어댑터 초기화 문제였나 arraylist에 데이터를 넣기 전에 초기화를 하는 게 맞나
        // edit에서 변환한 base64값을 테스트해보기 위함
        Intent intent = getIntent();
        ArrayList<String> encodedImages = intent.getStringArrayListExtra("encodedImages");
        if (encodedImages != null) {
            for (String base64 : encodedImages) {
                try {
                    decodeBase64toBitmap(base64);  // Base64 문자열을 Bitmap으로 변환 후 RecyclerView에 추가
                    Log.i(tag, "base64 값 : " + base64);
                } catch (IOException e) {
                    Log.e(tag, "base64 -> bitmap 변환 오류", e);
                }
            }
        }

        setViewPager();
    }

    // bitmap을 이용해 뷰페이저에서 보여줄 이미지 얻음
    private void setViewPager(){
        viewPager2.setAdapter(adapter);     // viepager2에 어댑터 연결
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);   // 가로 슬라이드
        indicator3.setViewPager(viewPager2);    // 인디케이터 설정
    }

    // 서버에서 보낸 base64 이미지 -> bitmap으로 변환하기
    private void decodeBase64toBitmap(String base64) throws IOException {
        try {
            byte[] decodeBytes = Base64.decode(base64, Base64.NO_WRAP);     // base64 문자열 -> byte[]로 디코딩, Base64 클래스가 java 라이브러리면 플래그X /android 클래스는 플래그 사용O
            BitmapFactory.Options options = new BitmapFactory.Options();    // 이미지 크기를 먼저 파악
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length, options);
            options.inSampleSize = calculateInSampleSize(options);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length);      // byte[] -> bitmap 변환
            if (adapter != null){
                bitmapArrayList.clear();
                bitmapArrayList.add(bitmap);
                adapter.notifyDataSetChanged();
            }
            Log.d(tag, "base64 -> bitamp 디코딩 성공");
        } catch (Exception e){
            Log.e(tag, "base64 -> bitamp 디코딩 실패", e);
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options){
        CommunityEdit communityEdit = new CommunityEdit();
        int MAX_WIDTH = communityEdit.dpToPx(getApplicationContext(), 384);
        int MAX_HEIGHT = communityEdit.dpToPx(getApplicationContext(), 250);
        int height = options.outHeight;    // 이미지 세로, 가로, MIME 타입("image/jpeg" 등) 얻음 -> 이미지 원본 크기를 얻어 inSampleSize를 설정해 이미지 비율 축소O
        int width = options.outWidth;
        int sampleSize = 4;
        if (height > MAX_HEIGHT || width > MAX_WIDTH) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / sampleSize) >= MAX_HEIGHT && (halfWidth / sampleSize) >= MAX_WIDTH) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

}
