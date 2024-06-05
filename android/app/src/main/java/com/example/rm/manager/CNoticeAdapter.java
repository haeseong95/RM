package com.example.rm.manager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.Notice;
import com.example.rm.R;

import java.util.List;

public class CNoticeAdapter extends RecyclerView.Adapter<CNoticeAdapter.NoticeViewHolder> {
    static final String tag = "관리자 공지사항 수정,삭제 어댑터";
    private List<Notice> noticeList;
    private Context context;

    public CNoticeAdapter(List<Notice> noticeList, Context context) {
        this.noticeList = noticeList;
        this.context = context;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_notice_listview, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        NoticeList notice = noticeList.get(position);
        holder.title.setText(notice.getTitle());
        holder.date.setText(notice.getDate());
        holder.editnotice.setOnClickListener(view -> {
            // Handle edit post
        });
        holder.deletenotice.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("공지사항 삭제")
                    .setMessage("공지사항을 삭제하시겠습니까?")
                    .setPositiveButton("확인", (dialogInterface, which) -> {
                        noticeList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, noticeList.size());
                        Log.i(tag, "관리자가 공지사항의 게시글 삭제함");
                    })
                    .setNegativeButton("취소", (dialogInterface, which) -> dialogInterface.dismiss())
                    .create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(android.R.color.black));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(android.R.color.black));
        });
    }

    @Override
    public int getItemCount() {
        if (noticeList == null) {
            Log.i(tag + " 공지사항 개수", "아이템 비어있음");
            return 0;
        } else {
            Log.i(tag + " 공지사항 개수", "아이템 개수 : " + noticeList.size());
            return noticeList.size();
        }
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView title, date;
        Button editnotice, deletenotice;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.m_cnotice_title);
            date = itemView.findViewById(R.id.m_cnotice_day);
            editnotice = itemView.findViewById(R.id.m_edit_notice);
            deletenotice = itemView.findViewById(R.id.m_delete_notice);
            deletenotice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
        }
    }
}

class NoticeList {
    private String title; // 게시글 제목
    private String date; // 생성날짜
    private String hash; // 게시글 해시값

    public NoticeList(String title, String date, String hash) {
        this.title = title;
        this.date = date;
        this.hash = hash;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}





