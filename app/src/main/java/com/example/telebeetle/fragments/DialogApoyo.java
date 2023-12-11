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

import java.util.HashMap;

public class DialogApoyo extends DialogFragment {


    public void setEvento_uid(String evento_uid) {
        this.evento_uid = evento_uid;
    }

    private String evento_uid;
    private HashMap<String,String> listaApoyosBarras;

    public HashMap<String, String> getListaApoyosBarras() {
        return listaApoyosBarras;
    }

    public void setListaApoyosBarras(HashMap<String, String> listaApoyosBarras) {
        this.listaApoyosBarras = listaApoyosBarras;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dialog_apoyo,container,false);
        Button button = view.findViewById(R.id.continuar);
        button.setOnClickListener(v -> {
            dismiss();
            // Proceed to change the current fragment
            OpcionApoyando opcionApoyando = new OpcionApoyando();
            Bundle bundle = new Bundle();
            bundle.putString("evento_uid", evento_uid);
            bundle.putSerializable("listaApoyosBarras", listaApoyosBarras);
            opcionApoyando.setArguments(bundle);
            //opcionApoyando.setOnFragmentInteractionListener( (OpcionApoyando.OnFragmentInteractionListener1) this);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, opcionApoyando);
            transaction.commit();
        });
        return view;
    }
}
