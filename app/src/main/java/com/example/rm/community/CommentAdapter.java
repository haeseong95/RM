package com.example.rm.community;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import java.util.ArrayList;

// 댓글 어댑터
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    static final String tag = "댓글 어댑터";
    private ArrayList<CommentData> arrayList = new ArrayList<>();
    private Context context;
    private String currentUserId;   // 사용자 현재 ID로 댓글이 본인이 쓴 게 맞는지 검사하려고 생성자에 넣었는데 일단 빼고 필요하면 나중에 추가하기

    public CommentAdapter(Context context, ArrayList<CommentData> arrayList){
        this.arrayList = arrayList;
        this.context = context;
        //this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_listview_comment, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentData commentData = arrayList.get(position);
        holder.commentNickname.setText(arrayList.get(position).getComment_nickname());
        holder.commentPlace.setText(arrayList.get(position).getComment_place());
        holder.commentDate.setText(arrayList.get(position).getComment_date());
        holder.commentComment.setText(arrayList.get(position).getComment_comment());
        holder.commentId.setText(arrayList.get(position).getComment_id());

        holder.btnMenu.setOnClickListener(v -> {
            showPopupMenu(v, commentData);
        });
    }

    @Override
    public int getItemCount() {
        if(arrayList == null){
            Log.i(tag + " 댓글 개수", "아이템 비어있음");
            return 0;
        } else {
            Log.i(tag + " 댓글 개수", "아이템 개수 : " + arrayList.size());
            return arrayList.size();
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentNickname, commentPlace, commentDate, commentComment, commentId;
        Button btnMenu;     // 댓글 수정, 삭제, 신고 버튼

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentNickname = itemView.findViewById(R.id.ccc_nickname);
            commentPlace = itemView.findViewById(R.id.ccc_level);
            commentDate = itemView.findViewById(R.id.ccc_date);
            commentComment = itemView.findViewById(R.id.ccc_commen);
            commentId = itemView.findViewById(R.id.ccc_userId);
            btnMenu = itemView.findViewById(R.id.modify_delete);
            btnMenu.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
        }
    }

    private void showPopupMenu(View view, CommentData comment) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.comment_menu);


        /* 본인 댓글인지 확인
        if (!comment.getComment_id().equals(currentUserId)) {
            popupMenu.getMenu().findItem(R.id.action_edit).setVisible(false);       // 댓글 수정
            popupMenu.getMenu().findItem(R.id.action_delete).setVisible(false);     // 댓글 삭제
        }


         */

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    // 수정 기능 처리
                    editComment(comment);
                    return true;
                case R.id.action_delete:
                    // 삭제 기능 처리
                    deleteComment(comment);
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }

    private void editComment(CommentData comment) {
        // 댓글 수정 기능 구현
    }

    private void deleteComment(CommentData comment) {
        // 댓글 삭제 기능 구현
    }
}

class CommentData {
    private String comment_id = "";     // 아이디
    private String comment_nickname = "";  // 닉네임
    private String comment_place = "";    // 등급
    private String comment_date = "";   // 생성날짜
    private String comment_comment = "";    // 게시글 댓글

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_nickname() {
        return comment_nickname;
    }

    public void setComment_nickname(String comment_nickname) {
        this.comment_nickname = comment_nickname;
    }

    public String getComment_place() {
        return comment_place;
    }

    public void setComment_place(String comment_place) {
        this.comment_place = comment_place;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public String getComment_comment() {
        return comment_comment;
    }

    public void setComment_comment(String comment_comment) {
        this.comment_comment = comment_comment;
    }

    public CommentData(String comment_id, String comment_nickname, String comment_place, String comment_date, String comment_comment) {
        this.comment_id = comment_id;
        this.comment_nickname = comment_nickname;
        this.comment_place = comment_place;
        this.comment_date = comment_date;
        this.comment_comment = comment_comment;
    }
}