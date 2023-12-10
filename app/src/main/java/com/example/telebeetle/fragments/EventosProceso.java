package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.EventAdapter;
import com.example.telebeetle.databinding.FragmentDonationBinding;
import com.example.telebeetle.databinding.FragmentEventosProcesoBinding;
import com.example.telebeetle.viewmodels.SharedViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventosProceso extends Fragment {

    FragmentEventosProcesoBinding binding;
    EventAdapter eventAdapter;
    DatabaseReference databaseReference;
    List<Evento> listaEvents;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        binding = FragmentEventosProcesoBinding.inflate(inflater,container,false);

        listaEvents = new ArrayList<>();
        eventAdapter = new EventAdapter();
        eventAdapter.setListEvents(listaEvents);
        eventAdapter.setContext(getActivity().getApplicationContext());
        binding.rvEventsProceso.setAdapter(eventAdapter);
        binding.rvEventsProceso.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        databaseReference = FirebaseDatabase.getInstance().getReference("evento"); //datos de firebase de la coleccion de "evento"
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaEvents.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Evento evento = dataSnapshot.getValue(Evento.class);
                    if(evento.getEstado().equalsIgnoreCase("en proceso")){
                        evento.setUidEvento(dataSnapshot.getKey());
                        listaEvents.add(evento);
                    }
                }
                eventAdapter.notifyDataSetChanged();
                if (binding.rvEventsProceso.getAdapter()!= null && binding.rvEventsProceso.getAdapter().getItemCount() == 0){
                    //vacio
                    Log.d("msg-test", "llega sin informacion");
                    binding.rvEventsProceso.setVisibility(View.GONE);
                    binding.textNoRegistros.setVisibility(View.VISIBLE);
                } else {
                    binding.textNoRegistros.setVisibility(View.GONE);
                    binding.rvEventsProceso.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getSearchQuery().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String query) {
                // Update RecyclerView based on the search query
                searchList(query);
            }
        });

        return binding.getRoot();
    }
    public void searchList(String text){
        ArrayList<Evento> searchList = new ArrayList<>();
        for(Evento evento: listaEvents){
            if(evento.getActividad().toLowerCase().contains(text.toLowerCase()) || evento.getEtapa().toLowerCase().contains(text.toLowerCase())){
                searchList.add(evento);
            }
        }
        eventAdapter.searchDataList(searchList);
    }
}