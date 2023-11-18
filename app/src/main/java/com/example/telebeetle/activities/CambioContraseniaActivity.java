package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityCambioContraseniaBinding;
import com.example.telebeetle.databinding.ActivityMainBinding;
import com.example.telebeetle.services.Regex;

public class CambioContraseniaActivity extends AppCompatActivity {

    ActivityCambioContraseniaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCambioContraseniaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* LINKEO HACIA INGRESO CORREO */
        Button buttonCancelar = findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CambioContraseniaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /* LINKEO HACIA INGRESO CORREO */




        Regex regex = new Regex();


        binding.buttonAceptar.setOnClickListener(v -> {
            String contrasena =  binding.contrasena.getEditText().getText().toString();
            String contrasena2 = binding.nuevaContrasena2.getEditText().getText().toString();
            if(contrasena2.equalsIgnoreCase(contrasena) && regex.contrasenaisValid(contrasena)){

                Intent intent = new Intent(CambioContraseniaActivity.this, CambioContrasenia2Activity.class);
                startActivity(intent);

            }else {
                Toast.makeText(CambioContraseniaActivity.this, "Revise si la contraseña actual, ademá deben ser iguales", Toast.LENGTH_SHORT).show();
            }
        });





    }
}