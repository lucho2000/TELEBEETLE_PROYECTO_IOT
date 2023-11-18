package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.telebeetle.R;

public class CambioContraseniaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_contrasenia);

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



    }
}