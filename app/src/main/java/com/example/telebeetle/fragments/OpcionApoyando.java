package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.activities.DetallesEvento1;
import com.example.telebeetle.cometchatapi.CometChatApiRest;
import com.example.telebeetle.databinding.FragmentOpcionApoyandoBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class OpcionApoyando extends Fragment {
    FragmentOpcionApoyandoBinding binding;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CometChatApiRest cometChatApiRest = new CometChatApiRest();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOpcionApoyandoBinding.inflate(inflater, container, false);

        String evento_uid;
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("evento_uid")) {
            evento_uid = bundle.getString("evento_uid");

        } else {
            evento_uid = "evento_uid_blank";
        }


        binding.cancelarApoyo.setOnClickListener(view -> {
            if (getActivity() instanceof DetallesEvento1) {
                DetallesEvento1 activity = (DetallesEvento1) getActivity();
                Log.d("msg-test-evento-uid",evento_uid);
                removeUserFromEventGroupCometChat(evento_uid);
                activity.deleteFragment(OpcionApoyando.this);

            }
        });
        return binding.getRoot();
    }


    public void removeUserFromEventGroupCometChat(String evento_uid) {
        String user_uid = firebaseAuth.getCurrentUser().getUid().toLowerCase();
        Log.d("msg-test-user-uid",user_uid);
        cometChatApiRest.removeMemberFromGroupEventCometChat(evento_uid,user_uid);

    }

}