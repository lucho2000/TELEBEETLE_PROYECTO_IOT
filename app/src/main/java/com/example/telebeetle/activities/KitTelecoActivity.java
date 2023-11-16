package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityKitTelecoBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.time.LocalDate;

public class KitTelecoActivity extends AppCompatActivity {

    ActivityKitTelecoBinding binding;
    FirebaseDatabase database;

    StorageReference storageReference;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKitTelecoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String usuarioActualUID = firebaseAuth.getCurrentUser().getUid();
        Task<DataSnapshot> usuario = database.getReference("usuarios").child(usuarioActualUID).get();
        Usuario usuario1 = (Usuario) usuario.getResult().getValue();

        binding.textView23.setText(usuario1.getNombres() + "" + usuario1.getApellidos()) ;
        binding.textView24.setText(usuario1.getCorreo());
        binding.textView25.setText("Oficina de la AITEL (Primer piso del pabellon V)");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.textView26.setText(LocalDate.now().toString());
        }

        binding.textView27.setText("15:00 horas");

    }
}