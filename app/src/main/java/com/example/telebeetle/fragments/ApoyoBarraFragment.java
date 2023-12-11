package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.ApoyoAdapter;
import com.example.telebeetle.activities.verApoyosActivity;
import com.example.telebeetle.databinding.FragmentApoyoBarraBinding;
import com.example.telebeetle.databinding.FragmentOpcionApoyandoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ApoyoBarraFragment extends Fragment {

    DatabaseReference databaseReference;
    FragmentApoyoBarraBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApoyoBarraBinding.inflate(inflater, container, false);
        Evento evento = (Evento) getArguments().getSerializable("Evento");
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        List<Usuario> listaUsuarios = new ArrayList<>();
        ApoyoAdapter apoyoAdapter = new ApoyoAdapter();
        apoyoAdapter.setListApoyo(listaUsuarios);
        apoyoAdapter.setContext(getActivity());
        binding.rvApoyobarra.setAdapter(apoyoAdapter);
        binding.rvApoyobarra.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    Set<String> keys3 = evento.getListaApoyosBarras().keySet();
                    for (String key : keys3) {
                        if(!key.equalsIgnoreCase("ga") && evento.getListaApoyosBarras().get(key).equals(dataSnapshot.getKey())){ //validando
                            listaUsuarios.add(usuario);
                            break;
                        }
                    }
                }
                apoyoAdapter.notifyDataSetChanged();
                if (binding.rvApoyobarra.getAdapter()!= null && binding.rvApoyobarra.getAdapter().getItemCount() == 0){
                    //vacio
                    Log.d("msg-test", "llega sin informacion");
                    binding.rvApoyobarra.setVisibility(View.GONE);
                    binding.textNoRegistros.setVisibility(View.VISIBLE);
                } else {
                    binding.textNoRegistros.setVisibility(View.GONE);
                    binding.rvApoyobarra.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
    public static ApoyoBarraFragment newInstance(Evento evento) {
        ApoyoBarraFragment fragment = new ApoyoBarraFragment();
        Bundle args = new Bundle();
        args.putSerializable("Evento", evento);
        fragment.setArguments(args);
        return fragment;
    }
}