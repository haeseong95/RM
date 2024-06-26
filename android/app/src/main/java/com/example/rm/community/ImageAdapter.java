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
    private ArrayList<Uri> uriArrayList = new ArrayList<>();    // 현재 recyclerView에 들어간 이미지 uri 저장할 리스트

    private ArrayList<ArrayList<String>> deleteImage = new ArrayList<>();     // 삭제할 이미지 경로 저장
    private ArrayList<ArrayList<String>> serverUrl;    // 서버에서 가져온 파일 위치 저장함
    private Context context;
    private Runnable updateImageCount;

//    public ImageAdapter(ArrayList<Uri> list, Context context, Runnable updateImageCount) {    // 게시글을 생성할 때 쓸 거
//        this.uriArrayList = list;
//        this.context = context;
//        this.updateImageCount = updateImageCount;
//    }

    public ImageAdapter(ArrayList<Uri> list, ArrayList<ArrayList<String>> url, Context context, Runnable updateImageCount) {    // 게시글을 생성할 때 쓸 거
        this.uriArrayList = list;
        this.context = context;
        this.updateImageCount = updateImageCount;
        this.serverUrl = url;
    }

    public ArrayList<ArrayList<String>> getDeleteImage(){
        Log.e("getDeleteImage 개수", String.valueOf(deleteImage.size()));
        return deleteImage;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {    // 데이터 -> 뷰 전환
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_listview_image, parent, false);
        return new ImageViewHolder(view);
    }
    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Uri uri = uriArrayList.get(position);
        Glide.with(context).load(uri).into(holder.imageView);

        // 로컬에서 이미지 로드
        if ("content".equals(uri.getScheme())) {
            Glide.with(context).load(uri).into(holder.imageView);
            Log.e(tag, "파일 로케이션 값 :dzfgasgsfgbasfg ");
        } else {
            Glide.with(context).load(uri).into(holder.imageView);
            Log.e(tag, "파일 로케이션 값 : else 부분 출력함ffffffff  ");

//            if (files != null) {
//                for (int i=0; i<files.size(); i++){
//                    String fileLocation = files.get(i);
//                    Log.e(tag, "파일 로케이션 값을 어디서 가져오는 지 확인 차 도전 : " + fileLocation);
//                    fetchImageData(uri.toString(), holder.imageView, fileLocation);
//                }
//            }
        }

        holder.delete.setOnClickListener(v -> {
            int adapterPosition = holder.getAbsoluteAdapterPosition();  // 현재 아이템 위치 가져옴
            Log.e("uriArrayList 이건 뭐가 들어있나?", uriArrayList.get(position).toString());
            Log.e("adapterPosition 값 확인", String.valueOf(adapterPosition));
            if (adapterPosition != RecyclerView.NO_POSITION){
//                Log.e("files 값", files.toString());
////                else if(adapterPosition < files.size()) Log.e("adapterPosition < files.size()", String.valueOf(files.size()));
//                if(files != null && adapterPosition < files.size()){
//                    Log.i("files 값 있냐", "확인 좀 " + files.size());
//                    Log.e("deleteImage 하나 삭제 전, 길이", String.valueOf(deleteImage.size()));
//                    deleteImage.add(files.get(adapterPosition));    // 삭제할 이미지 경로 저장
//                    Log.e("deleteImage 하나 삭제 후, 길이", String.valueOf(deleteImage.size()));
//                }


                if( !serverUrl.isEmpty() && (adapterPosition < serverUrl.size()) ) {
                    Log.e("uriArrayList.remove 수행 전 내부 값", uriArrayList.toString());
                    deleteImage.add(serverUrl.remove(adapterPosition));
//                    uriArrayList.remove(adapterPosition);  // 리스트 위치의 데이터 삭제
                    Log.e("deleteImage 수행", deleteImage.toString());
                    Log.e("uriArrayList.remove 수행 후 내부 값", uriArrayList.toString());
                }

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

    // 게시글 수정 시 기존의 이미지 가져옴
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
                    Log.e("레스펀스 바디", response.body().toString());
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