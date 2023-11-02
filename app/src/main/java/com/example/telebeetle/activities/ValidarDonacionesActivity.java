package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.R;

import java.util.ArrayList;
import java.util.List;

public class ValidarDonacionesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_donaciones);
        cargarListaDonaciones();
    }

    public List<Donacion> listaDonacionesHardcodeada(){
        List<Donacion> donacions = new ArrayList<>();
        for(int i=0; i<5; i++){
            Donacion donacion = new Donacion();
            donacion.setMonto("10"+i);
            donacion.setFecha("23/10/23");
            donacion.setDonante("Julio Aliaga");
            donacions.add(donacion);
        }
        return donacions;
    }
    public void cargarListaDonaciones(){
        List<Donacion> donacions = listaDonacionesHardcodeada();
        ValidarDonacionesAdapter validarDonacionesAdapter = new ValidarDonacionesAdapter();
        validarDonacionesAdapter.setListaValidarDonaciones(donacions);
        validarDonacionesAdapter.setContext(ValidarDonacionesActivity.this);
        RecyclerView rv = findViewById(R.id.rv_validardonaciones);
        rv.setAdapter(validarDonacionesAdapter);
        rv.setLayoutManager(new LinearLayoutManager(ValidarDonacionesActivity.this));

    }
}