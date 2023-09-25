package com.example.telebeetle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.databinding.ActivityEventsBinding;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    ActivityEventsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        cargarListaEventos();



    }


    public List<Evento> listaEventosHardcoded  (){

        List<Evento> listaEventos = new ArrayList<>();

        Evento evento1 = new Evento();
        evento1.setActividad("Voley damas");
        evento1.setNombre("Cuartos de final");
        evento1.setFecha("10/10/23 6:00pm");
        evento1.setLugar("Polideportivo: nave 1");

        listaEventos.add(evento1);

        Evento evento2 = new Evento();
        evento2.setActividad("Futsal Varones");
        evento2.setNombre("Fase de Grupos");
        evento2.setFecha("09/10/23 5:00pm");
        evento2.setLugar("Cancha de minas");

        listaEventos.add(evento2);

        return listaEventos;



    }

    public void cargarListaEventos(){
        List<Evento> listaEventos = listaEventosHardcoded();
        EventAdapter eventAdapter = new EventAdapter();
        eventAdapter.setListEvents(listaEventos);
        eventAdapter.setContext(EventsActivity.this);


        binding.rvEvents.setAdapter(eventAdapter);
        binding.rvEvents.setLayoutManager(new LinearLayoutManager(EventsActivity.this));


    }

}