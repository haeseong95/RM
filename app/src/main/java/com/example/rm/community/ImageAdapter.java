package com.example.rm.community;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
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

import java.util.ArrayList;

// 커뮤니티 글쓰기에서 이미지 5장 추가하기
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    static final String tag = "ImageAdapter";
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    private Context context;
    private Runnable updateImageCount;

    public ImageAdapter(ArrayList<Uri> list, Context context, Runnable updateImageCount) {    // 생성자는 데이터 리스트 객체, context를 전달받음
        this.uriArrayList = list;
        this.context = context;
        this.updateImageCount = updateImageCount;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {    // 데이터 -> 뷰 전환
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_listview_image, parent, false);
        ImageViewHolder viewHolder = new ImageViewHolder(view);
        return viewHolder;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri uri = uriArrayList.get(position);
        Glide.with(context).load(uri).into(holder.imageView);

        holder.delete.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();  // 현재 아이템 위치 가져옴
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
            delete.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
        }
    }
}