package com.example.telebeetle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ReciboDonaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibo_dona);

        Button botonParaKit = findViewById(R.id.buttonKit);

        //hacia la vista del recojo del kit teleco
        botonParaKit.setOnClickListener(view -> {

            Intent intent = new Intent(ReciboDonaActivity.this, KitTelecoActivity.class );
            startActivity(intent);
        });
    }
}