package com.example.rm.community;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import java.util.ArrayList;
import java.util.List;

// 커뮤니티 메인화면의 listview 어댑터
public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommuntiyViewHolder> {
    static final String tag = "커뮤니티 메인 어댑터";
    private List<CommunityData> communityDataArrayList;
    private Context context;

    // recyclerVew의 클릭 이벤트 처리
    private int currentItemPosition = -1;
    private  OnItemClickEventListener listener;   // 리스너 객체 참조를 저장하는 변수

    public interface OnItemClickEventListener {  //  리사이클러뷰의 클릭 이벤트 처리 -> 인터페이스 정의
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickEventListener listener){   // 리스너 객체 참조를 어댑터에 전달함
        this.listener = listener;
    }

    public CommunityAdapter(Context context, ArrayList<CommunityData> arrayList){
        this.context = context;
        this.communityDataArrayList = arrayList;
    }

    @NonNull
    @Override
    public CommuntiyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_listview, parent, false);
        CommuntiyViewHolder viewHolder = new CommuntiyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommuntiyViewHolder holder, int position) {
        CommunityData communityData = communityDataArrayList.get(position);
        holder.mainTitle.setText(communityData.getMain_title());
        holder.mainNickname.setText(communityData.getMain_nickname());
        holder.mainDate.setText(communityData.getMain_date());
        holder.itemView.setOnClickListener(v -> {   // 게시글 목록을 클릭하면 해시, 아이디 값을 상세 페이지로 전달
            Intent intent = new Intent(context, CommunityContent.class);
            intent.putExtra("community_post_hash", communityData.getMain_hash());
            intent.putExtra("community_post_userId", communityData.getMain_userId());
            Log.i(tag, "해시값 : " + communityData.getMain_hash() + ", 아이디 : " + communityData.getMain_userId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(communityDataArrayList == null){
            Log.i(tag + " 게시글 개수", "아이템 비어있음");
            return 0;
        } else {
            Log.i(tag + " 게시글 개수", "아이템 개수 : " + communityDataArrayList.size());
            return communityDataArrayList.size();
        }
    }

    public class CommuntiyViewHolder extends RecyclerView.ViewHolder{
        TextView mainNickname, mainDate, mainTitle;
        public CommuntiyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainNickname = itemView.findViewById(R.id.c_nickname);
            mainDate = itemView.findViewById(R.id.c_date);
            mainTitle = itemView.findViewById(R.id.c_title);
        }
    }

    public void updateRecyclerViewItem(ArrayList<CommunityData> newItem){
        communityDataArrayList = newItem;
        notifyDataSetChanged();
    }

}

class CommunityData {
    private String main_nickname = ""; // 닉네임
    private String main_date = "";   // 생성날짜
    private String main_title = "";    // 게시글 제목
    private String main_hash = "";   // 게시글 해시값
    private String main_userId = "";    // 게시글을 작성한 사용자 id

    public CommunityData(String main_nickname, String main_date, String main_title, String main_hash, String main_userId) {
        this.main_nickname = main_nickname;
        this.main_date = main_date;
        this.main_title = main_title;
        this.main_hash = main_hash;
        this.main_userId = main_userId;
    }

    public String getMain_nickname() {
        return main_nickname;
    }

    public void setMain_nickname(String main_nickname) {
        this.main_nickname = main_nickname;
    }

    public String getMain_date() {
        return main_date;
    }

    public void setMain_date(String main_date) {
        this.main_date = main_date;
    }

    public String getMain_title() {
        return main_title;
    }

    public void setMain_title(String main_title) {
        this.main_title = main_title;
    }

    public String getMain_hash() {
        return main_hash;
    }

    public void setMain_hash(String main_hash) {
        this.main_hash = main_hash;
    }

    public String getMain_userId() {
        return main_userId;
    }

    public void setMain_userId(String main_userId) {
        this.main_userId = main_userId;
    }
}