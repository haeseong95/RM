package com.example.rm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

// 쓰레기 메인 설명을 나타내는 리스트뷰 어댑터
public class TrashAdapter2 extends ArrayAdapter<TrashMainListData> {
    public TrashAdapter2(@NonNull Context context, @NonNull ArrayList<TrashMainListData> arrayList2) {
        super(context, 0, arrayList2);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_main_listview, parent, false);
        }

        TextView mainName = convertView.findViewById(R.id.main_trashname);
        TextView mainDes = convertView.findViewById(R.id.main_description);
        ImageView mainImage = convertView.findViewById(R.id.main_image);

        TrashMainListData trashMainListData = getItem(position);
        mainName.setText(trashMainListData.getMainName());
        mainDes.setText(trashMainListData.getMainIfo());
        mainImage.setImageResource(trashMainListData.getMainImage());

        return convertView;
    }
}

// 쓰레기 메인 설명 받아올 거
class TrashMainListData {
    public int mainImage = 0;  // 쓰레기 이미지
    public String mainName = "";    // 쓰레기 이름
    public String mainIfo="";   // 쓰레기 분리수거 방법

    public int getMainImage() {
        return mainImage;
    }

    public void setMainImage(int mainImage) {
        this.mainImage = mainImage;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public String getMainIfo() {
        return mainIfo;
    }

    public void setMainIfo(String mainIfo) {
        this.mainIfo = mainIfo;
    }

    public TrashMainListData(int mainImage, String mainName, String mainIfo) {
        this.mainImage = mainImage;
        this.mainName = mainName;
        this.mainIfo = mainIfo;
    }
}