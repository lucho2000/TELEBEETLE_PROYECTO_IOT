package com.example.telebeetle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityReciboDonaBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ReciboDonaActivity extends AppCompatActivity {

    ActivityReciboDonaBinding binding;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReciboDonaBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Button botonParaKit = findViewById(R.id.buttonKit);

        //hacia la vista del recojo del kit teleco
        botonParaKit.setOnClickListener(view -> {

            Intent intent = new Intent(ReciboDonaActivity.this, KitTelecoActivity.class );
            startActivity(intent);
        });

        firebaseAuth = FirebaseAuth.getInstance();

        String usuarioActualUID = firebaseAuth.getCurrentUser().getUid();

        databaseReference  = FirebaseDatabase.getInstance().getReference("usuarios");

        databaseReference.child(usuarioActualUID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    Random random = new Random();
                    int longitudCadena = 8; // Puedes ajustar la longitud según tus necesidades

                    // Generar una cadena de números aleatorios
                    StringBuilder cadenaAleatoria = new StringBuilder();

                    for (int i = 0; i < longitudCadena; i++) {
                        int digito = random.nextInt(10); // Números aleatorios entre 0 y 9
                        cadenaAleatoria.append(digito);
                    }

                    // Imprimir la cadena generada
                    //System.out.println("Cadena aleatoria: " + cadenaAleatoria.toString());



                    String condicion = snapshot.child("condicion").getValue(String.class);
                    String nombre = snapshot.child("nombres").getValue(String.class);
                    String apellidos = snapshot.child("apellidos").getValue(String.class);
                    String correo = snapshot.child("correo").getValue(String.class);

                    binding.textView11.setText(nombre + " " +apellidos) ;
                    binding.textView13.setText(correo);
                    binding.textView17.setText("Donacion a la AITEL"); //asunto
                    binding.textView18.setText(cadenaAleatoria.toString()); //numero de transaccion
                    binding.textView19.setText("S/80"); //monto preguntar como lo obtengo
                    binding.textView21.setText(""); //fecha y hora
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}