package com.example.rm.mypage;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import java.util.ArrayList;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.CommentViewHolder> {
    static final String tag = "내 댓글 수정 어댑터";
    private ArrayList<CommentData> arrayList;
    private Context context;
    public AdapterComment(Context context, ArrayList<CommentData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_community_modify_comment_listview, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentData commentData = arrayList.get(position);
        holder.commentComment.setText(commentData.getComment_comment());
        holder.commentPostTitle.setText(commentData.getComment_post());
        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("댓글 삭제")
                    .setMessage("이 댓글을 삭제하시겠습니까?")
                    .setPositiveButton("확인", (dialogInterface, which) -> {
                        arrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, arrayList.size());
                        Toast.makeText(context, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("취소", (dialogInterface, which) -> dialogInterface.dismiss())
                    .create();

            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(android.R.color.black));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(android.R.color.black));
        });
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MypageModify.class);
            intent.putExtra("mypage_comment_hash", commentData.getComment_hash());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(arrayList == null){
            Log.i(tag + " 게시글 개수", "아이템 비어있음");
            return 0;
        } else {
            Log.i(tag + " 게시글 개수", "아이템 개수 : " + arrayList.size());
            return arrayList.size();
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentComment, commentPostTitle;
        Button btnDelete;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentComment = itemView.findViewById(R.id.my_comment);   // 게시글 제목
            commentPostTitle = itemView.findViewById(R.id.my_post_title);
            btnDelete = itemView.findViewById(R.id.my_delete);
            btnDelete.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
        }
    }
}

class CommentData {
    private String comment_comment = "";  // 댓글 내용
    private String comment_post = "";   // 댓글 단 게시글 제목
    private String comment_hash = "";   // 댓글의 해시값

    public CommentData(String comment_comment, String comment_post, String comment_hash) {
        this.comment_comment = comment_comment;
        this.comment_post = comment_post;
        this.comment_hash = comment_hash;
    }

    public String getComment_comment() {
        return comment_comment;
    }

    public void setComment_comment(String comment_comment) {
        this.comment_comment = comment_comment;
    }

    public String getComment_post() {
        return comment_post;
    }

    public void setComment_post(String comment_post) {
        this.comment_post = comment_post;
    }

    public String getComment_hash() {
        return comment_hash;
    }

    public void setComment_hash(String comment_hash) {
        this.comment_hash = comment_hash;
    }
}