package com.example.telebeetle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* LINKEO HACIA REGISTRO DE USUARIO */
        TextView registerTextView = findViewById(R.id.registrarmeTextView);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        /* LINKEO HACIA REGISTRO DE USUARIO */




        /* LINKEO HACIA OLVIDE MI CONTRASENA / INGRESO CORREO */
        TextView olvideMiContraseniaTextView = findViewById(R.id.olvideMiContraseniaTextView);
        olvideMiContraseniaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IngresoCorreoActivity.class);
                startActivity(intent);
            }
        });
        /* LINKEO HACIA OLVIDE MI CONTRASENA / INGRESO CORREO */

    }
}