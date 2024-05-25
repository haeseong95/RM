package com.example.rm.community;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import java.util.ArrayList;

// 댓글 어댑터
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
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
        int currentPosition = holder.getAbsoluteAdapterPosition();  // 클릭한 item의 position 값
        CommentData commentData = arrayList.get(position);
        holder.commentNickname.setText(commentData.getComment_nickname());
        holder.commentPlace.setText(commentData.getComment_place());
        holder.commentDate.setText(commentData.getComment_date());
        holder.commentComment.setText(commentData.getComment_comment());
        holder.commentEdit.setText(commentData.getComment_comment());

        // 댓글 수정, 삭제 버튼 팝업 메뉴창
        if (holder.btnMenu != null){    // findviewbyId가 null을 반환하는 경우가 있음
            holder.btnMenu.setOnClickListener(v -> {
//          v.showContextMenu();    버튼 클릭 시 컨텍스트 메뉴 보여줌 (이건 communityContent.java에서 사용됨)
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.inflate(R.menu.comment_menu);

//                본인 댓글인지 확인, 본인이 작성한 댓글이면 메뉴창이 뜨고 아니면 안눌려야 함
//                if (!comment.getComment_id().equals(currentUserId)) {
//                    popupMenu.getMenu().findItem(R.id.action_edit).setVisible(false);       // 댓글 수정
//                    popupMenu.getMenu().findItem(R.id.action_delete).setVisible(false);     // 댓글 삭제
//                }

                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_edit:  // 수정
                            holder.commentComment.setVisibility(View.GONE);
                            holder.commentEdit.setVisibility(View.VISIBLE);
                            holder.btnUnModify.setVisibility(View.VISIBLE);
                            holder.btnModify.setVisibility(View.VISIBLE);
                            holder.btnModify.setEnabled(false);

                            holder.commentEdit.setText(holder.commentComment.getText().toString());
//                            holder.commentEdit.post(() -> {     // 키보드 올라옴
//                                holder.commentEdit.requestFocus();
//                                holder.commentEdit.setSelection(holder.commentEdit.getText().length());
//                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                                imm.showSoftInput(holder.commentEdit, InputMethodManager.SHOW_IMPLICIT);
//                            });
                            holder.commentEdit.postDelayed(() -> {
                                holder.commentEdit.requestFocus();
                                holder.commentEdit.setSelection(holder.commentEdit.getText().length());
                                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(holder.commentEdit, InputMethodManager.SHOW_FORCED);
                            }, 100);

                            return true;
                        case R.id.action_delete:    // 삭제
                            Log.i(tag, "현재 게시글 position 값 : " + currentPosition);
                            AlertDialog dialog = new AlertDialog.Builder(context)
                                    .setTitle("댓글 삭제")
                                    .setMessage("댓글을 완전히 삭제할까요?")
                                    .setPositiveButton("확인", null) // 리스너는 나중에 설정
                                    .setNegativeButton("취소", null) // 리스너는 나중에 설정
                                    .create();

                            dialog.setOnShowListener(dialogInterface -> {
                                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                                // 버튼이 null이 아닌지 확인 후 색상 설정
                                if (positiveButton != null) {
                                    positiveButton.setTextColor(context.getResources().getColor(android.R.color.black));
                                    positiveButton.setOnClickListener(v1 -> {
                                        // 삭제 로직 처리
                                        arrayList.remove(currentPosition);
                                        notifyItemRemoved(currentPosition);
                                        notifyItemRangeChanged(currentPosition, arrayList.size() - currentPosition);
                                        Toast.makeText(context, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    });
                                }
                                if (negativeButton != null) {
                                    negativeButton.setTextColor(context.getResources().getColor(android.R.color.black));
                                    negativeButton.setOnClickListener(v1 -> dialog.dismiss());
                                }
                            });
                            dialog.show();
                            return true;
                        default:
                            return false;
                    }
                });
                popupMenu.show();
            });
        } else {
            Log.e(tag, "팝업 메뉴창 오류");
        }

        // 댓글 수정 입력창
        holder.commentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentText = holder.commentComment.getText().toString();    // 수정 전 댓글
                String updateText = holder.commentEdit.getText().toString();    // 수정 후 댓글
                if(!currentText.equals(updateText)){   // 텍스트가 변경되면 확인 버튼 활성화 (s는 변경 후의 텍스트)
                    holder.btnModify.setEnabled(true);
                    holder.btnModify.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.blue));
                    holder.btnModify.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
                } else {
                    holder.btnModify.setEnabled(false);
                    holder.btnModify.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.category_gray));
                    holder.btnModify.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 댓글 수정 확인 버튼
        if(holder.btnModify != null){
            holder.btnModify.setOnClickListener(v -> {
                String updateComment = holder.commentEdit.getText().toString();
                commentData.setComment_comment(updateComment);
                notifyItemChanged(currentPosition);

                holder.commentComment.setText(updateComment);
                holder.commentComment.setVisibility(View.VISIBLE);
                holder.commentEdit.setVisibility(View.GONE);
                holder.btnModify.setVisibility(View.GONE);
                holder.btnUnModify.setVisibility(View.GONE);
            });
        } else {
            Log.e(tag, "댓글 수정 확인 버튼 오류");
        }

        // 댓글 수정 취소 버튼
        if(holder.btnUnModify != null){
            holder.btnUnModify.setOnClickListener(v -> {
                holder.commentComment.setVisibility(View.VISIBLE);
                holder.commentEdit.setVisibility(View.GONE);
                holder.btnModify.setVisibility(View.GONE);
                holder.btnUnModify.setVisibility(View.GONE);
            });
        } else {
            Log.e(tag, "댓글 수정 취소 버튼 오류");
        }
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

    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView commentNickname, commentPlace, commentDate, commentComment;
        EditText commentEdit;
        Button btnMenu, btnUnModify, btnModify;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            commentEdit = itemView.findViewById(R.id.ccc_edit);
            commentNickname = itemView.findViewById(R.id.ccc_nickname);
            commentPlace = itemView.findViewById(R.id.ccc_level);
            commentDate = itemView.findViewById(R.id.ccc_date);
            commentComment = itemView.findViewById(R.id.ccc_commen);
            btnUnModify = itemView.findViewById(R.id.btn_commentNo);
            btnModify = itemView.findViewById(R.id.btn_commentYes);
            btnMenu = itemView.findViewById(R.id.modify_delete);
            btnMenu.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {  // 팝업메뉴를 recyclerview item view에 등록함
            ((Activity) view.getContext()).getMenuInflater().inflate(R.menu.comment_menu, contextMenu);
        }
    }
}

class CommentData {
    private String comment_nickname = "";  // 닉네임
    private String comment_place = "";    // 등급
    private String comment_date = "";   // 생성날짜
    private String comment_comment = "";    // 게시글 댓글
    private String comment_edit = "";   // 댓글 수정

    public String getComment_edit() {
        return comment_edit;
    }

    public void setComment_edit(String comment_edit) {
        this.comment_edit = comment_edit;
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

    public CommentData(String comment_nickname, String comment_place, String comment_date, String comment_comment) {
        this.comment_nickname = comment_nickname;
        this.comment_place = comment_place;
        this.comment_date = comment_date;
        this.comment_comment = comment_comment;
    }
}