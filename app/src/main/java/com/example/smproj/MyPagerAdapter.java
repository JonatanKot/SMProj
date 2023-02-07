package com.example.smproj;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPagerAdapter extends FragmentStateAdapter {
    private Context mContext;

    public MyPagerAdapter(FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
        //mContext = context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:{
                return new MusicListFragment();
            }
            case 1:{
                return new ServerFragment();
            }
            case 2:{
                return new RecordingFragment();
            }
            default:{
                return new DocsFragment();
            }
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
