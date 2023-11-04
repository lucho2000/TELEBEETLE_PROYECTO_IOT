package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.telebeetle.R;

public class QRDonarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdonar);


        Button botonEnviar = findViewById(R.id.buttonSubirImagen);

        //hacia la vista de la pantalla de espera perosnalizada
        botonEnviar.setOnClickListener(view -> {

            Intent intent = new Intent(QRDonarActivity.this, ScreenEsperaActivity.class);
            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}