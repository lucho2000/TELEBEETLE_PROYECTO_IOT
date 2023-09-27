package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telebeetle.DetallesEvento1;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.FragmentOpcionesApoyarBinding;

import java.util.Objects;

public class OpcionesApoyar extends Fragment {

    FragmentOpcionesApoyarBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOpcionesApoyarBinding.inflate(inflater, container, false);

        binding.iconBarra.setOnClickListener(view -> {
            DialogApoyo dialogApoyo = new DialogApoyo();
            dialogApoyo.show(getChildFragmentManager(),"DIALOGAPOYO");
        });
        binding.iconParticipante.setOnClickListener(view -> {
            TextView textView = binding.textView10;
            textView.setText("Se alcanzó el máximo número de participantes");
            textView.setTextColor(0x706E8F);
        });
        binding.close.setOnClickListener(view -> {
            if (getActivity() instanceof DetallesEvento1) {
                DetallesEvento1 activity = (DetallesEvento1) getActivity();
                activity.deleteFragment(OpcionesApoyar.this);
            }
        });
        return binding.getRoot();
    }

}