package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telebeetle.Entity.Actividad;
import com.example.telebeetle.Entity.Evento;
import com.example.telebeetle.R;
import com.google.firebase.auth.FirebaseAuth;
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
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference2;
    String uidActividad;
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
            Intent intent = new Intent(DetalleActividad.this, FinalizarEventoActivity.class);
            startActivity(intent);
        });
        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onResume() {

        super.onResume();
        TextView nombreActividad = findViewById(R.id.textView37);
        databaseReference2 = FirebaseDatabase.getInstance().getReference("actividad");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Actividad actividad1 = dataSnapshot.getValue(Actividad.class);
                    if(actividad1.getDelegado().equals(firebaseAuth.getCurrentUser().getUid())){
                        uidActividad = dataSnapshot.getKey();
                        nombreActividad.setText(actividad1.getNombreActividad());
                        break;
                    }
                }
                databaseReference = FirebaseDatabase.getInstance().getReference("evento"); //datos de firebase de la coleccion de "evento"

                listaEvents = new ArrayList<>();

                eventAdapter2 = new EventAdapter2();
                eventAdapter2.setListEvents(listaEvents);
                eventAdapter2.setContext(DetalleActividad.this);
                RecyclerView rv = findViewById(R.id.rvEventos2);
                rv.setAdapter(eventAdapter2);
                rv.setLayoutManager(new LinearLayoutManager(DetalleActividad.this));
                TextView textView = findViewById(R.id.textNoRegistros);

                //codigo para extraer la data de firebase y mostrarla en el recycler view
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaEvents.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Evento evento = dataSnapshot.getValue(Evento.class);
                            if(evento.getActividad().equals(uidActividad)){
                                evento.setUidEvento(snapshot.getKey());
                                listaEvents.add(evento);
                            }
                        }
                        eventAdapter2.notifyDataSetChanged();
                        if (rv.getAdapter()!= null && rv.getAdapter().getItemCount() == 0){
                            //vacio
                            Log.d("msg-test", "llega sin informacion");
                            rv.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        } else {
                            textView.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}