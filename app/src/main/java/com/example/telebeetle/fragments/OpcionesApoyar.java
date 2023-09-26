package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
            Toast.makeText(container.getContext(), "Apoyar como barra", Toast.LENGTH_SHORT).show();
        });
        binding.iconParticipante.setOnClickListener(view -> {
            Toast.makeText(container.getContext(), "Apoyar como participante", Toast.LENGTH_SHORT).show();
        });
        binding.close.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        return binding.getRoot();
    }

}