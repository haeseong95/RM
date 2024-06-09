package com.example.rm.manager;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class NNoticeAdapter extends RecyclerView.Adapter<NNoticeAdapter.NoticeViewHolder> {
    static final String tag = "관리자 공지사항 어댑터";
    private List<NNoticeList> noticeList;
    private Context context;

    public NNoticeAdapter(List<NNoticeList> noticeList, Context context) {
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
        NNoticeList notice = noticeList.get(position);
        holder.noticeTitle.setText(notice.getM_notice_title());
        holder.noticeDate.setText(notice.getM_notice_date());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, NNoticeInfo.class);
            intent.putExtra("m_notice_hash", notice.getM_notice_hash().trim());
            intent.putExtra("m_notice_content", notice.getM_notice_content());
            context.startActivity(intent);
        });

        // 수정 버튼 누르면 edit 페이지로 넘김
        holder.btnModify.setOnClickListener(view -> {
            Intent intent = new Intent(context, NNoticeEdit.class);
            intent.putExtra("m_notice_hash", notice.getM_notice_hash().trim());
            intent.putExtra("m_notice_content", notice.getM_notice_content());
            context.startActivity(intent);
        });

        // 삭제 버튼 누르면
        holder.btnDelete.setOnClickListener(view -> {
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
            Log.i(tag + "관리자 공지사항 개수", "아이템 비어있음");
            return 0;
        } else {
            Log.i(tag + "관리자 공지사항 개수", "아이템 개수 : " + noticeList.size());
            return noticeList.size();
        }
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView noticeTitle, noticeDate;
        Button btnDelete, btnModify;
        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeTitle = itemView.findViewById(R.id.m_cnotice_title);
            noticeDate = itemView.findViewById(R.id.m_cnotice_day);
            btnDelete = itemView.findViewById(R.id.m_edit_notice);
            btnModify = itemView.findViewById(R.id.m_delete_notice);
        }
    }
}

class NNoticeList {
    String m_notice_title; // 공지사항 제목
    String m_notice_content;    // 공지사항 내용
    String m_notice_date; // 생성날짜
    String m_notice_hash; // 공지사항 해시값

    public NNoticeList(String m_notice_title, String m_notice_content, String m_notice_date, String m_notice_hash) {
        this.m_notice_title = m_notice_title;
        this.m_notice_content = m_notice_content;
        this.m_notice_date = m_notice_date;
        this.m_notice_hash = m_notice_hash;
    }

    public String getM_notice_title() {
        return m_notice_title;
    }

    public void setM_notice_title(String m_notice_title) {
        this.m_notice_title = m_notice_title;
    }

    public String getM_notice_content() {
        return m_notice_content;
    }

    public void setM_notice_content(String m_notice_content) {
        this.m_notice_content = m_notice_content;
    }

    public String getM_notice_date() {
        return m_notice_date;
    }

    public void setM_notice_date(String m_notice_date) {
        this.m_notice_date = m_notice_date;
    }

    public String getM_notice_hash() {
        return m_notice_hash;
    }

    public void setM_notice_hash(String m_notice_hash) {
        this.m_notice_hash = m_notice_hash;
    }
}








