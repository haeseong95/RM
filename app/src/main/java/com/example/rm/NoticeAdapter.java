package com.example.rm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {
    static final String tag = "공지사항 어댑터";
    private List<NoticeData> noticeDataList;
    private Context context;

    public NoticeAdapter(List<NoticeData> noticeDataList, Context context) {
        this.noticeDataList = noticeDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public NoticeAdapter.NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_listview, parent, false);
        NoticeViewHolder viewHolder = new NoticeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.NoticeViewHolder holder, int position) {
        NoticeData noticeData = noticeDataList.get(position);
        holder.noticeTitle.setText(noticeData.getNotice_title());
        holder.noticeDate.setText(noticeData.getNotice_date());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Notice.class);
            intent.putExtra("notice_hash", noticeData.getNotice_hash());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(noticeDataList == null){
            Log.i(tag + " 공지사항 개수", "아이템 비어있음");
            return 0;
        } else {
            Log.i(tag + " 공지사항 개수", "아이템 개수 : " + noticeDataList.size());
            return noticeDataList.size();
        }
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder{
        TextView noticeTitle, noticeDate;
        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeTitle = itemView.findViewById(R.id.notice_title);
            noticeDate = itemView.findViewById(R.id.notice_date);
        }
    }
}

class NoticeData {
    private String notice_title = "";   // 공지사항 제목
    private String notice_date = "";  // 공지사항 생성날짜
    private String notice_hash = "";    // 공지사항 해시값

    public String getNotice_hash() {
        return notice_hash;
    }

    public void setNotice_hash(String notice_hash) {
        this.notice_hash = notice_hash;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public String getNotice_date() {
        return notice_date;
    }

    public void setNotice_date(String notice_date) {
        this.notice_date = notice_date;
    }

    public NoticeData(String notice_title, String notice_date, String notice_hash) {
        this.notice_title = notice_title;
        this.notice_date = notice_date;
        this.notice_hash = notice_hash;
    }
}