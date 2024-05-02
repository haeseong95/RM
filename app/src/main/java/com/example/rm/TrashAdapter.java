package com.example.rm;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

// Listview의 어댑터, Listview와 데이터 항목(TrashListData 객체 목록)-뷰 중간다리 역할
public class TrashAdapter extends ArrayAdapter<TrashListData> {

    ArrayList<TrashListData> arrayList = new ArrayList<>();

    public TrashAdapter(@NonNull Context context, ArrayList<TrashListData> arrayList) {     // 현재 컨텍스트(nullX), 뷰 만들 때 사용할 레이아웃 리소스 파일
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {  // position(listview 항목)에 위치한 데이터 -> 리스트 뷰의 아이템으로 만들어줌

        TrashListData trashListData = getItem(position);        // positon에 데이터 추가

        if (convertView == null){   // 화면에 없어진 view 재사용
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_listview, parent, false);
        }

        TextView listName = convertView.findViewById(R.id.list_trashName);  // 화면에 표시할 view 연결
        TextView listInfo = convertView.findViewById(R.id.list_trashInfo);
        ImageView listImage = convertView.findViewById(R.id.list_trashImage);

        listName.setText(trashListData.getTrashName());     // 각 뷰에 대한 데이터 전달
        listInfo.setText(trashListData.getTrashInfo());
        listImage.setImageResource(trashListData.getTrashImage());

        return convertView;
    }

    // 아이템에 데이터 추가
    public void add(int image, String name, String info){
        TrashListData trashListData = new TrashListData();

        trashListData.setTrashImage(image);
        trashListData.setTrashName(name);
        trashListData.setTrashInfo(info);

        arrayList.add(trashListData);
    }

}

// 쓰레기 listview에서 보여지는 각 아이템 항목의 데이터 모델 정의함 (get 데이터 반환, set 데이터 설정)
class TrashListData {
    public int trashImage = 0;  // 쓰레기 이미지
    public String trashName = "";   // 쓰레기 이름
    public String trashInfo = "";   // 쓰레기 분리수거 방법

    public int getTrashImage() {return trashImage;}
    public void setTrashImage(int trashImage) {this.trashImage = trashImage;}

    public String getTrashInfo() {return trashInfo;}
    public void setTrashInfo(String trashInfo) {this.trashInfo = trashInfo;}

    public String getTrashName(){return trashName;}
    public void setTrashName(String trashName){this.trashName = trashName;}
}
