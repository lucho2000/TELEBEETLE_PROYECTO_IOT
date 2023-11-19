package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.telebeetle.fragments.EventosFinal;
import com.example.telebeetle.fragments.EventosProceso;

public class MyPagerAdapter extends FragmentStateAdapter {
    public MyPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new EventosFinal();
            // Add more cases for additional tabs
        }
        return new EventosProceso(); // Default to Fragment1
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
