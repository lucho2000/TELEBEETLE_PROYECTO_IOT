package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.telebeetle.Entity.Evento;

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
        eventAdapter.setContext(EventsActivity.this);


        binding.rvEvents.setAdapter(eventAdapter);
        binding.rvEvents.setLayoutManager(new LinearLayoutManager(EventsActivity.this));


    }

}