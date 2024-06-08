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
import com.example.rm.community.CommunityContent;

import java.util.ArrayList;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MypageModifyViewHolder> {
    static final String tag = "내 게시글 수정 어댑터";
    private ArrayList<PostData> arrayList;
    private Context context;
    public AdapterPost(Context context, ArrayList<PostData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MypageModifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_community_modify_listview, parent, false);
        return new MypageModifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MypageModifyViewHolder holder, int position) {
        PostData postData = arrayList.get(position);
        holder.postDate.setText(postData.getMy_post_date());
        holder.postTitle.setText(postData.getMy_post_title());
        holder.postNickname.setText(postData.getMy_post_nickname());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CommunityContent.class);
            intent.putExtra("mypage_post_hash", postData.getMy_post_hash().trim());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList == null) {
            Log.i(tag, "내가 작성한 게시글 개수 : " + arrayList.size());
            return 0;
        } else {
            Log.i(tag, "내가 작성한 게시글 개수 : " + arrayList.size());
            return arrayList.size();
        }
    }
    public class MypageModifyViewHolder extends RecyclerView.ViewHolder {
        TextView postDate, postTitle, postNickname;
        public MypageModifyViewHolder(@NonNull View itemView) {
            super(itemView);
            postDate = itemView.findViewById(R.id.mypage_date);
            postTitle = itemView.findViewById(R.id.mypage_title);
            postNickname = itemView.findViewById(R.id.mypage_nickname);
        }
    }
}

class PostData {
    private String my_post_title;   // 게시글 제목
    private String my_post_nickname;    // 게시글 닉네임
    private String my_post_date;    // 게시글 작성날짜
    private String my_post_hash;    // 게시글 해시값
    public PostData(String my_post_title, String my_post_nickname, String my_post_date, String my_post_hash) {
        this.my_post_title = my_post_title;
        this.my_post_nickname = my_post_nickname;
        this.my_post_date = my_post_date;
        this.my_post_hash = my_post_hash;
    }
    public String getMy_post_nickname() {
        return my_post_nickname;
    }

    public void setMy_post_nickname(String my_post_nickname) {
        this.my_post_nickname = my_post_nickname;
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