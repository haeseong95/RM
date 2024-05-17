package com.example.rm.community;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rm.R;

import java.util.ArrayList;

// 게시글 상세 페이지에서 상단에 보여줄 사진
public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.PagerHolder> {
    final String tag = "ViewPagerAdapter";
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    private Context context;
    public ViewPagerAdapter(Context context, ArrayList<Bitmap> bitmapArrayList){
        this.context = context;
        this.bitmapArrayList = bitmapArrayList;
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
        Bitmap bitmap = bitmapArrayList.get(position);
        Glide.with(context)
                .load(bitmap)
                .format(DecodeFormat.PREFER_RGB_565)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .override(200, 200)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(bitmapArrayList == null){
            Log.i(tag + " bitmap 이미지 개수", "아이템 비어있음");
            return 0;
        } else {
            Log.i(tag + " bitmap 이미지 개수", "아이템 개수 : " + bitmapArrayList.size());
            return bitmapArrayList.size();
        }
    }

    public class PagerHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PagerHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.viewpager_image);
        }
    }
}
