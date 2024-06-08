package com.example.rm.community;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rm.R;
import com.example.rm.token.PreferenceHelper;
import com.example.rm.token.TokenManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommunityContent extends AppCompatActivity {
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    CircleIndicator3 indicator3;
    ImageView btnBack;
    ImageView likeImage, commentImage;  // 좋아요 아이콘, 댓글 이동 아이콘
    TextView cNickname, cLevel, cDate, cTitle, cContent, cLike, cView;    // 닉네임, 등급, 생성날짜, 게시글 제목, 게시글 내용, 좋아요 개수, 조회수 개수
    Toolbar toolbar;
    //
    private static final String tag = "상세 게시글 화면";
    private static int currentItemCount = -1;
    private int itemPosition = RecyclerView.NO_POSITION, statusCount = -1;    // item 초기값
    private String postTitle, postContent;
    private JSONArray postImageArray;
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();    // viewpager의 이미지를 담을 bitmap 리스트

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
        String postHash = getIntent().getStringExtra("community_post_hash");

//        String postTitle = cTitle.getText().toString();
//        String postContent = cContent.getText().toString();
//        JSONArray postImageArray;

        // 게시글 수정, 삭제 팝업 메뉴
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.inflateMenu(R.menu.comment_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            String currentUserId = PreferenceHelper.getLoginId(context);    // 로그인 시 사용된 아이디
            String postUserId = getIntent().getStringExtra("community_post_userId");     // 게시글을 작성한 아이디
            Log.e(tag, "게시글 작성한 아이디 : " + postUserId + ", 로그인 시 사용된 아이디 : " + currentUserId);
            if(!currentUserId.equals(postUserId)) {
                Log.d(tag, "아이디 불일치, 본인이 작성한 게시글X -> 메뉴 버튼 안눌림");
                return false;
            }
            switch (item.getItemId()) {
                case R.id.action_delete:    // 게시글 삭제
                    messageDeletePost(postHash);
                    return true;
                case R.id.action_edit:      // 게시글 수정
                    Log.e(tag, "여기에" + postHash + " 값은?");
                    getModifyPostContent(postHash);
//                    Log.e(tag, "여기에" + postHash + " 값은?");
//                    Log.e("postTitle", postTitle);
//                    Log.e("postContent", postContent);
//                    Log.e("postImageArray", postImageArray.toString());
//                    postModify(postHash, postTitle, postContent, postImageArray);
                    return true;
                default:
                    return CommunityContent.super.onOptionsItemSelected(item);
            }
        });

        // 말풍선 아이콘 클릭하면 댓글창 열림
        commentImage.setOnClickListener(view -> {
            String post_hash = getIntent().getStringExtra("community_post_hash");
            CommentBottomSheet commentBottomSheet = CommentBottomSheet.newInstance(post_hash);
            commentBottomSheet.show(getSupportFragmentManager(), "댓글창");
        });

        // 좋아요 버튼
        likeImage.setOnClickListener(v -> {
            updateLike(postHash);
        });

        // 게시글 상세 내용
        getTextData(postHash);
        setupViewPager();

        // 좋아요
        likeState(postHash);
        getLikeCount(postHash);

        // 조회수
        getViews(postHash);
        updateViews(postHash);
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
                                String date = messageObject.getString("createTime");
                                cDate.setText(date.split("T")[0]);
                                JSONArray imageArray = messageObject.getJSONArray("images");

                                for (int i = 0; i < imageArray.length(); i++) {
                                    JSONObject image = imageArray.getJSONObject(i);
                                    String directory = image.getString("fileLocation");
                                    String file = image.getString("name");
                                    Log.e("디렉터리 파일", directory + file);
                                    getImageData(directory, file);
                                }
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
    private void getImageData(String directories, String files) {
        OkHttpClient client = new OkHttpClient();
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("directory", directories);
            jsonObject.put("file", files);
            Log.e("디렉터리 파일", directories + files);
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

    // 이미지 사이즈 줄임
    private Bitmap decodeSampledBitmapFromBytes(byte[] bytes, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    // 이미지 사이즈 줄임2
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
    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), bitmapArrayList);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        indicator3.setViewPager(viewPager2);
    }

    // 좋아요 상태 정보에 따라 하트 이미지 달라짐
    private void updateLikeImage(boolean state) {
        if (state) {  // true, 즉 좋아요가 눌린 상태
            likeImage.setImageResource(R.drawable.community_fill_heart);
        } else {
            likeImage.setImageResource(R.drawable.community_empty_heart);
        }
    }

    // 초기 좋아요 상태
    private void likeState(String hash){
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            TokenManager tokenManager = new TokenManager(context.getApplicationContext());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("hash", hash);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/read/writing/info/internal/like/first")
                    .post(body)
                    .addHeader("Authorization", tokenManager.getToken())
                    .addHeader("Device-Info", Build.MODEL)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    String isYes = new JSONObject(responseBody).getString("message");
                    updateLikeImage(isYes.equals("yes"));   // 받은 결과값이 yes이면 하트가 눌린 상태로 변함
                    Log.e("좋아요 버튼, likeState", "초기 좋아요 상태 : " + responseBody);
                } else {
                    Log.e("실패, likeState", "이유는 " + responseBody);
                }
            } catch (IOException | JSONException e) {
                Log.e(tag, "오류, likeState", e);
            }
        }).start();
    }

    // 초기 좋아요 개수
    private void getLikeCount(String hash){
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            TokenManager tokenManager = new TokenManager(context.getApplicationContext());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("hash", hash);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/read/writing/info/internal/like")
                    .post(body)
                    .addHeader("Authorization", tokenManager.getToken())
                    .addHeader("Device-Info", Build.MODEL)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    String count = new JSONObject(responseBody).getString("message");   // 초기 좋아요 개수 가져옴
                    runOnUiThread(() -> {
                        cLike.setText(count);
//                        statusCount = Integer.parseInt(count);
                        Log.e("좋아요 개수, getLikeCount", "좋아요 개수 : " + count + "  " + responseBody);
                    });
                } else {
                    Log.e("좋아요 개수 실패, getLikeCount", "이유는 " + responseBody);
                }
            } catch (IOException | JSONException e) {
                Log.e(tag, "좋아요 개수 오류, getLikeCount", e);
            }
        }).start();
    }

    // 좋아요 개수 업데이트
    private void updateLike(String hash){
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            TokenManager tokenManager = new TokenManager(context.getApplicationContext());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("hash", hash);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/update/writing/info/internal/like")
                    .post(body)
                    .addHeader("Authorization", tokenManager.getToken())
                    .addHeader("Device-Info", Build.MODEL)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    String changeStatus = new JSONObject(responseBody).getString("message");
                    runOnUiThread(() -> {
                        updateLikeImage(changeStatus.equals("no2yes"));
                        getLikeCount(hash);
                        Log.e("좋아요 버튼, updateLike", "좋아요 개수 : "  + " yes2no-안누름 / no2yes-하트 누름 " + responseBody);
                    });
                } else {
                    Log.e("실패, updateLike", "이유는 " + responseBody);
                }
            } catch (IOException | JSONException e) {
                Log.e(tag, "오류, updateLike", e);
            }
        }).start();
    }

    // 초기 조회수 개수
    private void getViews(String hash){
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            TokenManager tokenManager = new TokenManager(context.getApplicationContext());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("hash", hash);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/read/writing/info/internal/view")
                    .post(body)
                    .addHeader("Authorization", tokenManager.getToken())
                    .addHeader("Device-Info", Build.MODEL)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    String views = new JSONObject(responseBody).getString("message");
                    runOnUiThread(() -> {
                        cView.setText(views);
                        Log.e("조회수 버튼", "조회수 개수 : " + views + "  " + responseBody);
                    });
                } else {
                    Log.e("조회수 개수 실패", "이유는 " + responseBody);
                }
            } catch (IOException | JSONException e) {
                Log.e(tag, "조회수 개수 오류", e);
            }
        }).start();
    }

    // 조회수 개수 업데이트
    private void updateViews(String hash){
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            TokenManager tokenManager = new TokenManager(context.getApplicationContext());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("hash", hash);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/update/writing/info/internal/view")
                    .post(body)
                    .addHeader("Authorization", tokenManager.getToken())
                    .addHeader("Device-Info", Build.MODEL)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    String changeStatus = new JSONObject(responseBody).getString("message");
                    runOnUiThread(() -> {
                        Log.e("조회수 버튼", "조회수 개수 : "  + changeStatus + responseBody);
                    });
                } else {
                    Log.e("실패", "이유는 " + responseBody);
                }
            } catch (IOException | JSONException e) {
                Log.e(tag, "오류", e);
            }
        }).start();
    }

    // 툴바 상단 버튼 (게시글 수정/삭제)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comment_menu, menu);
        return true;
    }

    // 게시글 수정 버튼 클릭 -> 해당 게시글의 해시값 넘기고 수정(=생성) 페이지로 넘어감

    // 게시글 수정 (이미지 가져옴)
    private void getModifyPostContent(String hash) {
            OkHttpClient client = new OkHttpClient();
            TokenManager tokenManager = new TokenManager(getApplicationContext());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("hash", hash);
                Log.e("hash 값을 넘겨줍니다.", hash);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/read/writing/info")
                    .post(body)
                    .addHeader("Authorization", tokenManager.getToken()) // String token = tokenManager.getToken();
                    .addHeader("Device-Info", Build.MODEL) // String deviceModel = Build.MODEL;
                    .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(tag, "서버 연결 실패", e);
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject responseObject = new JSONObject(responseBody);
                        JSONObject json = responseObject.getJSONObject("message");  // json 결과를 배열로 저장

                        postTitle = json.getString("title");
                        postContent = json.getString("contentText");
                        postImageArray = json.getJSONArray("images");

                        runOnUiThread(() -> {
                            postModify(hash, postTitle, postContent, postImageArray, bitmapArrayList);
                        });
                    } catch (JSONException e) {
                        Log.e(tag, "오류 발생", e);
                    }
                } else {
                    Log.e(tag, "서버 응답 오류: " + response.message());
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "서버 응답 오류", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void sendImageURLss(ArrayList<Bitmap> bitmaps, Context context) {
        ArrayList<Uri> uriList = new ArrayList<>();
        for (Bitmap bitmap : bitmaps) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "test", null);
            Uri imageUri = Uri.parse(path);
            uriList.add(imageUri);
        }
        Intent intent = new Intent(CommunityContent.this, CommunityEdit.class);
        intent.putExtra("modify_post_image_uri", uriList);
        startActivity(intent);
    }

    private void postModify(String postHash, String postTitle, String postContent, JSONArray postImageArray, ArrayList<Bitmap> bitmaps) {

        Log.e("비트맵에 데이터 있는지 확인", String.valueOf(bitmaps));
        ArrayList<Uri> uriList = new ArrayList<>();
        for (Bitmap bitmap : bitmaps) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "test", null);
            Uri imageUri = Uri.parse(path);
            uriList.add(imageUri);
        }

        Intent intent = new Intent(CommunityContent.this, CommunityEdit.class);
        intent.putExtra("modify_post_image_uri", uriList);
        intent.putExtra("modify_post_hash", postHash);
        intent.putExtra("modify_post_title", postTitle);
        intent.putExtra("modify_post_content", postContent);
        intent.putExtra("modify_post_image_Array", postImageArray.toString());
        startActivity(intent);
    }

    //////////////////////////////////////////////////////////

    // 게시글 삭제 버튼 클릭 -> 메시지 띄운 뒤에 서버에서 게시글 삭제
    private void messageDeletePost(String hash) {
        AlertDialog dialog = new AlertDialog.Builder(CommunityContent.this)
                .setMessage("게시글을 삭제하시겠습니까?")
                .setPositiveButton("확인", (dialog1, which) -> {
                    deletePost(hash);
                })
                .setNegativeButton("취소", (dialog2, which) -> dialog2.dismiss())
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(android.R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(android.R.color.black));
    }

    // 서버에 저장된 게시글 삭제
    private void deletePost(String hash){
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            TokenManager tokenManager = new TokenManager(context.getApplicationContext());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("hash", hash);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/delete/writing/info")
                    .post(body)
                    .addHeader("Authorization", tokenManager.getToken())
                    .addHeader("Device-Info", Build.MODEL)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(context.getApplicationContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("게시글 삭제", "게시글 삭제 성공");
                        Intent intent = new Intent(CommunityContent.this, Community.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    Log.e("게시글 삭제 실패", "이유는 " + responseBody);
                }
            } catch (IOException e) {
                Log.e(tag, "게시글 삭제 오류", e);
            }
        }).start();
    }

    // 댓글 클래스
    public static class CommentBottomSheet extends BottomSheetDialogFragment {
        private static final String tag = "댓글 클래스";
        CommentAdapter commentAdapter;
        RecyclerView recyclerView;
        EditText editText;
        ImageView sendComment;
        //
        ArrayList<CommentData> commentDataArrayList = new ArrayList<>();    // 댓글 데이터 담음
        private String postHash; // post_hash 값을 저장할 변수

        public static CommentBottomSheet newInstance(String postHash) {
            CommentBottomSheet fragment = new CommentBottomSheet();
            Bundle args = new Bundle();
            args.putString("community_post_hash", postHash);
            fragment.setArguments(args);
            Log.e(tag, "넘겨받은 해시값" + postHash);
            return fragment;
        }
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                postHash = getArguments().getString("community_post_hash");
                Log.e(tag, "넘겨받은 해시값" + postHash);
            }
        }

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
            recyclerView = view.findViewById(R.id.comment_recyclerview);
            editText = view.findViewById(R.id.cc_edit_comment);
            sendComment = view.findViewById(R.id.send_comment);
            sendComment.setOnClickListener(view1 -> {
                sendComments(editText.getText().toString()); // 댓글 서버에 전송
                InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            });
            getCommentData(postHash);
            setRecyclerView();
        }

        // 댓글창 초기화
        private void setRecyclerView(){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            commentAdapter = new CommentAdapter(getContext(), commentDataArrayList);
            recyclerView.setAdapter(commentAdapter);
            recyclerView.setItemAnimator(null);
        }

        // 전체 댓글창 데이터 가져옴
        private void getCommentData(String postHash) {
            new Thread(() -> {
                OkHttpClient client = new OkHttpClient();
                TokenManager tokenManager = new TokenManager(getContext());
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("type", "comment");
                    jsonObject.put("whichWriting", postHash);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                String token = tokenManager.getToken();
                String deviceModel = Build.MODEL;
                Request request = new Request.Builder()
                        .url("http://ipark4.duckdns.org:58395/api/read/writing/list")
                        .post(body)
                        .addHeader("Authorization", token)
                        .addHeader("Device-Info", deviceModel)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    JSONObject responseObject = new JSONObject(responseBody);
                    JSONArray jsonArray = responseObject.getJSONArray("message");  // json 결과를 배열로 저장
                    List<CommentData> allComments = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonO = jsonArray.getJSONObject(i);
                        String contentText = jsonO.getString("contentText");   // 댓글 내용
                        String author = jsonO.getString("author"); // 아이디
                        String nickname = jsonO.getString("nickname"); // 닉네임
                        String place = jsonO.getString("place");    // 등급
                        String hash = jsonO.getString("hash");  // 댓글의 해시값
                        String createTime = jsonO.getString("createTime"); // 생성날짜
                        String date = createTime.split("T")[0];

                        Log.i(tag, "댓글 : " + contentText);
                        allComments.add(new CommentData(nickname, place, date, contentText, author, hash));
                    }
                    getActivity().runOnUiThread(() -> {
                        commentDataArrayList.addAll(allComments);
                        commentAdapter.notifyDataSetChanged();
                        Log.i(tag, "댓글 전송 성공!" + responseBody);
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }

            }).start();
        }

        // 댓글창에 입력한 문자열 서버로 전송
        private void sendComments(String comment) {
            OkHttpClient client = new OkHttpClient();
            TokenManager tokenManager = new TokenManager(getContext());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("type", "comment")
                    .addFormDataPart("contentText", comment)
                    .addFormDataPart("whichWriting", postHash);

            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url("http://ipark4.duckdns.org:58395/api/create/users/writings")
                    .post(requestBody)
                    .addHeader("Authorization", tokenManager.getToken())
                    .addHeader("Device-Info", Build.MODEL)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(tag, "서버 연결 실패", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Log.e(tag, "댓글 작성 완료 " + response.body().toString());
                        getActivity().runOnUiThread(() -> {
                            commentDataArrayList.clear();
                            getCommentData(postHash);
                            editText.setText("");
                        });
                    } else {
                        Log.e(tag, "서버 응답 오류: " + response.message());
                    }
                }
            });
        }
    }
}