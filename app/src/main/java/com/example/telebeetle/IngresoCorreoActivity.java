package com.example.telebeetle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IngresoCorreoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_correo);


        /* LINKEO HACIA CODIGO CORREO  */
        Button buttonContinuar = findViewById(R.id.buttonContinuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngresoCorreoActivity.this, CodigoCorreoActivity.class);
                startActivity(intent);
            }
        });
        /* LINKEO HACIA CODIGO CORREO */


        /* LINKEO HACIA INICIO SESION / MAIN ACTIVITY  */
        Button buttonCancelar= findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngresoCorreoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        /* LINKEO HACIA INICIO SESION / MAIN ACTIVITY */


    }
}