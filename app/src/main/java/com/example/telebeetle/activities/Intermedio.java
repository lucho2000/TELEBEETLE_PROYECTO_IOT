package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.telebeetle.databinding.ActivityIntermedioBinding;

public class Intermedio extends AppCompatActivity {
    ActivityIntermedioBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntermedioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /* LINKEO HACIA ACTIVITY PRINCIPAL USUARIO/*/
        binding.button3.setOnClickListener(view -> {
            Intent intent = new Intent(Intermedio.this, DetallesEvento1.class);
            startActivity(intent);
        });
        /* LINKEO HACIA ACTIVITY PRINCIPAL USUARIO/*/
    }
}