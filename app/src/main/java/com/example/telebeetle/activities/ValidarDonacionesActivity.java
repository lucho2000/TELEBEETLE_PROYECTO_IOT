package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ValidarDonacionesActivity extends AppCompatActivity {

    List<Donacion> solicitudesDonaciones;

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_donaciones);
        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cargarListaDonaciones();
    }

    public List<Donacion> listaDonacionesHardcodeada(){
        List<Donacion> donacions = new ArrayList<>();
        for(int i=0; i<5; i++){
            Donacion donacion = new Donacion();
            donacion.setMonto("10"+i);
            donacion.setFecha("23/10/23");
            //donacion.setDonante("Julio Aliaga");
            donacions.add(donacion);
        }
        return donacions;
    }
    public void cargarListaDonaciones(){
        //List<Donacion> donacions = listaDonacionesHardcodeada();
        solicitudesDonaciones = new ArrayList<>();
        TextView textView = findViewById(R.id.textNoRegistros);
        ValidarDonacionesAdapter validarDonacionesAdapter = new ValidarDonacionesAdapter();
        validarDonacionesAdapter.setListaValidarDonaciones(solicitudesDonaciones);
        validarDonacionesAdapter.setContext(ValidarDonacionesActivity.this);
        RecyclerView rv = findViewById(R.id.rv_validardonaciones);
        rv.setAdapter(validarDonacionesAdapter);
        rv.setLayoutManager(new LinearLayoutManager(ValidarDonacionesActivity.this));

        databaseReference2 = FirebaseDatabase.getInstance().getReference("donaciones_por_validar");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                solicitudesDonaciones.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String keyDonacion = dataSnapshot.getKey();
                    Donacion donacion = dataSnapshot.getValue(Donacion.class);
                    donacion.setKeyDonacion(keyDonacion);
                    solicitudesDonaciones.add(donacion);
                }
                validarDonacionesAdapter.notifyDataSetChanged();
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
}