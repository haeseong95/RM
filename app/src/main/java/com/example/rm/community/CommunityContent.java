package com.example.rm.community;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rm.Mypage;
import com.example.rm.PreferenceHelper;
import com.example.rm.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class CommunityContent extends AppCompatActivity{
    // 레이아웃
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    CircleIndicator3 indicator3;
    ImageView btnBack;
    ImageView likeImage, sendComment;  // 좋아요 아이콘, 댓글 작성 완료 비행기 아이콘
    TextView cNickname, cLevel, cDate, cTitle, cContent, cCount;    // 닉네임, 등급, 생성날짜, 게시글 제목, 게시글 내용, 추천 개수
    RecyclerView recyclerView;  // 댓글
    EditText editText;  // 댓글 입력창
    Toolbar toolbar;

    //
    private static final String tag = "CommunityContent, 상세 게시글";
    private static int currentItemCount = -1;
    private int itemPosition = RecyclerView.NO_POSITION;    // item 초기값
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();    // viewpager의 이미지를 담을 bitmap 리스트
    ArrayList<CommentData> commentDataArrayList = new ArrayList<>();    // 댓글 데이터 담음
    CommentAdapter commentAdapter;

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
        editText = findViewById(R.id.cc_edit_comment);
        viewPager2 = findViewById(R.id.viewpager);
        indicator3 = findViewById(R.id.indicator);
        recyclerView = findViewById(R.id.comment_recyclerview);
        likeImage = findViewById(R.id.heart);
        sendComment = findViewById(R.id.send_comment);
        toolbar = findViewById(R.id.toolbar);
        btnBack.setOnClickListener(v -> finish());
        PreferenceHelper.init(CommunityContent.this);

        // 서버에서 받은 댓글 데이터를 게시글 상세 화면에 출력
        getPostData();

        // ViewPager
        showViewPager();

        // 좋아요 버튼
        String postId = "post1";    // 게시글 고유 ID 값
        setLike(postId);
        likeImage.setOnClickListener(v -> currentLike(postId));

        // 댓글
        getCommentData();   // 서버에서 댓글 데이터 가져옴
        setRecyclerView();  // 어댑터 설정
        sendComment.setOnClickListener(v -> addComment());  // 댓글 추가

        // 게시글 수정, 삭제
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.inflateMenu(R.menu.comment_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()){
                case R.id.action_delete:    // 게시글 삭제
                    postDelete();
                    return true;
                case R.id.action_edit:      // 게시글 수정
                    postModify();
                    return true;
                default:
                    return CommunityContent.super.onOptionsItemSelected(item);
            }
        });

        String postHash = getIntent().getStringExtra("postHash");
        String postUserId = getIntent().getStringExtra("postUserId");
    }

    // bitmap을 이용해 뷰페이저에서 보여줄 이미지 얻음
    private void setViewPager(){
        viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), bitmapArrayList);   // 어댑터 초기화 문제였나 arraylist에 데이터를 넣기 전에 초기화를 하는 게 맞나
        viewPager2.setAdapter(viewPagerAdapter);     // viepager2에 어댑터 연결
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
            bitmapArrayList.add(bitmap);
            Log.d(tag, "base64 -> bitamp 디코딩 성공");
        } catch (Exception e){
            Log.e(tag, "base64 -> bitamp 디코딩 실패", e);
        }
    }

    // edit에서 변환한 base64값을 테스트해보기 위해 viewpager2에 출력해봄
    private void showViewPager(){
        Intent intent = getIntent();
        ArrayList<String> encodedImages = intent.getStringArrayListExtra("encodedImages");
        if (encodedImages != null) {
            bitmapArrayList.clear();    // 기존 데이터 초기화
            for (String base64 : encodedImages) {
                try {
                    decodeBase64toBitmap(base64);  // Base64 문자열을 Bitmap으로 변환 후 RecyclerView에 추가
                    Log.i(tag, "base64 값 : " + base64);
                } catch (IOException e) {
                    Log.e(tag, "base64 -> bitmap 변환 오류", e);
                }
            }
            if (viewPagerAdapter == null){  // 어댑터가 초기화X 실행 -> 이미 초기화가 된 경우에는 데이터 변경 업데이트
                setViewPager();
            } else {
                viewPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    // 이미지 사이즈 계산 (용량 줄이기)
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

    // 초기 좋아요 정보
    private void setLike(String postId){
        boolean likeState = PreferenceHelper.getLikeState(postId);      // 좋아요 상태 정보 가져옴 (눌림-ture, 안눌림-false)
        int likeCount = PreferenceHelper.getLikeCount(postId);      // 좋아요 개수는 서버에서 받기
        cCount.setText(String.valueOf(likeCount));
        updateLikeImage(likeState);
    }

    // 현재 좋아요 정보
    private void currentLike(String postId){
        boolean currentLikeState = PreferenceHelper.getLikeState(postId);
        int currentLikeCount = PreferenceHelper.getLikeCount(postId);
        CommunityContent.this.setLikeState(postId, currentLikeState, currentLikeCount);
    }

    // 좋아요 클릭 시 상태/개수 변경
    private void setLikeState(String postId, boolean likeState, int count){
        int likeCount = count;

        if(likeState) { //likestate가 true, 즉 눌린 상태에서 또 누른거니까 취소
            PreferenceHelper.likeState(postId, false);
            updateLikeImage(false);
            likeCount -= 1;
            PreferenceHelper.likeCount(postId, likeCount);
            cCount.setText(String.valueOf(likeCount));
            Log.d(tag, "좋아요 취소" + PreferenceHelper.getLikeState(postId));
        } else {
            PreferenceHelper.likeState(postId, true);
            updateLikeImage(true);
            likeCount += 1;
            PreferenceHelper.likeCount(postId, likeCount);
            cCount.setText(String.valueOf(likeCount));
            Log.d(tag, "좋아요 누름"+ PreferenceHelper.getLikeCount(postId));
        }
    }

    // 좋아요 상태 정보에 따라 하트 이미지 달라짐
    private void updateLikeImage(boolean state){
        if(state){  //true, 즉 좋아요가 눌린 상태
            likeImage.setImageResource(R.drawable.community_fill_heart);
        } else {
            likeImage.setImageResource(R.drawable.community_empty_heart);
        }
    }

    // 서버에서 전송한 댓글 데이터 받음
    private void getCommentData() {

        List<CommentData> newItem = new ArrayList<>();


        for (int i = 0; i < 2; i++) {
            commentDataArrayList.add(new CommentData("닉네임 " + (i + 1), "등급 " + (i + 1), "2024-05-19", "댓글 " + (i + 1)));
            currentItemCount += 1;
        }
        commentDataArrayList.addAll(newItem);
        Log.i(tag, "현재 item 위치 : " + commentDataArrayList + ", 마지막 item 위치 : " + currentItemCount);

    }

    // 서버에서 해당 게시글의 댓글 데이터를 가져와야 함
    private void getPostData(){
        cTitle.setText(getIntent().getStringExtra("contentTitle"));
    }


    // 댓글창 recyclerview 어댑터 초기화
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommunityContent.this);
        recyclerView.setLayoutManager(linearLayoutManager); // layoutManager 설정
        commentAdapter = new CommentAdapter(CommunityContent.this, commentDataArrayList);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setItemAnimator(null); // 애니메이션 효과 제거
    }

    // 댓글 입력창에 댓글 입력한 걸 recyclerview 댓글창에 추가하기
    private void addComment(){
        String comment = editText.getText().toString();     // 댓글 입력한 문자열 얻음
        String nickname = getIntent().getStringExtra("contentNickname"); // 서버에 연결해서 데이터 받고 원래는 사용자 본인 닉네임이 떠야 함
        String level = getIntent().getStringExtra("contentPlace");
        String date = getIntent().getStringExtra("contentPlace");

        commentDataArrayList.add(new CommentData(nickname, level, date, comment));
        commentAdapter.notifyItemInserted(commentDataArrayList.size());
        editText.setText(null);
        final InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comment_menu, menu);
        return true;
    }

    // 댓글의 수정, 삭제 버튼 (이게 아니라 일단 어댑터에서 설정한 popUpMenu 사용할 거임), 일단 해당 게시글의 해시값만 있으면 될 듯?

    // 게시글 수정
    private void postModify(){
        Log.i(tag, "현재 게시글의 해시값 : ");
        Intent intent = new Intent(CommunityContent.this, CommunityEdit.class);
//                intent.putExtra("post_hash", hash);  해시값 전달
        startActivity(intent);
    }

    private void postDelete(){
        AlertDialog dialog = new AlertDialog.Builder(CommunityContent.this)
                .setMessage("게시글을 삭제하시겠습니까?")
                .setPositiveButton("확인", (dialog1, which) -> {
                    Log.i(tag, "게시글 삭제O");  // 서버로 함수(해시값) 해서 해댱 해시값에 해당하는 글 삭제, 게시글의 데이터 삭제 요청
                    finish();
                })
                .setNeutralButton("취소", (dialog2, which) -> dialog2.dismiss())
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(android.R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(android.R.color.black));
    }
}
