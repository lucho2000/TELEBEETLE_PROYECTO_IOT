package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.activities.DetallesEvento1;
import com.example.telebeetle.cometchatapi.CometChatApiRest;
import com.example.telebeetle.databinding.FragmentOpcionesApoyarBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpcionesApoyar extends Fragment {

    CometChatApiRest cometChatApiRest = new CometChatApiRest();
    FragmentOpcionesApoyarBinding binding;
    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;

    List<String> listaUIDUsuarios;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("evento");

        String uidUserActual =  firebaseAuth.getCurrentUser().getUid();

        String evento_uid;
        Bundle bundle = getArguments();
        // Verificar si el Bundle no es nulo y contiene la clave esperada
        if (bundle != null && bundle.containsKey("evento_uid")) {
            // Obtener el String del Bundle
            evento_uid = bundle.getString("evento_uid");
        } else {
            evento_uid = "evento_uid_blank";
        }
        Log.d("msg_test","GASAGAGSDAGSAGASGASAG ");

        binding = FragmentOpcionesApoyarBinding.inflate(inflater, container, false);

        binding.iconBarra.setOnClickListener(view -> {
            Bundle bundleHaciaDialogApoyo = new Bundle();
            bundleHaciaDialogApoyo.putString("evento_uid",evento_uid);
            Log.d("msg_test","iconBarra");
            addUsertoEventoGroupCometChat(evento_uid);
            listaUIDUsuarios.add(uidUserActual); //agregando el usuario a la lista de barras
            databaseReference.child(evento_uid).child("barra").setValue(listaUIDUsuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DialogApoyo dialogApoyo = new DialogApoyo();
                    dialogApoyo.setEvento_uid(evento_uid);
                    dialogApoyo.show(getChildFragmentManager(),"DIALOGAPOYO");
                }
            });

        });
        binding.iconParticipante.setOnClickListener(view -> { //esto deberia estar en delegado de actividad
            TextView textView = binding.textView10;
            textView.setText("Se alcanzó el máximo número de participantes");
            textView.setTextColor(0x706E8F);
            Log.d("msg_test","iconParticipante");
            addUsertoEventoGroupCometChat(evento_uid);
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

