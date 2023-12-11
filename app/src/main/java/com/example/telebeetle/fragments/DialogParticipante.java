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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.telebeetle.R;
import com.example.telebeetle.activities.DetallesEvento1;

import java.util.HashMap;

public class DialogParticipante extends DialogFragment {
    public void setEvento_uid(String evento_uid) {
        this.evento_uid = evento_uid;
    }

    private HashMap<String,String> listaNoValidados;

    public HashMap<String, String> getListaNoValidados() {
        return listaNoValidados;
    }

    public void setListaNoValidados(HashMap<String, String> listaNoValidados) {
        this.listaNoValidados = listaNoValidados;
    }
    private String evento_uid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dialog_participante,container,false);
        Button button = view.findViewById(R.id.continuar);
        button.setOnClickListener(v -> {
            dismiss();
            EsperaParticipante esperaParticipante = new EsperaParticipante();
            Bundle bundle = new Bundle();
            bundle.putString("evento_uid", evento_uid);
            bundle.putSerializable("listaNoValidados", listaNoValidados);
            esperaParticipante.setArguments(bundle);
            //esperaParticipante.setOnFragmentInteractionListener((EsperaParticipante.OnFragmentInteractionListener3) this);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, esperaParticipante);
            transaction.commit();
        });
        return view;
    }
}
