package com.example.rm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

// 쓰레기 품목 종류를 나타내는 리스트뷰 어댑터 (searchview, categoryInfo 품목 리스트 -> trashDetail)
public class TrashListAdapter extends ArrayAdapter<TrashListData> {

    private ArrayList<TrashListData> arrayList;

    public TrashListAdapter(@NonNull Context context, ArrayList<TrashListData> arrayList) {     // 현재 컨텍스트(nullX), 뷰 만들 때 사용할 레이아웃 리소스 파일
        super(context, 0, arrayList);
        this.arrayList = arrayList;
        Log.i("TrashList어댑터 (품목)", "어댑터 연결 성공" + arrayList.size());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {  // position(listview 항목 위치)에 위치한 데이터 -> 리스트 뷰의 아이템으로 만들어줌

        if (convertView == null){   // 화면에 없어진 view 재사용
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_listview, parent, false);
        }

        TextView listName = convertView.findViewById(R.id.list_trashName);  // 화면에 표시할 view 연결
        TextView listInfo = convertView.findViewById(R.id.list_trashInfo);
        ImageView listImage = convertView.findViewById(R.id.list_trashImage);

        TrashListData trashListData = getItem(position);        // positon에 데이터 추가해 해당 위치의 데이터 가져옴
        String imageUrl = trashListData.getTrashListImage();    // 이미지를 받을 때는 setimageResource는 리소스 id를 참고하는데 타입이 안맞으니 string에 저장해야 할듯
        listName.setText(trashListData.getTrashListName());     // 각 뷰에 대한 데이터 전달
        listInfo.setText(trashListData.getTrashListInfo());
        Glide.with(getContext()).load(imageUrl).encodeQuality(80).diskCacheStrategy(DiskCacheStrategy.ALL).into(listImage);
        return convertView;
    }

    public void updateListviewItem(ArrayList<TrashListData> newItem){  // 어댑터 내 데이터 변경 + 뷰 업데이트 (searchview 갱신 시 필요함)
        arrayList.clear();  // 데이터 초기화
        arrayList.addAll(newItem);  // 매개변수로 받은 ArrayList의 모든 요소를 추가(=업데이트)
        notifyDataSetChanged(); // listview와 연결된 어댑터에 new 데이터 반영함
    }
}

// 쓰레기 listview에서 보여지는 각 아이템 항목의 데이터 모델 정의함 (get 데이터 반환, set 데이터 설정)
class TrashListData {
    public String trashListImage = "";  // 쓰레기 이미지
    public String trashListName = "";   // 쓰레기 이름
    public String trashListInfo = "";   // 쓰레기 분리수거 방법

    public String getTrashListImage() {
        return trashListImage;
    }

    public void setTrashListImage(String trashListImage) {
        this.trashListImage = trashListImage;
    }

    public String getTrashListName() {
        return trashListName;
    }

    public void setTrashListName(String trashListName) {
        this.trashListName = trashListName;
    }

    public String getTrashListInfo() {
        return trashListInfo;
    }

    public void setTrashListInfo(String trashListInfo) {
        this.trashListInfo = trashListInfo;
    }


    //
    public TrashListData(String trashListImage, String trashListName, String trashListInfo) {
        this.trashListImage = trashListImage;
        this.trashListName = trashListName;
        this.trashListInfo = trashListInfo;
    }
}
