package com.example.rm.mypage;

import android.content.Context;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private ArrayList<MypageModifyData> commentDataArrayList;
    private Context context;

    public CommentAdapter(Context context, ArrayList<MypageModifyData> arrayList) {
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
        MypageModifyData commentData = commentDataArrayList.get(position);
        holder.mainDate.setText(commentData.getMain_date());
        holder.mainContent.setText(commentData.getMain_title());

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

            // 버튼 텍스트 색상 설정
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(android.R.color.black));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(android.R.color.black));
        });
    }

    @Override
    public int getItemCount() {
        return commentDataArrayList == null ? 0 : commentDataArrayList.size();
    }

    public void updateData(ArrayList<MypageModifyData> newData) {
        this.commentDataArrayList = newData;
        notifyDataSetChanged();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView mainDate, mainContent;
        Button modifyDeleteButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mainDate = itemView.findViewById(R.id.c_date);
            mainContent = itemView.findViewById(R.id.c_comment);
            modifyDeleteButton = itemView.findViewById(R.id.modify_delete);
            modifyDeleteButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
        }
    }
}

