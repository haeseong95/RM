package com.example.rm.category;

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

public class TrashAdapter extends RecyclerView.Adapter<TrashAdapter.TrashViewHolder> {
    static final String tag = "카테고리 쓰레기 목록 어댑터";
    private ArrayList<TrashData> arrayList;
    private Context context;

    public TrashAdapter(ArrayList<TrashData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        Log.i(tag, "쓰레기 설명 어댑터 연결 성공");
    }

    @NonNull
    @Override
    public TrashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_listview, parent, false);
        TrashViewHolder viewHolder = new TrashViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrashAdapter.TrashViewHolder holder, int position) {
        TrashData trashData = arrayList.get(position);
        holder.trashName.setText(trashData.getTrash_name());
        holder.trashInfo.setText(trashData.getTrash_info());
        holder.trashHash.setText(trashData.getTrash_hash());
        holder.itemView.setOnClickListener(v -> {       // 해시값 넘겨서 해당하는 이미지 가져오기
            int item_position = holder.getAbsoluteAdapterPosition();    // item의 position 반환
            Intent intent = new Intent(context, TrashDetail.class);
            intent.putExtra("category_trash_hash", trashData.getTrash_hash());
            Log.i(tag, "클릭한 item 위치 : " + item_position + ", 해시값 : " + trashData.getTrash_hash());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(arrayList == null){
            Log.i(tag, "쓰레기 목록을 나타내는 아이템 0개");
            return 0;
        } else {
            Log.i(tag, "쓰레기 목록 개수 : " + arrayList.size());
            return arrayList.size();
        }
    }

    public class TrashViewHolder extends RecyclerView.ViewHolder {
        TextView trashName, trashInfo, trashHash;
        public TrashViewHolder(@NonNull View itemView) {
            super(itemView);
            trashName = itemView.findViewById(R.id.list_trashName);
            trashInfo = itemView.findViewById(R.id.list_trashInfo);
            trashHash = itemView.findViewById(R.id.list_trashHash);
        }
    }

    // 쓰레기 검색창의 텍스트마다 recyclerview 갱신 시 필요
    public void updateSearchView(ArrayList<TrashData> newItem) {
        arrayList.clear();
        arrayList.addAll(newItem);
        notifyDataSetChanged();
    }
}

class TrashData {
    private String trash_name = "";     // 쓰레기 이름
    private String trash_info = "";     // 쓰레기 분리수거 방법
    private String trash_hash = "";     // 쓰레기 설명 해시값
    public TrashData(String trash_name, String trash_info, String trash_hash) {
        this.trash_name = trash_name;
        this.trash_info = trash_info;
        this.trash_hash = trash_hash;
    }
    public String getTrash_name() {
        return trash_name;
    }

    public void setTrash_name(String trash_name) {
        this.trash_name = trash_name;
    }

    public String getTrash_info() {
        return trash_info;
    }

    public void setTrash_info(String trash_info) {
        this.trash_info = trash_info;
    }

    public String getTrash_hash() {
        return trash_hash;
    }

    public void setTrash_hash(String trash_hash) {
        this.trash_hash = trash_hash;
    }
}