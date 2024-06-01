package com.example.rm.mypage;

import android.content.Context;
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

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MypageModifyViewHolder> {
    static final String tag = "내 게시글 수정 어댑터";
    private ArrayList<PostData> modifyDataArrayList;
    private Context context;

    public AdapterPost(Context context, ArrayList<PostData> arrayList) {
        this.context = context;
        this.modifyDataArrayList = arrayList;
    }

    @NonNull
    @Override
    public MypageModifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_community_modify_listview, parent, false);
        return new MypageModifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MypageModifyViewHolder holder, int position) {
        PostData postData = modifyDataArrayList.get(position);
        holder.postDate.setText(postData.getMy_post_date());
        holder.postTitle.setText(postData.getMy_post_title());
        holder.modifyDeleteButton.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("게시글 삭제")
                    .setMessage("이 게시글을 삭제하시겠습니까?")
                    .setPositiveButton("확인", (dialogInterface, which) -> {
                        modifyDataArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, modifyDataArrayList.size());
                        Toast.makeText(context, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("취소", (dialogInterface, which) -> dialogInterface.dismiss())
                    .create();

            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(android.R.color.black));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(android.R.color.black));
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (modifyDataArrayList == null) {
            Log.i(tag + " 게시글 개수", "아이템 비어있음");
            return 0;
        } else {
            Log.i(tag + " 게시글 개수", "아이템 개수 : " + modifyDataArrayList.size());
            return modifyDataArrayList.size();
        }
    }

    public void updateData(ArrayList<PostData> newData) {
        this.modifyDataArrayList = newData;
        notifyDataSetChanged();
    }

    public class MypageModifyViewHolder extends RecyclerView.ViewHolder {
        TextView postDate, postTitle;
        Button modifyDeleteButton;

        public MypageModifyViewHolder(@NonNull View itemView) {
            super(itemView);
            postDate = itemView.findViewById(R.id.c_date);
            postTitle = itemView.findViewById(R.id.c_title);
            modifyDeleteButton = itemView.findViewById(R.id.modify_delete);
            modifyDeleteButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
        }
    }
}

class PostData {
    private String my_post_title;   // 게시글 제목
    private String my_post_date;    // 게시글 작성날짜
    private String my_post_hash;    // 게시글 해시값

    public PostData(String my_post_title, String my_post_date, String my_post_hash) {
        this.my_post_title = my_post_title;
        this.my_post_date = my_post_date;
        this.my_post_hash = my_post_hash;
    }

    public String getMy_post_title() {
        return my_post_title;
    }

    public void setMy_post_title(String my_post_title) {
        this.my_post_title = my_post_title;
    }

    public String getMy_post_date() {
        return my_post_date;
    }

    public void setMy_post_date(String my_post_date) {
        this.my_post_date = my_post_date;
    }

    public String getMy_post_hash() {
        return my_post_hash;
    }

    public void setMy_post_hash(String my_post_hash) {
        this.my_post_hash = my_post_hash;
    }
}



