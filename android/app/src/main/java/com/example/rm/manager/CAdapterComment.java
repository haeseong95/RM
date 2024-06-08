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

import com.example.rm.R;
import java.util.ArrayList;
import java.util.List;

public class CAdapterComment extends RecyclerView.Adapter<CAdapterComment.CommentViewHolder> {

    static final String tag = "관리자 댓글 어댑터";
    private List<CommentList> list = new ArrayList<>();
    private Context context;

    public CAdapterComment(List<CommentList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CAdapterComment.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_community_comment, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CAdapterComment.CommentViewHolder holder, int position) {
        CommentList commentList = list.get(position);
        holder.commentTitle.setText(commentList.getmComment_title());
        holder.commentId.setText(commentList.getmComment_id());
        holder.commentDate.setText(commentList.getmComment_date());
        holder.deleteComment.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("댓글 삭제")
                    .setMessage("댓글을 삭제하시겠습니까?")
                    .setPositiveButton("확인", (dialogInterface, which) -> {
                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, list.size());
                        Log.i(tag, "관리자가 커뮤니티의 댓글을 삭제함");
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
        if(list == null){
            Log.i(tag + " 게시글 개수", "아이템 비어있음");
            return 0;
        } else {
            Log.i(tag + " 게시글 개수", "아이템 개수 : " + list.size());
            return list.size();
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentTitle, commentId, commentDate;
        Button deleteComment;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTitle = itemView.findViewById(R.id.m_comment_title);
            commentId = itemView.findViewById(R.id.m_comment_userId);
            commentDate = itemView.findViewById(R.id.m_comment_date);
            deleteComment = itemView.findViewById(R.id.m_delete_comment);
            deleteComment.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
        }
    }
}


class CommentList {
    private String mComment_title;
    private String mComment_id;
    private String mComment_date;
    private String mComment_hash;
    public CommentList(String mComment_title, String mComment_id, String mComment_date, String mComment_hash) {
        this.mComment_title = mComment_title;
        this.mComment_id = mComment_id;
        this.mComment_date = mComment_date;
        this.mComment_hash = mComment_hash;
    }

    public String getmComment_title() {
        return mComment_title;
    }

    public void setmComment_title(String mComment_title) {
        this.mComment_title = mComment_title;
    }

    public String getmComment_id() {
        return mComment_id;
    }

    public void setmComment_id(String mComment_id) {
        this.mComment_id = mComment_id;
    }

    public String getmComment_date() {
        return mComment_date;
    }

    public void setmComment_date(String mComment_date) {
        this.mComment_date = mComment_date;
    }

    public String getmComment_hash() {
        return mComment_hash;
    }

    public void setmComment_hash(String mComment_hash) {
        this.mComment_hash = mComment_hash;
    }
}