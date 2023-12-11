package com.example.telebeetle.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.R;
import com.example.telebeetle.activities.DetallesEvento1;
import com.example.telebeetle.activities.GeneralViewActivity;
import com.example.telebeetle.cometchatapi.CometChatApiRest;
import com.example.telebeetle.databinding.FragmentOpcionApoyandoBinding;
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
import java.util.Set;
import java.util.UUID;


public class OpcionApoyando extends Fragment {
    FragmentOpcionApoyandoBinding binding;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("evento");;
    CometChatApiRest cometChatApiRest = new CometChatApiRest();
    HashMap<String, String> listaApoyosBarras;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOpcionApoyandoBinding.inflate(inflater, container, false);

        String uidUserActual =  firebaseAuth.getCurrentUser().getUid();
        String keyUser;
        String evento_uid;
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("evento_uid") && bundle.containsKey("listaApoyosBarras")) {
            evento_uid = bundle.getString("evento_uid");
            listaApoyosBarras = (HashMap<String, String>) bundle.getSerializable("listaApoyosBarras");
        } else {
            evento_uid = "evento_uid_blank";
        }
        Set<String> keys = listaApoyosBarras.keySet();
        //validando
        keyUser = keys.stream().filter(key -> !key.equalsIgnoreCase("ga") && listaApoyosBarras.get(key).equals(uidUserActual)).findFirst().orElse(new String());
        binding.cancelarApoyo.setOnClickListener(view -> {
            if (getActivity() instanceof DetallesEvento1) {
                DetallesEvento1 activity = (DetallesEvento1) getActivity();
                Log.d("msg-test-evento-uid",evento_uid);
                listaApoyosBarras.remove(keyUser);
                HashMap<String, Object> eventoUpdate = new HashMap<>();
                eventoUpdate.put("listaApoyosBarras", listaApoyosBarras);
                //removiendo al usuario de la lista de barras del evento
                databaseReference.child(evento_uid).updateChildren(eventoUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        removeUserFromEventGroupCometChat(evento_uid);
                        activity.deleteFragment(OpcionApoyando.this);
                    }
                });

            }
        });

        binding.chat.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), GeneralViewActivity.class);
            intent.putExtra("loadFragment", "chat_from_detalle_evento");
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Evita la acumulación de instancias
            //startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            /*Intent intent = new Intent(requireActivity(), GeneralViewActivity.class);
            intent.putExtra("loadFragment", "chat_from_detalle_evento");
            //startActivity(intent)
            startActivityForResult(intent, REQUEST_CODE_ACTIVITY_B);*/
            requireActivity().finish();

        });
        return binding.getRoot();
    }
    public void removeUserFromEventGroupCometChat(String evento_uid) {
        String user_uid = firebaseAuth.getCurrentUser().getUid().toLowerCase();
        Log.d("msg-test-user-uid",user_uid);
        cometChatApiRest.removeMemberFromGroupEventCometChat(evento_uid,user_uid);

    }

}