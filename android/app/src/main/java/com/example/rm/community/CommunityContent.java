package com.example.rm.community;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rm.R;
import com.example.rm.token.TokenManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommunityContent extends AppCompatActivity {
    // 레이아웃
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    CircleIndicator3 indicator3;
    ImageView btnBack;
    ImageView likeImage, commentImage;  // 좋아요 아이콘, 댓글 이동 아이콘
    TextView cNickname, cLevel, cDate, cTitle, cContent, cLike, cView;    // 닉네임, 등급, 생성날짜, 게시글 제목, 게시글 내용, 좋아요 개수, 조회수 개수
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
        cLike = findViewById(R.id.like_counts);
        cView = findViewById(R.id.view_count);
        viewPager2 = findViewById(R.id.viewpager);
        indicator3 = findViewById(R.id.indicator);
        likeImage = findViewById(R.id.heart);
        commentImage = findViewById(R.id.chat);
        toolbar = findViewById(R.id.toolbar);
        btnBack.setOnClickListener(v -> finish());
        PreferenceHelper.init(CommunityContent.this);

        String postId = getIntent().getStringExtra("postId");

        // 좋아요 버튼
        setLike(postId);
        likeImage.setOnClickListener(v -> currentLike(postId));

        // 게시글 수정, 삭제
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.inflateMenu(R.menu.comment_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_delete:    // 게시글 삭제
                    postDelete(postId);
                    return true;
                case R.id.action_edit:      // 게시글 수정
                    postModify(postId);
                    return true;
                default:
                    return CommunityContent.super.onOptionsItemSelected(item);
            }
        });


        String hash = getIntent().getStringExtra("community_post_hash");

        getTextData(hash);  // 게시글 상세 내용
        increaseViewCount(hash); // 조회수 증가
    }

    // 텍스트 데이터 얻음
    private void getTextData(String hash) {
        OkHttpClient client = new OkHttpClient();
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hash", hash);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("http://ipark4.duckdns.org:58395/api/read/writing/list/post")
                .post(body)
                .addHeader("Authorization", tokenManager.getToken())
                .addHeader("Device-Info", Build.MODEL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(tag, "서버 연결 실패", e);
                runOnUiThread(() -> Toast.makeText(CommunityContent.this, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject responseObject = new JSONObject(responseBody);
                        JSONObject messageObject = responseObject.getJSONObject("message");

                        runOnUiThread(() -> {
                            try {
                                cNickname.setText(messageObject.getString("nickname"));
                                cLevel.setText(messageObject.getString("place"));
                                cDate.setText(messageObject.getString("createTime"));
                                cTitle.setText(messageObject.getString("title"));
                                cContent.setText(messageObject.getString("contentText"));
                                cLike.setText(messageObject.getString("thumbsUp"));
                                cView.setText(messageObject.getString("views"));
                                String date = messageObject.getString("createTime");
                                cDate.setText(date.split("T")[0]);

                                JSONArray imageArray = messageObject.getJSONArray("images");
                                String[] directories = new String[imageArray.length()];
                                String[] files = new String[imageArray.length()];
                                for (int i = 0; i < imageArray.length(); i++) {
                                    JSONObject image = imageArray.getJSONObject(i);
                                    directories[i] = image.getString("fileLocation");
                                    files[i] = image.getString("name");
                                    Log.e("디렉터리 파일", directories[i] + files[i]);
                                }
                                getImageData(directories, files);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (JSONException e) {
                        Log.e(tag, "JSON 파싱 오류", e);
                    }
                } else {
                    Log.e(tag, "서버 응답 오류: " + response.body().string());
                }
            }
        });
    }


    // 이미지 데이터 출력
    private void getImageData(String[] directories, String[] files) {
        OkHttpClient client = new OkHttpClient();
        TokenManager tokenManager = new TokenManager(getApplicationContext());

        for (int i = 0; i < directories.length; ++i) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("directory", directories[i]);
                jsonObject.put("file", files[i]);
                Log.e("디렉터리 파일", directories[i] + files[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/read/image")
                    .post(body)
                    .addHeader("Authorization", tokenManager.getToken())
                    .addHeader("Device-Info", Build.MODEL)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(tag, "이미지 서버 연결 실패", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        byte[] imageBytes = response.body().bytes();
                        Bitmap bitmap = decodeSampledBitmapFromBytes(imageBytes, 1080, 1920);  // 원하는 크기로 디코딩
                        runOnUiThread(() -> {
                            bitmapArrayList.add(bitmap);
                            viewPagerAdapter.notifyDataSetChanged();
                        });
                    } else {
                        Log.e(tag, "이미지 서버 응답 오류: " + response.body().toString());
                    }
                }
            });
        }
        setupViewPager(bitmapArrayList);
    }

    // 이미지 사이즈 줄임
    private Bitmap decodeSampledBitmapFromBytes(byte[] bytes, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        Log.e(tag, "이미지 사이즈 조절4");
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    // 뷰페이저 설정
    private void setupViewPager(ArrayList<Bitmap> bitmaps) {
        viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), bitmaps);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        indicator3.setViewPager(viewPager2);
    }

    private void decodeBase64toBitmap(String base64) throws IOException {
        try {
            byte[] decodeBytes = Base64.decode(base64, Base64.NO_WRAP);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length, options);
            options.inSampleSize = calculateInSampleSize(options);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length);
            bitmapArrayList.add(bitmap);
            Log.d(tag, "base64 -> bitamp 디코딩 성공");
        } catch (Exception e) {
            Log.e(tag, "base64 -> bitamp 디코딩 실패", e);
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options) {
        CommunityEdit communityEdit = new CommunityEdit();
        int MAX_WIDTH = communityEdit.dpToPx(getApplicationContext(), 384);
        int MAX_HEIGHT = communityEdit.dpToPx(getApplicationContext(), 250);
        int height = options.outHeight;
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
    private void setLike(String postId) {
        boolean likeState = PreferenceHelper.getLikeState(postId); // 좋아요 상태 정보 가져옴 (눌림-true, 안눌림-false)
        updateLikeImage(likeState);
        int likeCount = PreferenceHelper.getLikeCount(postId); // 좋아요 개수 정보 가져옴
        cLike.setText(String.valueOf(likeCount));
    }

    // 현재 좋아요 정보
    private void currentLike(String postId) {
        boolean currentLikeState = PreferenceHelper.getLikeState(postId);
        int currentLikeCount = Integer.parseInt(cLike.getText().toString());
        setLikeState(postId, currentLikeState, currentLikeCount);
    }

    // 좋아요 클릭 시 상태/개수 변경
    private void setLikeState(String postId, boolean likeState, int count) {
        int likeCount = count;

        if (likeState) { // likestate가 true, 즉 눌린 상태에서 또 누른거니까 취소
            PreferenceHelper.likeState(postId, false);
            updateLikeImage(false);
            likeCount -= 1;
            PreferenceHelper.likeCount(postId, likeCount);
        } else {
            PreferenceHelper.likeState(postId, true);
            updateLikeImage(true);
            likeCount += 1;
            PreferenceHelper.likeCount(postId, likeCount);
        }
        cLike.setText(String.valueOf(likeCount));
        updateLikeCountOnServer(postId, likeCount); // 서버에서 새로운 좋아요 개수 업데이트
    }

    // 좋아요 상태 정보에 따라 하트 이미지 달라짐
    private void updateLikeImage(boolean state) {
        if (state) {  // true, 즉 좋아요가 눌린 상태
            likeImage.setImageResource(R.drawable.community_fill_heart);
        } else {
            likeImage.setImageResource(R.drawable.community_empty_heart);
        }
    }


    // 좋아요 개수 서버 업데이트
    private void updateLikeCountOnServer(String hash, int likeCount) {
        OkHttpClient client = new OkHttpClient();
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hash", hash);
            jsonObject.put("likeCount", likeCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("http://ipark4.duckdns.org:58395/api/update/like")
                .post(body)
                .addHeader("Authorization", tokenManager.getToken())
                .addHeader("Device-Info", Build.MODEL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(tag, "서버 연결 실패", e);
                runOnUiThread(() -> Toast.makeText(CommunityContent.this, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(tag, "서버 응답 오류: " + response.body().string());
                }
            }
        });
    }

    // 조회수 증가
    private void increaseViewCount(String hash) {
        OkHttpClient client = new OkHttpClient();
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hash", hash);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("http://ipark4.duckdns.org:58395/api/increase/view")
                .post(body)
                .addHeader("Authorization", tokenManager.getToken())
                .addHeader("Device-Info", Build.MODEL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(tag, "서버 연결 실패", e);
                runOnUiThread(() -> Toast.makeText(CommunityContent.this, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i(tag, "조회수 증가 성공");
                    runOnUiThread(() -> {
                        int currentViewCount = Integer.parseInt(cView.getText().toString());
                        cView.setText(String.valueOf(currentViewCount + 1));
                    });
                } else {
                    Log.e(tag, "서버 응답 오류: " + response.body().string());
                }
            }
        });
    }

    // 툴바 상단 버튼 (게시글 수정/삭제)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comment_menu, menu);
        return true;
    }

    // 게시글 수정
    private void postModify(String postId) {
        Log.i(tag, "현재 게시글의 해시값 : " + postId);
        Intent intent = new Intent(CommunityContent.this, CommunityEdit.class);
        intent.putExtra("postId", postId);  // 해시값 전달
        startActivity(intent);
    }

    private void postDelete(String postId) {
        AlertDialog dialog = new AlertDialog.Builder(CommunityContent.this)
                .setMessage("게시글을 삭제하시겠습니까?")
                .setPositiveButton("확인", (dialog1, which) -> {
                    Log.i(tag, "게시글 삭제O");
                    deletePostFromServer(postId);
                })
                .setNeutralButton("취소", (dialog2, which) -> dialog2.dismiss())
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(android.R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(android.R.color.black));
    }

    private void deletePostFromServer(String postId) {
        OkHttpClient client = new OkHttpClient();
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("postId", postId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("http://ipark4.duckdns.org:58395/api/delete/post")
                .post(body)
                .addHeader("Authorization", tokenManager.getToken())
                .addHeader("Device-Info", Build.MODEL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(tag, "서버 연결 실패", e);
                runOnUiThread(() -> Toast.makeText(CommunityContent.this, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i(tag, "게시글 삭제 성공");
                    runOnUiThread(() -> finish());
                } else {
                    Log.e(tag, "서버 응답 오류: " + response.body().string());
                }
            }
        });
    }

    // 댓글 클래스
    public static class CommentBottomSheet extends BottomSheetDialogFragment {
        private static final String tag = "댓글 클래스";

        @Override
        public void onStart() {
            super.onStart();
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
            if (dialog != null) {
                FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                WindowManager wm = getActivity().getWindowManager();
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getRealSize(size);
                int screenHeight = size.y;
                bottomSheet.getLayoutParams().height = screenHeight;
                bottomSheet.setLayoutParams(bottomSheet.getLayoutParams());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetBehavior.setDraggable(false);
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.community_comment_page, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ImageView btnClose = view.findViewById(R.id.btn_close_);
            RecyclerView recyclerView1 = view.findViewById(R.id.comment_recyclerview);
            EditText editText1 = view.findViewById(R.id.cc_edit_comment);
            ImageView sendComment = view.findViewById(R.id.send_comment);
            btnClose.setOnClickListener(v -> dismiss());
        }
    }

    // PreferenceHelper 클래스
    public static class PreferenceHelper {
        private static SharedPreferences sharedPreferences;
        private static SharedPreferences.Editor editor;

        public static void init(Context context) {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences("like_pref", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
            }
        }

        public static void likeState(String postId, boolean state) {
            editor.putBoolean(postId + "_liked", state);
            editor.apply();
        }

        public static boolean getLikeState(String postId) {
            return sharedPreferences.getBoolean(postId + "_liked", false);
        }

        public static void likeCount(String postId, int count) {
            editor.putInt(postId + "_like_count", count);
            editor.apply();
        }

        public static int getLikeCount(String postId) {
            return sharedPreferences.getInt(postId + "_like_count", 0);
        }
    }
}
