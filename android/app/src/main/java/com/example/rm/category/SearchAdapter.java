package com.example.rm.category;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    static final String tag = "검색창 어댑터 태그";
    private ArrayList<SearchData> arrayList;
    private Context context;

    public SearchAdapter(ArrayList<SearchData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_search_view, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        SearchData searchData = arrayList.get(position);
        holder.trashName.setText(searchData.getTrash_name());
        holder.trashTag.setText(searchData.getTrash_tag());
        String colorCode = "#FFFFFF";
        switch (searchData.getTrash_tag()) {
            case "종이류":
                holder.trashTag.setVisibility(View.VISIBLE);
                colorCode = "#0000FF";
                break;
            case "플라스틱":
                holder.trashTag.setVisibility(View.VISIBLE);
                colorCode = "#FF0000";
                break;
            case "유리류":
                holder.trashTag.setVisibility(View.VISIBLE);
                colorCode = "#808080";
                break;
            default:
                holder.trashTag.setVisibility(View.INVISIBLE);
                colorCode = "#FFFFFF";
                break;
        }
        setColor(holder.trashTag, colorCode);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, TrashDetail.class);
            intent.putExtra("category_trash_search_hash", searchData.getTrash_hash());
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

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView trashName, trashTag;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            trashName = itemView.findViewById(R.id.search_trashName);
            trashTag = itemView.findViewById(R.id.search_category_tag);
        }
    }

    public void updateSearchView(ArrayList<SearchData> newItem) {
        arrayList.clear();
        arrayList.addAll(newItem);
        notifyDataSetChanged();
    }

    private void setColor(TextView textView, String colorCode) {
        if (textView != null) {
            ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(colorCode));
            textView.setBackgroundTintList(colorStateList);
        } else {
            Log.e("SearchAdapter", "TextView is null");
        }
    }
}

class SearchData {
    private String trash_name = "";     // 쓰레기 이름
    private String trash_info = "";     // 쓰레기 분리수거 방법
    private String trash_hash = "";     // 쓰레기 설명 해시값
    private String trash_tag = "";      // 쓰레기 태그

    public SearchData(String trash_name, String trash_hash, String trash_tag) {
        this.trash_name = trash_name;
        this.trash_hash = trash_hash;
        this.trash_tag = trash_tag;
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

    public String getTrash_tag() {
        return trash_tag;
    }

    public void setTrash_tag(String trash_tag) {
        this.trash_tag = trash_tag;
    }
}