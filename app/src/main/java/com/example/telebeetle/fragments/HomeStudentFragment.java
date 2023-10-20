package com.example.telebeetle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.EventAdapter;
import com.example.telebeetle.activities.EventsActivity;
import com.example.telebeetle.databinding.FragmentHomeStudentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeStudentFragment extends Fragment {

    FragmentHomeStudentBinding binding;


    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeStudentBinding.inflate(inflater,container,false);

        cargarListaEventos();

        return binding.getRoot();
    }
    public List<Evento> listaEventosHardcoded  (){

        List<Evento> listaEventos = new ArrayList<>();

        Evento evento1 = new Evento("Voley damas","Cuartos de Final","10/10/23 6:00pm","Polideportivo: nave 1");
        listaEventos.add(evento1);


        Evento evento2 = new Evento("Futsal Varones","Fase de Grupos","09/10/23 5:00pm","Cancha de minas");
        listaEventos.add(evento2);


        Evento evento3 = new Evento("Ajedrez","Clasificatorias","11/10/23 6:00pm","Polideportivo");
        listaEventos.add(evento3);

        Evento evento4 = new Evento("Basquet Varones","Fase de Grupos","12/10/23 6:00pm","Polideportivo: nave 3");
        listaEventos.add(evento4);

        Evento evento5 = new Evento("Atletismo","Etapa inicial","14/10/23 10:00am","Circuito");
        listaEventos.add(evento5);


        return listaEventos;



    }

    public void cargarListaEventos(){

        databaseReference = FirebaseDatabase.getInstance().getReference("evento"); //datos de firebase de la coleccion de "evento"

        //List<Evento> listaEventos = listaEventosHardcoded();

        List<Evento> listaEventos2 = new ArrayList<>();

        EventAdapter eventAdapter = new EventAdapter();
        eventAdapter.setListEvents(listaEventos2);
        eventAdapter.setContext(getActivity().getApplicationContext());
        binding.rvEvents.setAdapter(eventAdapter);
        binding.rvEvents.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        //codigo para extraer la data de firebase y mostrarla en el recycler view
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Evento evento = dataSnapshot.getValue(Evento.class);
                    listaEventos2.add(evento);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}