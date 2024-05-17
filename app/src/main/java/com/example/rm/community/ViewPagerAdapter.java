package com.example.rm.community;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.rm.R;

import java.util.ArrayList;

// 게시글 상세 페이지에서 상단에 보여줄 사진
public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.PagerHolder> {
    final String tag = "ViewPagerAdapter";
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    private Context context;
    public ViewPagerAdapter(ArrayList<Uri> arrayList, Context context){
        this.context = context;
        this.uriArrayList = arrayList;
    }

    @NonNull
    @Override
    public PagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_viewpager, parent, false);
        PagerHolder pagerHolder = new PagerHolder(view);
        return pagerHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PagerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PagerHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PagerHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.viewpager_image);
        }
    }
}
