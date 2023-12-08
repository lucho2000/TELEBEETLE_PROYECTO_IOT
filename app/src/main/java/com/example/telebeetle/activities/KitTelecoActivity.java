package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityKitTelecoBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.time.LocalDate;

public class KitTelecoActivity extends AppCompatActivity {

    ActivityKitTelecoBinding binding;
    DatabaseReference database;

    StorageReference storageReference;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKitTelecoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("usuarios");

        String usuarioActualUID = firebaseAuth.getCurrentUser().getUid();
        database.child(usuarioActualUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String condicion = snapshot.child("condicion").getValue(String.class);
                    String nombre = snapshot.child("nombres").getValue(String.class);
                    String apellidos = snapshot.child("apellidos").getValue(String.class);
                    String correo = snapshot.child("correo").getValue(String.class);
                    Log.d("msg-test", "Usuario llego con nombre: " + nombre + " " +apellidos);
                    binding.textView23.setText(nombre + " " +apellidos) ;
                    binding.textView24.setText(correo);
                    binding.textView25.setText("Oficina de la AITEL");

                    Calendar calendar = Calendar.getInstance();

                    // Sumar un día
                    calendar.add(Calendar.DAY_OF_MONTH, 1);

                    // Obtener la nueva fecha
                    int nuevoAnio = calendar.get(Calendar.YEAR);
                    int nuevoMes = calendar.get(Calendar.MONTH) + 1; // Se suma 1 porque los meses empiezan desde 0
                    int nuevoDia = calendar.get(Calendar.DAY_OF_MONTH);

                    // Imprimir la nueva fecha
                    //System.out.println(nuevoDia + "/" + nuevoMes + "/" + nuevoAnio);
                    binding.textView26.setText(nuevoDia + "/" + nuevoMes + "/" + nuevoAnio);
                    binding.textView27.setText("15:00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //binding.textView25.setText("Oficina de la AITEL (Primer piso del pabellon V)");

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.textView26.setText(LocalDate.now().toString());
        }*/

        //binding.textView27.setText("15:00 horas");

    }
}