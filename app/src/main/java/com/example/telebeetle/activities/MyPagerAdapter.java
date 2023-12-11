package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.telebeetle.fragments.EventosFinal;
import com.example.telebeetle.fragments.EventosProceso;
import com.example.telebeetle.fragments.MisEventosFragment;

public class MyPagerAdapter extends FragmentStateAdapter {
    public MyPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new EventosFinal();
        }else if (position == 2){
            return new MisEventosFragment();
        }else{
            return new EventosProceso();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
