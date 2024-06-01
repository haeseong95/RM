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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MypageModifyViewHolder> {
    static final String tag = "내 게시글 수정 어댑터";
    private ArrayList<MypageModifyData> modifyDataArrayList;
    private Context context;

    public PostAdapter(Context context, ArrayList<MypageModifyData> arrayList) {
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
        MypageModifyData mypageModifyData = modifyDataArrayList.get(position);
        holder.mainDate.setText(mypageModifyData.getMain_date());
        holder.mainTitle.setText(mypageModifyData.getMain_title());

        holder.modifyDeleteButton.setOnClickListener(v -> {
            // 삭제 버튼 클릭 시 알림 다이얼로그 표시
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("게시글 삭제")
                    .setMessage("이 게시글을 삭제하시겠습니까?")
                    .setPositiveButton("확인", (dialogInterface, which) -> {
                        // 확인 버튼 클릭 시 삭제 처리
                        modifyDataArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, modifyDataArrayList.size());
                        Toast.makeText(context, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("취소", (dialogInterface, which) -> dialogInterface.dismiss())
                    .create();

            dialog.show();

            // 버튼 텍스트 색상 설정
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(android.R.color.black));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(android.R.color.black));
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

    public void updateData(ArrayList<MypageModifyData> newData) {
        this.modifyDataArrayList = newData;
        notifyDataSetChanged();
    }

    public class MypageModifyViewHolder extends RecyclerView.ViewHolder {
        TextView mainDate, mainTitle;
        Button modifyDeleteButton;

        public MypageModifyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainDate = itemView.findViewById(R.id.c_date);
            mainTitle = itemView.findViewById(R.id.c_title);
            modifyDeleteButton = itemView.findViewById(R.id.modify_delete);
            modifyDeleteButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                // 아이템 클릭 이벤트 처리
            });
        }
    }
}

class MypageModifyData {
    private String main_date;   // 생성날짜
    private String main_title;  // 게시글 제목

    public MypageModifyData(String main_date, String main_title) {
        this.main_date = main_date;
        this.main_title = main_title;
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



