package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.fragments.ApoyoBarraFragment;
import com.example.telebeetle.fragments.ApoyoParticipanteFragment;


public class MyPagerAdapter2 extends FragmentStateAdapter {
    private Evento evento;
    public MyPagerAdapter2(@NonNull FragmentActivity fragmentActivity, Evento evento) {
        super(fragmentActivity);
        this.evento = evento;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return ApoyoParticipanteFragment.newInstance(evento);
        }
        return ApoyoBarraFragment.newInstance(evento);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
