package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetalleActividad extends AppCompatActivity {

    DatabaseReference databaseReference;
    EventAdapter2 eventAdapter2;
    List<Evento> listaEvents;
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
    }
    @Override
    public void onResume() {

        super.onResume();

        databaseReference = FirebaseDatabase.getInstance().getReference("evento"); //datos de firebase de la coleccion de "evento"

        listaEvents = new ArrayList<>();

        eventAdapter2 = new EventAdapter2();
        eventAdapter2.setListEvents(listaEvents);
        eventAdapter2.setContext(DetalleActividad.this);
        RecyclerView rv = findViewById(R.id.rvEventos2);
        rv.setAdapter(eventAdapter2);
        rv.setLayoutManager(new LinearLayoutManager(DetalleActividad.this));

        //codigo para extraer la data de firebase y mostrarla en el recycler view
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaEvents.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uidEvento = dataSnapshot.getKey();
                    Evento evento = dataSnapshot.getValue(Evento.class);
                    Log.d("uid", uidEvento);
                    evento.setUidEvento(uidEvento);
                    listaEvents.add(evento);
                }
                eventAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}