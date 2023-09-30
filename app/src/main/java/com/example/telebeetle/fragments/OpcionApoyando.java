package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.activities.DetallesEvento1;
import com.example.telebeetle.databinding.FragmentOpcionApoyandoBinding;


public class OpcionApoyando extends Fragment {
    FragmentOpcionApoyandoBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOpcionApoyandoBinding.inflate(inflater, container, false);
        binding.cancelarApoyo.setOnClickListener(view -> {
            if (getActivity() instanceof DetallesEvento1) {
                DetallesEvento1 activity = (DetallesEvento1) getActivity();
                activity.deleteFragment(OpcionApoyando.this);
            }
        });
        return binding.getRoot();
    }
}