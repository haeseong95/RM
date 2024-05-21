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

import java.util.ArrayList;

// 커뮤니티 게시글 댓글 listview 어댑터
public class CommunityContentAdapter extends ArrayAdapter<ComContentListData> {

    private ArrayList<ComContentListData> arrayList;

    public CommunityContentAdapter(@NonNull Context context, ArrayList<ComContentListData> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
        Log.i("댓글 어댑터", "어댑터 연결 성공" + arrayList.size());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.community_listview_comment, parent, false);
        }

        TextView cContentNickname = convertView.findViewById(R.id.ccc_nickname);
        TextView cContentPlace = convertView.findViewById(R.id.ccc_level);
        TextView cContentDate = convertView.findViewById(R.id.ccc_date);
        TextView cContentComment = convertView.findViewById(R.id.ccc_commen);

        ComContentListData comContentListData = getItem(position);
        cContentNickname.setText(comContentListData.getComContentNickname());
        cContentPlace.setText(comContentListData.getComContentPlace());
        cContentDate.setText(comContentListData.getComContentDate());
        cContentComment.setText(comContentListData.getComContentComment());

        return convertView;
    }
}

class ComContentListData {
    public String comContentNickname = "";  // 닉네임
    public String comContentPlace = "";    // 등급
    public String comContentDate = "";   // 생성날짜
    public String comContentComment = "";    // 게시글 댓글

    public String getComContentNickname() {
        return comContentNickname;
    }

    public void setComContentNickname(String comContentNickname) {
        this.comContentNickname = comContentNickname;
    }

    public String getComContentPlace() {
        return comContentPlace;
    }

    public void setComContentPlace(String comContentPlace) {
        this.comContentPlace = comContentPlace;
    }

    public String getComContentDate() {
        return comContentDate;
    }

    public void setComContentDate(String comContentDate) {
        this.comContentDate = comContentDate;
    }

    public String getComContentComment() {
        return comContentComment;
    }

    public void setComContentComment(String comContentComment) {
        this.comContentComment = comContentComment;
    }

    public ComContentListData(String comContentNickname, String comContentPlace, String comContentDate, String comContentComment) {
        this.comContentNickname = comContentNickname;
        this.comContentPlace = comContentPlace;
        this.comContentDate = comContentDate;
        this.comContentComment = comContentComment;
    }
}