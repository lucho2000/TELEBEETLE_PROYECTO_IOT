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

import com.example.telebeetle.Entity.Actividad;
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
    DatabaseReference databaseReference2;
    List<Evento> listaEvents;
    List<String> nombresActividades;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        binding = FragmentEventosProcesoBinding.inflate(inflater,container,false);
        listaEvents = new ArrayList<>();
        nombresActividades = new ArrayList<>();
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
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("actividad"); //datos de firebase de la coleccion de "evento"
                        databaseReference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                for(DataSnapshot dataSnapshot1: snapshot1.getChildren()){
                                    Actividad actividad = dataSnapshot1.getValue(Actividad.class);
                                    if(evento.getActividad().equals(dataSnapshot1.getKey())){
                                        nombresActividades.add(actividad.getNombreActividad()) ;
                                        break;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
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
        return binding.getRoot();
    }
    @Override
    public void onResume(){
        super.onResume();
        eventAdapter.searchDataList(listaEvents);
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getSearchQuery().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String query) {
                if(!query.equals("")){
                    searchList(query);
                }else{
                    eventAdapter.searchDataList(listaEvents);
                }
            }
        });
    }
    public void searchList(String text){
        List<Evento> searchList = new ArrayList<>();
        for(int i=0; i<listaEvents.size(); i++){
            if(nombresActividades.get(i).toLowerCase().contains(text.toLowerCase()) || listaEvents.get(i).getEtapa().toLowerCase().contains(text.toLowerCase())){
                searchList.add(listaEvents.get(i));
            }
        }
        eventAdapter.searchDataList(searchList);
    }
}