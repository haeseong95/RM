package com.example.rm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MypageModifyAdapter extends RecyclerView.Adapter<MypageModifyAdapter.MypageModifyViewHolder> {
    static final String tag = "커뮤니티 메인 어댑터";
    private ArrayList<MypageModifyData> modifyDataArrayList = new ArrayList<>();
    private Context context;

    public MypageModifyAdapter(Context context, ArrayList<MypageModifyData> arrayList){
        this.context = context;
        this.modifyDataArrayList = arrayList;
    }

    @NonNull
    @Override
    public MypageModifyAdapter.MypageModifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_listview, parent, false);
        MypageModifyAdapter.MypageModifyViewHolder viewHolder = new MypageModifyAdapter.MypageModifyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MypageModifyAdapter.MypageModifyViewHolder holder, int position) {
        MypageModifyData MypageModifyData = modifyDataArrayList.get(position);
        holder.mainNickname.setText(modifyDataArrayList.get(position).getMain_nickname());
        holder.mainPlace.setText(modifyDataArrayList.get(position).getMain_place());
        holder.mainDate.setText(modifyDataArrayList.get(position).getMain_date());
        holder.mainTitle.setText(modifyDataArrayList.get(position).getMain_title());
    }

    @Override
    public int getItemCount() {
        if(modifyDataArrayList == null){
            Log.i(tag + " 게시글 개수", "아이템 비어있음");
            return 0;
        } else {
            Log.i(tag + " 게시글 개수", "아이템 개수 : " + modifyDataArrayList.size());
            return modifyDataArrayList.size();
        }
    }

    public class MypageModifyViewHolder extends RecyclerView.ViewHolder{
        TextView mainNickname, mainPlace, mainDate, mainTitle;
        public MypageModifyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainNickname = itemView.findViewById(R.id.c_nickname);
            mainPlace = itemView.findViewById(R.id.c_level);
            mainDate = itemView.findViewById(R.id.c_date);
            mainTitle = itemView.findViewById(R.id.c_title);

            itemView.setOnClickListener(new View.OnClickListener() {    // 아이템 클릭 이벤트 처리 (ViewHolder 내 itemView에서 클릭 이벤트 처리)
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();    // item의 position 반환

                }
            });
        }
    }


}

class MypageModifyData {
    public String main_nickname = ""; // 닉네임
    public String main_place = "";    // 등급
    public String main_date = "";   // 생성날짜
    public String main_title = "";    // 게시글 제목

    public MypageModifyData(String main_nickname, String main_place, String main_date, String main_title) {
        this.main_nickname = main_nickname;
        this.main_place = main_place;
        this.main_date = main_date;
        this.main_title = main_title;
    }

    public String getMain_nickname() {
        return main_nickname;
    }

    public void setMain_nickname(String main_nickname) {
        this.main_nickname = main_nickname;
    }

    public String getMain_place() {
        return main_place;
    }

    public void setMain_place(String main_place) {
        this.main_place = main_place;
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
}