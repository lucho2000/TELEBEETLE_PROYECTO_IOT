package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityAfterGoogleBinding;
import com.example.telebeetle.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AfterGoogleActivity extends AppCompatActivity {

    ActivityAfterGoogleBinding binding;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAfterGoogleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Spinner spinner = binding.spinner3;
        String[] roles = {"Alumno","Egresado"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,roles );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios_por_admitir");

        binding.button3.setOnClickListener(view -> {
            String code = binding.editTextText.getText().toString();
            String role = spinner.getSelectedItem().toString();

            if (!code.isEmpty() && !role.isEmpty()) {
                // Obtiene el usuario actual de Firebase
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Crea un mapa con la información a almacenar en la base de datos
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("nombres", user.getDisplayName());
                    map.put("correo", user.getEmail());
                    map.put("profile", user.getPhotoUrl().toString());
                    map.put("codigo", code);
                    map.put("condicion", role);

                    // Almacena la información en la base de datos
                    databaseReference.child(user.getUid()).setValue(map)
                            .addOnSuccessListener(aVoid -> {
                                // Los datos se guardaron con éxito
                                Intent intent = new Intent(AfterGoogleActivity.this,WaitActivity.class);
                                startActivity(intent);
                                finish();
                                //Toast.makeText(AfterGoogleActivity.this, "Información guardada", Toast.LENGTH_SHORT).show();

                            })
                            .addOnFailureListener(e -> {
                                // Ocurrió un error al guardar los datos
                                Toast.makeText(AfterGoogleActivity.this, "Error al guardar la información", Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                // Notifica al usuario que los campos deben estar completos
                Toast.makeText(AfterGoogleActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            }


        });

    }
}