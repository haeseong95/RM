package com.example.rm.community;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rm.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

// 커뮤니티 메인화면의 listview 어댑터
public class CommunityAdapter extends ArrayAdapter<ComListData> {

    private ArrayList<ComListData> arrayList;
    public CommunityAdapter(@NonNull Context context, ArrayList<ComListData> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
        Log.i("ComList어댑터 (메인)", "어댑터 연결 성공" + arrayList.size());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.community_listview, parent, false);
        }

        TextView cNickname = convertView.findViewById(R.id.c_nickname);
        TextView cPlace = convertView.findViewById(R.id.c_level);
        TextView cDate = convertView.findViewById(R.id.c_date);
        TextView cTitle = convertView.findViewById(R.id.c_title);

        ComListData comListData = getItem(position);
        cNickname.setText(comListData.getComNickname());
        cPlace.setText(comListData.getComPlace());
        cDate.setText(comListData.getComDate());
        cTitle.setText(comListData.getComTitle());

        return convertView;
    }
}

class ComListData {
    public String comNickname = ""; // 닉네임
    public String comPlace = "";    // 등급
    public String comDate = "";   // 생성날짜
    public String comTitle = "";    // 게시글 제목

    public String getComNickname() {
        return comNickname;
    }

    public void setComNickname(String comNickname) {
        this.comNickname = comNickname;
    }

    public String getComPlace() {
        return comPlace;
    }

    public void setComPlace(String comPlace) {
        this.comPlace = comPlace;
    }

    public String getComDate() {
        return comDate;
    }

    public void setComDate(String comDate) {
        this.comDate = comDate;
    }

    public String getComTitle() {
        return comTitle;
    }

    public void setComTitle(String comTitle) {
        this.comTitle = comTitle;
    }

    public ComListData(String comNickname, String comPlace, String comDate, String comTitle) {
        this.comNickname = comNickname;
        this.comPlace = comPlace;
        this.comDate = comDate;
        this.comTitle = comTitle;
    }
}