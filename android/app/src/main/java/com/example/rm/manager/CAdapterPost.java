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

public class CAdapterPost extends RecyclerView.Adapter<CAdapterPost.PostViewHolder> {
    static final String tag = "관리자 게시글 어댑터";
    private List<PostList> list = new ArrayList<>();
    private Context context;

    public CAdapterPost(List<PostList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CAdapterPost.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_community_post, parent, false);
        PostViewHolder viewHolder = new PostViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CAdapterPost.PostViewHolder holder, int position) {
        PostList postList = list.get(position);
        holder.postTitle.setText(postList.getmPost_title());
        holder.postId.setText(postList.getmPost_id());
        holder.postDate.setText(postList.getmPost_date());
        holder.deletePost.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("게시글 삭제")
                    .setMessage("게시글을 삭제하시겠습니까?")
                    .setPositiveButton("확인", (dialogInterface, which) -> {
                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, list.size());
                        Log.i(tag, "관리자가 커뮤니티의 게시글 삭제함");
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

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postTitle, postId, postDate;
        Button deletePost;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.m_post_title);
            postId = itemView.findViewById(R.id.m_post_userId);
            postDate = itemView.findViewById(R.id.m_post_date);
            deletePost = itemView.findViewById(R.id.m_delete_post);
            deletePost.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
        }
    }
}

class PostList {
    private String mPost_title; // 게시글 제목
    private String mPost_id;    // 아이디
    private String mPost_date;  // 생성날짜
    private String mPost_hash;  // 게시글 해시값

    public PostList(String mPost_title, String mPost_id, String mPost_date, String mPost_hash) {
        this.mPost_title = mPost_title;
        this.mPost_id = mPost_id;
        this.mPost_date = mPost_date;
        this.mPost_hash = mPost_hash;
    }

    public String getmPost_title() {
        return mPost_title;
    }

    public void setmPost_title(String mPost_title) {
        this.mPost_title = mPost_title;
    }

    public String getmPost_id() {
        return mPost_id;
    }

    public void setmPost_id(String mPost_id) {
        this.mPost_id = mPost_id;
    }

    public String getmPost_date() {
        return mPost_date;
    }

    public void setmPost_date(String mPost_date) {
        this.mPost_date = mPost_date;
    }

    public String getmPost_hash() {
        return mPost_hash;
    }

    public void setmPost_hash(String mPost_hash) {
        this.mPost_hash = mPost_hash;
    }
}

