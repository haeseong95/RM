package com.example.rm.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
    private ArrayList<CommentData> commentDataArrayList;
    private Context context;

    public AdapterComment(Context context, ArrayList<CommentData> arrayList) {
        this.context = context;
        this.commentDataArrayList = arrayList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_community_modify_comment_listview, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentData commentData = commentDataArrayList.get(position);
        holder.commentTitle.setText(commentData.getComment_title());
        holder.commentPost.setText(commentData.getComment_post());
        holder.modifyDeleteButton.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("댓글 삭제")
                    .setMessage("이 댓글을 삭제하시겠습니까?")
                    .setPositiveButton("확인", (dialogInterface, which) -> {
                        commentDataArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, commentDataArrayList.size());
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
        return commentDataArrayList == null ? 0 : commentDataArrayList.size();
    }

    public void updateData(ArrayList<CommentData> newData) {
        this.commentDataArrayList = newData;
        notifyDataSetChanged();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentTitle, commentPost;
        Button modifyDeleteButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTitle = itemView.findViewById(R.id.c_comment);
            commentPost = itemView.findViewById(R.id.c_date);
            modifyDeleteButton = itemView.findViewById(R.id.modify_delete);
            modifyDeleteButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
        }
    }
}

class CommentData {
    private String comment_title = "";  // 댓글
    private String comment_post = "";   // 댓글 단 게시글 제목
    private String comment_hash = "";   // 댓글의 해시값

    public CommentData(String comment_title, String comment_post, String comment_hash) {
        this.comment_title = comment_title;
        this.comment_post = comment_post;
        this.comment_hash = comment_hash;
    }

    public String getComment_title() {
        return comment_title;
    }

    public void setComment_title(String comment_title) {
        this.comment_title = comment_title;
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