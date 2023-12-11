package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.ApoyoAdapter;
import com.example.telebeetle.databinding.FragmentApoyoParticipanteBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ApoyoParticipanteFragment extends Fragment {

    DatabaseReference databaseReference;
    FragmentApoyoParticipanteBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApoyoParticipanteBinding.inflate(inflater, container, false);
        Evento evento = (Evento) getArguments().getSerializable("Evento");
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        List<Usuario> listaUsuarios = new ArrayList<>();
        ApoyoAdapter apoyoAdapter = new ApoyoAdapter();
        apoyoAdapter.setListApoyo(listaUsuarios);
        apoyoAdapter.setContext(getActivity());
        binding.rvApoyoparticipante.setAdapter(apoyoAdapter);
        binding.rvApoyoparticipante.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    Set<String> keys1 = evento.getListaApoyosParticipantesValidados().keySet();
                    for (String key : keys1) {
                        if(!key.equalsIgnoreCase("ga") && evento.getListaApoyosParticipantesValidados().get(key).equals(dataSnapshot.getKey())){ //validando
                            listaUsuarios.add(usuario);
                            break;
                        }
                    }
                }
                apoyoAdapter.notifyDataSetChanged();
                if (binding.rvApoyoparticipante.getAdapter()!= null && binding.rvApoyoparticipante.getAdapter().getItemCount() == 0){
                    //vacio
                    Log.d("msg-test", "llega sin informacion");
                    binding.rvApoyoparticipante.setVisibility(View.GONE);
                    binding.textNoRegistros.setVisibility(View.VISIBLE);
                } else {
                    binding.textNoRegistros.setVisibility(View.GONE);
                    binding.rvApoyoparticipante.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
    public static ApoyoParticipanteFragment newInstance(Evento evento) {
        ApoyoParticipanteFragment fragment = new ApoyoParticipanteFragment();
        Bundle args = new Bundle();
        args.putSerializable("Evento", evento);
        fragment.setArguments(args);
        return fragment;
    }
}