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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

// 쓰레기 메인 설명을 나타내는 리스트뷰 어댑터
public class TrashAdapter2 extends ArrayAdapter<TrashMainListData> {
    private ArrayList<TrashMainListData> arrayList;

    public TrashAdapter2(@NonNull Context context, @NonNull ArrayList<TrashMainListData> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_main_listview, parent, false);
        }

        TextView mainName = convertView.findViewById(R.id.main_trashname);
        TextView mainDescription = convertView.findViewById(R.id.main_description);
        ImageView mainImage = convertView.findViewById(R.id.main_image);

        TrashMainListData trashMainListData = getItem(position);
        mainName.setText(trashMainListData.getMainName());
        mainDescription.setText(trashMainListData.getMainInfo());

        String imageUrl = trashMainListData.getMainImage();

        // 이미지 전송
        Glide.with(getContext())
                .load(imageUrl)   // with : 어떤 뷰에 넣을 지, load : 원하는 이미지의 URL(.getMainImage()로 사용하면 이미지 리소스 ID를 쓰는거라 아이템 전달 시 형식 안맞는듯
                .encodeQuality(80)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.LOW)
                .into(mainImage);   // 어떤 imageView에 넣어줄 지 정함

        /*
        // listview의 아이템 클릭 시 반응X (카테고리 메인 설명에 넣을 거임)
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
         */

        return convertView;
    }


    // 어댑터 내 데이터 변경 + 뷰 업데이트
    public void updateListviewItem(ArrayList<TrashMainListData> newItem) {
        arrayList.clear();      // 데이터 초기화
        arrayList.addAll(newItem);  // 매개변수로 받은 ArrayList의 모든 요소를 추가(=업데이트)
        notifyDataSetChanged(); // listview와 연결된 어댑터에 new 데이터 반영함
    }
}

// 쓰레기 메인 설명 받아올 거
class TrashMainListData {
    public String mainImage = "";  // 쓰레기 이미지, 이미지 url을 저장하므로 String임
    public String mainName = "";    // 쓰레기 이름
    public String mainInfo="";   // 쓰레기 분리수거 방법

    public String getMainImage() {return mainImage;}
    public void setMainImage(String mainImage) {this.mainImage = mainImage;}

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public String getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(String mainIfo) {
        this.mainInfo = mainIfo;
    }

    public TrashMainListData(String mainImage, String mainName) {
        this.mainImage = mainImage;
        this.mainName = mainName;
    }
}