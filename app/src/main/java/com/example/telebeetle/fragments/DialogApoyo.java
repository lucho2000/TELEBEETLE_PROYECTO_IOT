package com.example.telebeetle.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.telebeetle.R;

public class DialogApoyo extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dialog_apoyo,container,false);
        Button button = view.findViewById(R.id.continuar);
        button.setOnClickListener(v -> {
            dismiss();
            // Proceed to change the current fragment
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, new OpcionApoyando());
            transaction.commit();
        });
        return view;
    }
}
