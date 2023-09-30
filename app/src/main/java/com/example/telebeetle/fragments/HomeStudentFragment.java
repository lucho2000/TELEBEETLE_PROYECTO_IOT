package com.example.telebeetle.fragments;

import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;


public class HomeStudentFragment extends Fragment {

    FragmentHomeStudentBinding binding;
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
        List<Evento> listaEventos = listaEventosHardcoded();
        EventAdapter eventAdapter = new EventAdapter();
        eventAdapter.setListEvents(listaEventos);
        eventAdapter.setContext(getActivity().getApplicationContext());
        binding.rvEvents.setAdapter(eventAdapter);
        binding.rvEvents.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


    }
}