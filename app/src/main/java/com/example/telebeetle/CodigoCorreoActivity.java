package com.example.telebeetle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CodigoCorreoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_correo);


        /* LINKEO HACIA CAMBIO CONTRASENIA  */
        Button buttonContinuar = findViewById(R.id.buttonContinuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CodigoCorreoActivity.this, CambioContraseniaActivity.class);
                startActivity(intent);
            }
        });
        /* LINKEO HACIA CAMBIO CONTRASENIA */

        /* LINKEO HACIA INGRESO CORREO */
        Button buttonCancelar = findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CodigoCorreoActivity.this, IngresoCorreoActivity.class);
                startActivity(intent);
            }
        });
        /* LINKEO HACIA INGRESO CORREO */


    }
}