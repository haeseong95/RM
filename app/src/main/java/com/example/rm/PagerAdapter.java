package com.example.rm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {      // position 값에 따라 각각의 Fragment 반환
        switch (position){
            case 0 :
                return new FragTrashInfo();
            case 1:
                return new FragTrashList();
            default:
                return null;        // null 반환/예외 처리
        }
    }

    // ViewPager에 표시할 페이지 수 반환
    @Override
    public int getItemCount(){
        return 2;
    }


}
