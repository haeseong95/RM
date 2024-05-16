package com.example.rm.community;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rm.R;

import java.util.ArrayList;


public class CommunityImageAdapter extends BaseAdapter {

    private Context context;    // 앱 컨텍스트를 담는 필드
    private LayoutInflater layoutInflater;      // 레이아웃 전개자 객체를 담을 필드
    private int count;  // 레이아웃 리소스 id를 담는 필드
    private ArrayList<ImageData> imageData = new ArrayList<>();     // 모델

    public CommunityImageAdapter(Context context, int count, ArrayList<Uri> uriArrayList){
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   // xml 레이아웃 파일을 실제 뷰 객체로 만돌어줌
        this.context = context;
        this.count = count;

    }

    @Override
    public int getCount() {     // 어댑터에 사용되는 데이터 개수
        return imageData.size();
    }

    @Override
    public Object getItem(int position) {   // 지정된 position에 있는 데이터(i번째 아이템) -> 객체 형태로 변환
        return imageData.get(position);
    }

    @Override
    public long getItemId(int position) {   // 지정된 postion에 해당한 아이템의 id 반환
        return position;
    }

    @Override   // postion에 위치한 데이터 -> 아이템으로 만들어줌
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.community_listview_image, parent, false);
        }

        ImageData imageData = (ImageData) getItem(position);   // ImageData에서 position에 위치한 데이터 참조 획득
        String imageUrl = imageData.getAlbumImage();

        ImageView imageView = convertView.findViewById(R.id.images);
        Button deleteImage = convertView.findViewById(R.id.btn_image_delete);
        deleteImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d5dee8")));  //이미지 x버튼을 회색으로 만듬
        Glide.with(context).load(imageUrl).into(imageView);     // 이미지 출력해줌

        deleteImage.setOnClickListener(new View.OnClickListener() {     // x버튼 클릭 시 이미지 지우기
            @Override
            public void onClick(View v) {
                Log.i("이미지 제거 X 버튼", "실행O");
            }
        });




        return convertView;
    }


    public void deleteListviewImage(int position){      // 이미지 지우기 + listview 업데이트
        imageData.remove(position);
        this.notifyDataSetChanged();
    }

    // x버튼을 누르면 검색창처럼 listview 업데이트를 위해 어댑터 내 데이터 변경 + 뷰 업데이트 필요할 듯
    public void updateImage(ArrayList<ImageData> newImage) {
        imageData.clear();
        imageData.addAll(newImage);
        notifyDataSetChanged();
    }

}

class ImageData {
    public String albumImage = "";  // 갤러리 이미지, 버튼은 어댑터에서 정의

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    public ImageData(String albumImage) {
        this.albumImage = albumImage;
    }
}
