package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;

import java.util.ArrayList;
import java.util.List;

public class DetalleActividad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad);
        Button crearEvento = findViewById(R.id.buttonCrearEvento);
        Button finalizarEvento = findViewById(R.id.buttonFinalizarEvento);
        crearEvento.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleActividad.this, CrearEventoActivity.class);
            startActivity(intent);
        });
        finalizarEvento.setOnClickListener(v -> {
            Toast.makeText(DetalleActividad.this,"Falta activity finalizar evento",Toast.LENGTH_SHORT).show();
        });
        ImageView arrow = findViewById(R.id.left_arrow);
        arrow.setOnClickListener(view -> {
            this.finish();
        });
        cargarListaEventos();
    }
    public List<Evento> listaEventHardcodeado(){
        List<Evento> eventoList = new ArrayList<>();
        for(int i=0; i<10; i++){
            Evento event = new Evento("Voley Damas", "Curatos de Final", "01/11/23", "Polideportivo");
            event.setHora(i+":00 pm");
            eventoList.add(event);
        }
        return eventoList;
    }
    public void cargarListaEventos(){
        List<Evento> eventoList = listaEventHardcodeado();
        EventAdapter2 eventAdapter2 = new EventAdapter2();
        eventAdapter2.setListEvents(eventoList);
        eventAdapter2.setContext(DetalleActividad.this);

        RecyclerView rv = findViewById(R.id.rvEventos2);
        rv.setAdapter(eventAdapter2);
        rv.setLayoutManager(new LinearLayoutManager(DetalleActividad.this));
    }
}