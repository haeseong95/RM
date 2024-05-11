package com.example.rm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

// 쓰레기 메인 설명을 나타내는 리스트뷰 어댑터
public class TrashMainAdapter extends ArrayAdapter<TrashMainListData> {
    private ArrayList<TrashMainListData> arrayList;

    public TrashMainAdapter(@NonNull Context context, @NonNull ArrayList<TrashMainListData> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
        Log.i("TrashMain 어댑터 (메인 설명)", "어댑터 연결 성공" + arrayList.size());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_main_listview, parent, false);
        }

        TextView mainName = convertView.findViewById(R.id.main_trashname);
        TextView mainInfo = convertView.findViewById(R.id.main_description);
        ImageView mainImage = convertView.findViewById(R.id.main_image);

        TrashMainListData trashMainListData = getItem(position);
        mainName.setText(trashMainListData.getTrashMainName());
        mainInfo.setText(trashMainListData.getTrashMainInfo());
        String imageUrl = trashMainListData.getTrashMainImage();
        Glide.with(getContext())
                .load(imageUrl)   // with : 어떤 뷰에 넣을 지, load : 원하는 이미지의 URL(.getMainImage()로 사용하면 이미지 리소스 ID를 쓰는거라 아이템 전달 시 형식 안맞는듯
                .encodeQuality(80)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.LOW)
                .into(mainImage);   // 어떤 imageView에 넣어줄 지 정함

        // listview의 아이템 클릭 시 반응X (카테고리 메인 설명에 필요)
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        return convertView;
    }
}

// 쓰레기 메인 설명 받아올 거
class TrashMainListData {
    public String trashMainImage = "";  // 쓰레기 이미지, 이미지 url을 저장하므로 String임
    public String trashMainName = "";    // 쓰레기 이름
    public String trashMainInfo ="";   // 쓰레기 분리수거 방법

    public String getTrashMainImage() {
        return trashMainImage;
    }

    public void setTrashMainImage(String trashMainImage) {
        this.trashMainImage = trashMainImage;
    }

    public String getTrashMainName() {
        return trashMainName;
    }

    public void setTrashMainName(String trashMainName) {
        this.trashMainName = trashMainName;
    }

    public String getTrashMainInfo() {
        return trashMainInfo;
    }

    public void setTrashMainInfo(String trashMainInfo) {
        this.trashMainInfo = trashMainInfo;
    }

    public TrashMainListData(String trashMainImage, String trashMainName, String trashMainInfo) {
        this.trashMainImage = trashMainImage;
        this.trashMainName = trashMainName;
        this.trashMainInfo = trashMainInfo;
    }
}