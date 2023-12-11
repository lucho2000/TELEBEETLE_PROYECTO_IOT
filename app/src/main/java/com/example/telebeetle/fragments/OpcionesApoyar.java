package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.activities.DetallesEvento1;
import com.example.telebeetle.cometchatapi.CometChatApiRest;
import com.example.telebeetle.databinding.FragmentOpcionesApoyarBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class OpcionesApoyar extends Fragment {

    CometChatApiRest cometChatApiRest = new CometChatApiRest();
    FragmentOpcionesApoyarBinding binding;
    FirebaseAuth firebaseAuth;
    HashMap<String, String> listaApoyosBarras, listaParticipantes, noValidadosParticipantes;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("evento");
        String uidUserActual =  firebaseAuth.getCurrentUser().getUid();
        String evento_uid;
        String nroMaxParticipantes;
        Bundle bundle = getArguments();
        // Verificar si el Bundle no es nulo y contiene la clave esperada
        if (bundle != null && bundle.containsKey("evento_uid") && bundle.containsKey("listaApoyosBarras") && bundle.containsKey("listaApoyosParticipantes") && bundle.containsKey("nroMaxParticipantes") && bundle.containsKey("noValidadosParticipantes")) {
            // Obtener el String del Bundle
            evento_uid = bundle.getString("evento_uid");
            nroMaxParticipantes = bundle.getString("nroMaxParticipantes");
            listaApoyosBarras = (HashMap<String, String>) bundle.getSerializable("listaApoyosBarras");
            listaParticipantes = (HashMap<String, String>) bundle.getSerializable("listaApoyosParticipantes");
            noValidadosParticipantes = (HashMap<String, String>) bundle.getSerializable("noValidadosParticipantes");
        } else {
            nroMaxParticipantes = null;
            evento_uid = "evento_uid_blank";
        }
        Log.d("msg_test","GASAGAGSDAGSAGASGASAG ");
        binding = FragmentOpcionesApoyarBinding.inflate(inflater, container, false);
        binding.iconBarra.setOnClickListener(view -> {
            Log.d("msg_test","iconBarra");
            //hashmap de apoyos
            listaApoyosBarras.put(UUID.randomUUID().toString(), uidUserActual);
            HashMap<String, Object> eventoUpdate = new HashMap<>();
            eventoUpdate.put("listaApoyosBarras", listaApoyosBarras);
            //agregando el usuario a la lista de barras del evento
            databaseReference.child(evento_uid).updateChildren(eventoUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    addUsertoEventoGroupCometChat(evento_uid);
                    DialogApoyo dialogApoyo = new DialogApoyo();
                    dialogApoyo.setEvento_uid(evento_uid);
                    dialogApoyo.setListaApoyosBarras(listaApoyosBarras);
                    dialogApoyo.show(getChildFragmentManager(),"DIALOGAPOYO");
                }
            });

        });
        binding.iconParticipante.setOnClickListener(view -> { //esto deberia estar en delegado de actividad
            if(listaParticipantes.size()==Integer.parseInt(nroMaxParticipantes)+1) {
                Toast.makeText(getActivity(), "Se alcanzó el máximo número de participantes", Toast.LENGTH_SHORT).show();
            }else{
                noValidadosParticipantes.put(UUID.randomUUID().toString(), uidUserActual);
                HashMap<String, Object> eventoUpdate = new HashMap<>();
                eventoUpdate.put("listaApoyosParticipantes", noValidadosParticipantes);
                //agregando el usuario a la lista de barras del evento
                databaseReference.child(evento_uid).updateChildren(eventoUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DialogParticipante dialogParticipante  = new DialogParticipante();
                        dialogParticipante.setEvento_uid(evento_uid);
                        dialogParticipante.setListaNoValidados(noValidadosParticipantes);
                        dialogParticipante.show(getChildFragmentManager(),"DIALOGAPOYO");
                    }
                });
            }
            Log.d("msg_test","iconParticipante");
        });
        binding.close.setOnClickListener(view -> {
            if (getActivity() instanceof DetallesEvento1) {
                DetallesEvento1 activity = (DetallesEvento1) getActivity();
                activity.deleteFragment(OpcionesApoyar.this);
            }
        });
        return binding.getRoot();
    }

    public void addUsertoEventoGroupCometChat(String evento_uid) {
        String user_uid = firebaseAuth.getCurrentUser().getUid().toLowerCase();
        ArrayList<String> listaUsersCometChat = new ArrayList<>();
        listaUsersCometChat.add(user_uid);
        String[] listaUsersCometChatArreglo = listaUsersCometChat.toArray(new String[0]);
        //String listaUsersCometChatString = Arrays.toString(listaUsersCometChatArreglo);
        // Crear una cadena con corchetes y comillas
        String listaUsersCometChatString = "[" + String.join(", ", Arrays.stream(listaUsersCometChatArreglo).map(s -> "\"" + s + "\"").toArray(String[]::new)) + "]";
        Log.d("msg_test","addUsertoEventoGroupCometChat");
        cometChatApiRest.addMemberToGroupEventCometChat(evento_uid,listaUsersCometChatString);
    }
}

