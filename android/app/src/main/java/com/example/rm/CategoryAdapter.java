package com.example.rm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rm.category.CategoryInfo;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    static final String tag = "메인화면 카테고리 어댑터";
    private ArrayList<CategoryData> arrayList;
    private Context context;

    public CategoryAdapter(ArrayList<CategoryData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_btn_view, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        CategoryData data = arrayList.get(position);
        holder.trash_type.setText(data.getCategory_title());
        holder.trash_type.setOnClickListener(view -> {
            Intent intent = new Intent(context, CategoryInfo.class);
            intent.putExtra("category_trash_hash", data.getCategory_hash().trim());
            intent.putExtra("category_trash_name", data.getCategory_title().trim());
            Log.i(tag, "해시값 : " + data.getCategory_hash());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(arrayList == null){
            return 0;
        } else {
            return arrayList.size();
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView trash_type;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            trash_type = itemView.findViewById(R.id.main_trashName);
        }
    }
}

class CategoryData {
    private String category_title = ""; // 카테고리 쓰레기 종류 이름
    private String category_hash = "";  // 해시값

    public CategoryData(String category_title, String category_hash) {
        this.category_title = category_title;
        this.category_hash = category_hash;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getCategory_hash() {
        return category_hash;
    }

    public void setCategory_hash(String category_hash) {
        this.category_hash = category_hash;
    }
}
