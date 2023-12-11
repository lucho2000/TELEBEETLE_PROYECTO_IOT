package com.example.telebeetle.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.telebeetle.activities.DetallesEvento1;
import com.example.telebeetle.cometchatapi.CometChatApiRest;
import com.example.telebeetle.databinding.FragmentEsperaParticipanteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Set;

public class EsperaParticipante extends Fragment {
    FragmentEsperaParticipanteBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("evento");;
    HashMap<String, String> listaNoValidados;
    CometChatApiRest cometChatApiRest = new CometChatApiRest();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        binding = FragmentEsperaParticipanteBinding.inflate(inflater, container, false);

        String uidUserActual =  firebaseAuth.getCurrentUser().getUid();
        String keyUser;
        String evento_uid;
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("evento_uid") && bundle.containsKey("listaNoValidados")) {
            evento_uid = bundle.getString("evento_uid");
            listaNoValidados = (HashMap<String, String>) bundle.getSerializable("listaNoValidados");
        } else {
            evento_uid = "evento_uid_blank";
        }
        Set<String> keys = listaNoValidados.keySet();
        //validando
        keyUser = keys.stream().filter(key -> !key.equalsIgnoreCase("ga") && listaNoValidados.get(key).equals(uidUserActual)).findFirst().orElse(new String());
        binding.cancelarApoyo.setOnClickListener(view -> {
            if (getActivity() instanceof DetallesEvento1) {
                DetallesEvento1 activity = (DetallesEvento1) getActivity();
                Log.d("msg-test-evento-uid",evento_uid);
                listaNoValidados.remove(keyUser);
                HashMap<String, Object> eventoUpdate = new HashMap<>();
                eventoUpdate.put("listaApoyosParticipantes", listaNoValidados);
                //removiendo al usuario de la lista de barras del evento
                databaseReference.child(evento_uid).updateChildren(eventoUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        removeUserFromEventGroupCometChat(evento_uid);
                        activity.deleteFragment(EsperaParticipante.this);
                    }
                });

                notificarBotonPresionado();
            }
        });
        return binding.getRoot();
    }
    public void removeUserFromEventGroupCometChat(String evento_uid) {
        String user_uid = firebaseAuth.getCurrentUser().getUid().toLowerCase();
        Log.d("msg-test-user-uid",user_uid);
        cometChatApiRest.removeMemberFromGroupEventCometChat(evento_uid,user_uid);
    }

    // Interfaz para comunicarse con la actividad
    public interface OnFragmentInteractionListener3 {
        void onTipo3ButtonPressed();
    }

    // Variable para almacenar el listener
    private OnFragmentInteractionListener3 mListener;

    // Método para establecer el listener desde la actividad
    public void setOnFragmentInteractionListener(OnFragmentInteractionListener3 listener) {
        this.mListener = listener;
    }

    // Método para notificar a la actividad que el botón ha sido presionado
    private void notificarBotonPresionado() {
        if (mListener != null) {
            mListener.onTipo3ButtonPressed();
        }
    }
}
