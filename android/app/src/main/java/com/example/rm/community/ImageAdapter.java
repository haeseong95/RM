package com.example.rm.community;

import static android.content.Intent.getIntent;
import static com.example.rm.token.ApiClient.runOnUiThread;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rm.R;
import com.example.rm.token.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// 커뮤니티 글쓰기에서 이미지 5장 추가하기
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    static final String tag = "이미지 어댑터";
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    private ArrayList<String> files;    // 서버에서 가져온 파일 위치 저장함
    private Context context;
    private Runnable updateImageCount;

    public ImageAdapter(ArrayList<Uri> list, Context context, Runnable updateImageCount) {    // 게시글을 생성할 때 쓸 거
        this.uriArrayList = list;
        this.context = context;
        this.updateImageCount = updateImageCount;
    }

    public ImageAdapter(ArrayList<String> files){
        this.files = files;
        getFiles(files);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {    // 데이터 -> 뷰 전환
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_listview_image, parent, false);
//        ImageViewHolder viewHolder = new ImageViewHolder(view);
        return new ImageViewHolder(view);
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri uri = uriArrayList.get(position);
        Glide.with(context).load(uri).into(holder.imageView);

        // 로컬에서 이미지 로드
        if ("content".equals(uri.getScheme())) {  // content 스킴인 경우, 로컬에서 이미지 로드
            Glide.with(context).load(uri).into(holder.imageView);
            Log.e(tag, "파일 로케이션 값 :dzfgasgsfgbasfg ");
        } else {
            Glide.with(context).load(uri).into(holder.imageView);
            Log.e(tag, "파일 로케이션 값 : else 부분 출력함  ");



            if (files != null) {
                for (int i=0; i<files.size(); i++){
                    String fileLocation = files.get(i);
                    Log.e(tag, "파일 로케이션 값 : " + fileLocation);
                    fetchImageData(uri.toString(), holder.imageView, fileLocation);
                }

            }
        }

        holder.delete.setOnClickListener(v -> {
            int adapterPosition = holder.getAbsoluteAdapterPosition();  // 현재 아이템 위치 가져옴
            if (adapterPosition != RecyclerView.NO_POSITION){
                uriArrayList.remove(adapterPosition);  // 리스트 위치의 데이터 삭제
                notifyItemRemoved(adapterPosition);    // 리사이클뷰의 ui 항목 제거 알림
                notifyItemRangeChanged(adapterPosition, uriArrayList.size());  // 제거하면 다른 목록들의 위치 업데이트
                if(updateImageCount != null){
                    updateImageCount.run();
                }
                Log.i(tag + " X 버튼", "X버튼 클릭 시 목록 위치 업데이트 성공");
            }
        });
    }

    @Override
    public int getItemCount() {     // 전체 데이터 개수(리스트 크기) 반환
        if(uriArrayList == null){
            Log.i(tag + " uri 이미지 개수", "아이템 비어있음");
            return 0;
        } else {
            Log.i(tag + " uri 이미지 개수", "아이템 개수 : " + uriArrayList.size());
            return uriArrayList.size();
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{    // 아이템 뷰를 저장하는 뷰홀더 클래스
        ImageView imageView;
        Button delete;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.images);
            delete = itemView.findViewById(R.id.btn_image_delete);
            delete.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0000FF")));
        }
    }

    private void getFiles(ArrayList<String> files){
        Log.i("ㄴㅇㄹ니얾 어댑터", String.valueOf(files));

        for (int i=0; i<files.size(); i++){
            String fileLocation = files.get(i);
            Log.i("ㄴㅇㄹ니얾 sdfsdf어댑터", fileLocation);
        }

    }

    private void fetchImageData(String uri, ImageView imageView, String fileLocation) {
        OkHttpClient client = new OkHttpClient();
        TokenManager tokenManager = new TokenManager(context);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("directory", fileLocation);
            jsonObject.put("file", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
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
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    runOnUiThread(() -> Glide.with(context).load(bitmap).into(imageView));
                } else {
                    Log.e(tag, "이미지 서버 응답 오류: " + response.body().toString());
                }
            }
        });
    }
}